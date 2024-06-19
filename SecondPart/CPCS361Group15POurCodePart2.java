/*
    -- Designing and simulating a contiguous region of memory --
    Operating System CPCS-361

    Compiler name and version: Netbeans IDE 8.2
    Hardware processor: Intel(R) Core(TM) i5-7200U CPU @ 2.50GHz
    Operating system and version: Microsoft Windows 10 Home Single Language Version 21H2 OS Build 19044.2604

 */

package cpcs361group15pourcodepart2;

// java imports
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CPCS361Group15POurCodePart2 {
    
    // declarations & initializations
    static HashMap<Integer, Byte> myTestData = new HashMap<>();
    static final int FRAME_SIZE = 256;
    static final int FRAMES_NUM = 256;
    static final int PAGE_SIZE = 256;
    static int pagetable[] = new int[256];
    static int frametable[]= new int[FRAMES_NUM];
    static byte physicalMemory[] = new byte[FRAME_SIZE * FRAMES_NUM];
    static int logicaladdress, pagenumber, offset, physicaladdress, frame = 0;
    static byte correctValue;
    static int pagefaults = 0, pagehits = 0;
    

    public static void main(String[] args) throws FileNotFoundException{
        
        // Create files
        File address = new File("addresses.txt");
        File correct = new File("correct.txt");
        
        // if addresses file doesn't exist
        if(!address.exists()){
            System.out.println("Addresses file not found");
        }
        
        // if correct file doesn't exist
        if(!correct.exists()){
            System.out.println("Correct Values file not found.");
        }
        
        // Create Scnnaers
        Scanner addressInput = new Scanner(address);
        Scanner correctInput = new Scanner(correct);
        
        // initialize page table
        initialize(pagetable);
        
        // initialize frame table
        initialize(frametable);
        
        // initialize physical memory
        initialize(physicalMemory);
        
        // store 100 values from input files
        storeValues(addressInput, correctInput, 100);
        
        // For testing, create a hashmap of five random elements from myTestData 
        HashMap<Integer, Byte> randomTestCase = new HashMap<>(); 
        // an array to make sure the five addresses are unique 
        int[] uniqueArr = new int[5]; 
         
        // populate randomTestCase hashmap 
        int index = 0; 
        while(index <5){ 
            // get a random logical address 
            int logAdd = getRandomAddress(myTestData); 
            // if it's unique, store it in logArr,get its correct value, store them in randomTestCase, and increment the index 
            if(uniqueElement(logAdd, uniqueArr)){ 
                byte value = myTestData.get(logAdd); 
                uniqueArr[index] = logAdd; 
                randomTestCase.put(logAdd, value); 
                index++; 
            }   
        }
        
        // test the retrieving requirement for the randomized test case
        testRetrieve(randomTestCase);

        // Test R2-Second Part
        countPageFaults(addressInput);

    }
    
    
    
    /* ----------------------------------Requirements ----------------------------------*/
    
    //implementation of R1
    // storing values in the physical memory given their logical addresses
    public static void storeValues(Scanner addressInput, Scanner correctInput, int length){
        // create index
        int index = 0;
        
        // read 100 logical addresses and values from the input files
        while(index<length){
            
            // populate logical addresses data structure
            logicaladdress = addressInput.nextInt();
            correctValue = (byte) correctInput.nextInt();
            myTestData.put(logicaladdress, correctValue);
          
            // extract page number and offset
            pagenumber = extractPageNumber(logicaladdress);
            offset = extractOffset(logicaladdress);
            
            // assign frame number to page number if it doesnt already exist in page table
            if(pagetable[pagenumber] == -1){
                // keep generating a random frame until we get a unique one and assign it
                while(pagetable[pagenumber] == -1){
                    // generate random frame
                    frame = randomFrame();
                    // check uniqueness of frame
                    if(uniqueElement(frame, frametable)){
                        pagetable[pagenumber] = frame;
                    }
                }
            }else{
                // if page number already exists in page table, just retrieve it and use the frame number stored in it
                frame = pagetable[pagenumber];
            }
            
            // compute physical address 
            physicaladdress = computePA(frame, offset);
            
            // check uniqueness of physical address.
            //However, we don't really need to do this step since the logical addresses in the file are unique
            if(physicalMemory[physicaladdress] != -1){
                while(physicalMemory[physicaladdress] != -1){
                    frame = randomFrame();
                    if(uniqueElement(frame, frametable)){
                        physicaladdress = computePA(frame, offset);
                    }
                }
            }
            
            // store the correct value in the physical memory in the computed physical address
            physicalMemory[physicaladdress] = correctValue;
            
            // increment index
            index++;
        }
    }
    
    
    // implementation of R2
    // Retrieving a value from physical memory given its logical address
    public static Byte retrieveValue(Integer R2logadd){
            
            // calculate pagenumber
            pagenumber = extractPageNumber(R2logadd);
            
            // calculate offset
            offset = extractOffset(R2logadd);
             
            // get frame number from page table
            frame = pagetable[pagenumber];
            
            // use frame #, frame size, and offset
            physicaladdress = computePA(frame, offset);
            
            // retrieve value from physical address
            byte R2storedValue = physicalMemory[physicaladdress];
            
            return R2storedValue;
            
    }
    
    // Testing R2 - First Part
    // testing the retrieve function
    public static void testRetrieve(HashMap<Integer, Byte> testLogs){
        System.out.println("The result of retrieving the values of 5 logical addresses: ");
        System.out.println("Logical Address\t Page#\t Offset\t Frame#\t Value\t Same as model answer");
        // for each address in testLogs, do the following
        for(Map.Entry<Integer, Byte> entry: testLogs.entrySet()){
            // get logical address and correct value (model answer)
            Integer R2logadd = entry.getKey();
            byte R2correctValue = testLogs.get(R2logadd);
            
            // get value stored in memory
            byte R2storedValue = retrieveValue(R2logadd);
            
            // compare stored and correct values
            String compareResult = compareValues(R2correctValue, R2storedValue); 
            
            // print results
            System.out.println(R2logadd + "\t\t " + pagenumber + "\t " + offset + "\t " + frame + "\t " + R2storedValue + "\t " + compareResult);
        }
        System.out.println("------------------------------------------------------------------------");
    }
    
    // Testing R2 - Second Part
    // counting page faults and page hitd
    public static void countPageFaults(Scanner addressInput){
        // create a new string of 80 addresses (array data structure)
        int[] addsString = new int[80];
        
        // initialize new array of addresses
        initialize(addsString);
        
        // print header message
        System.out.println("Counting number of page faults for a string of 80 randomly chosen logical addresses: ");
        System.out.println("The string sequence is: ");
        
        // pick 50 random addresses from the 100 in myTestData
        for(int i=0; i<50; i++){
            // this while loop makes sure that all randomly chosen addresses are unique
            while(addsString[i] == -1){
                // pick a random address
                int randomAdd = getRandomAddress(myTestData);
                // if its unique, use it, and print it out
                if(uniqueElement(randomAdd, addsString)){
                    addsString[i] = randomAdd;
                    System.out.print(addsString[i] + " ");
                    
                    // for formatting
                    if(i != 0 && i%10 == 0){
                        System.out.println("");
                    }
                }
            }
        }
        
        // pick 30 new elements from the addresses input file
        for(int i=50; i<80; i++){
            // since all addresses in the file are unique, no need to check for uniqueness
            // immediately use it and print it out
            addsString[i] = addressInput.nextInt();
            System.out.print(addsString[i] + " ");
            
            // for formatting
            if(i%10 == 0){
                System.out.println("");
            }
        }
        
        // call check page faults function
        checkPagefaults(addsString);
        
        // print the results
        System.out.println("\nNumber of page faults: " + pagefaults);
        System.out.println("Number of page hits: " + pagehits);
    }
    
    
    /* ----------------------------------Services ----------------------------------*/
    
    // updating static variables: page faults, and page hits
    public static void checkPagefaults(int[] arr){
        for(int i=0; i<arr.length; i++){
            // calculate page number of logical address
            pagenumber = extractPageNumber(arr[i]);
            if(pagetable[pagenumber] == -1){
                // if it does not exist in pagetable, increment static page faults counter
                pagefaults++;
            }else{
                // if it exists, increment page hits
                pagehits++;
            }
        }
    }
    
    // initialize int arrays with -1
    public static void initialize(int[] arr){
        for(int i=0; i<arr.length;i++){
            arr[i] = -1;
        }
    }
    
    // initialize byte arrays with -1
    public static void initialize(byte[] arr){
        for(int i=0; i<arr.length;i++){
            arr[i] = -1;
        }
    }
    
    // check if an element in an array is unique
    public static Boolean uniqueElement(int element, int[] arr){
        for(int i=0; i<arr.length; i++){
            if(arr[i] == element){
                return false;
            }
        }
        return true;
    }
    
    // generate a random frame
    public static int randomFrame(){
        return (int) Math.floor(Math.random() * FRAMES_NUM);
    }
    
    // compute page number of a logical address
    public static int extractPageNumber(int logicalAddress){
        return logicalAddress / PAGE_SIZE;
    }
    
    // compute offset of a logical address
    public static int extractOffset(int logicalAddress){
        return logicalAddress % PAGE_SIZE;
    }
    
    // compute physical address, given frame and offset
    public static int computePA(int frame, int offset){
        return (frame * FRAME_SIZE) + offset;
    }
    
    // compare two values
    public static String compareValues(byte value1, byte value2){
        if(value1 == value2){
            return "yes";
        }
        return "no";
    }
    
    // pick a random address from addresses in a given HashMap
    public static int getRandomAddress(HashMap<Integer, Byte> data) {

        // get set of logical addresses
        Set<Integer> addressesSet = data.keySet();
        // convert set into a list
        List<Integer> addressesList = new ArrayList<>(addressesSet);

        // get size of list
        int size = addressesList.size();
        
        // get a random index based on the size
        int randIdx = new Random().nextInt(size);
        
        // return the logical address at the randomly chosen index
        return addressesList.get(randIdx);

    }

 }
