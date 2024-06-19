/*
    -- Designing and simulating a contiguous region of memory --
    Operating System CPCS-361

    Compiler name and version: Netbeans IDE 8.2
    Hardware processor: Intel(R) Core(TM) i7-10510U CPU @ 1.80GHz   2.30 GHz
    Operating system and version: Microsoft Windows 10 Home version 10.0.19045 Build 19045

 */
package cpcs361_poject_part1_mainmemory_program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CPCS361_Poject_Part1_MainMemory_Program {

    // create static array lists
    static ArrayList<Hole> hole = new ArrayList<>();
    static ArrayList<Process> process = new ArrayList<>();

    public static void main(String[] args) {

        // create scanner object
        Scanner input = new Scanner(System.in);

        // initiate variables
        int memorySize = 0;
        String command = "";

        System.out.println("--Welcome to main memory program--\n");
        // ask the user to enter memory size and number of blocks
        System.out.println("Please enter main memory size:");
        System.out.print("./allocator ");
        memorySize = input.nextInt();

        // initiate limits of the first hole
        hole.add(new Hole());
        hole.get(0).setSize(memorySize);
        hole.get(0).setBase(0);
        hole.get(0).setLimit(memorySize - 1);
        
        System.out.println("\nNote: in case you want to end the program, enter (end)");

        while (true) {

            System.out.println(" ");
            // prompt the user to enter a command
            System.out.print("allocator>");
            command = input.next();

            // Process Aloocation
            if (command.equalsIgnoreCase("RQ")) {

                // get allocation info.
                String processName = input.next().toUpperCase();
                int processSize = input.nextInt();
                char allocationMethod = input.next().toUpperCase().charAt(0);

                int processNum = Integer.parseInt(processName.replaceAll("[^0-9]", ""));

                // switch for allocation methods
                switch (allocationMethod) {
                    case 'F':
                        firstFit(processName, processNum, processSize);
                        break;

                    case 'B':
                        bestFit(processName, processNum, processSize);
                        break;
                    case 'W':
                        worstFit(processName, processNum, processSize);
                        break;
                    default:
                        throw new AssertionError();
                }

            } 
            // Release a Process
            else if (command.equalsIgnoreCase("RL")) {

                // get release info.
                String processName = input.next().toUpperCase();
                int processNum = Integer.parseInt(processName.replaceAll("[^0-9]", ""));

                // release the process
                release(processNum);

            } 
            // Compaction
            else if (command.equalsIgnoreCase("C")) {
                compact();

            } 
            // Report (STAT)
            else if (command.equalsIgnoreCase("STAT")) {
                stat(memorySize);
            }
            // end program
            else if(command.equalsIgnoreCase("END")){
                break;
            
            }

        }// end of while loop

    }// main

    /* ----------------------------------------------------------------------------------
       --------------------------------- Methods ----------------------------------------
       ----------------------------------------------------------------------------------
     */
 /* ----------------- First fit Method ------------------------------------*/
 /* -----------------------------------------------------------------------*/
    public static void firstFit(String processName, int processNum, int processSize) {

        boolean allocated = false;

        for (int i = 0; i < hole.size(); i++) {

            if (processSize <= hole.get(i).getSize()) {

                // allocation process
                int processBase = hole.get(i).getBase();
                int processLimit = processBase + processSize - 1;
                process.add(new Process(processName, processNum, processBase, processLimit, processSize));

                allocated = true;

                // check whether some part of the hole is remaining after the allocation or not
                int freeSize = hole.get(i).getSize() - processSize;

                if (freeSize == 0) {
                    hole.remove(i);
                } else {
                    hole.get(i).setBase(processLimit + 1);
                    hole.get(i).setSize(freeSize);
                }
                break;
            }
        }

        // check if the process is allocated successfully or not
        if (allocated) {
            System.out.println("\nSuccessful allocation\n");
        } else {
            System.out.println("\nError!! the process cannot be allocated in the memory.\n");
        }
        
        // sort the hole list by addreses
        Collections.sort(hole);

    } // end of first fit method

    /* ----------------- Best fit Method ------------------------------------*/
    /* ----------------------------------------------------------------------*/
    public static void bestFit(String processName, int processNum, int processSize) {

        // best size hole index variable
        int minSize = -1;

        for (int i = 0; i < hole.size(); i++) {

            int holeSize = hole.get(i).getSize();
            
            if (processSize <= holeSize) {

                if (minSize == -1) {
                    minSize = i;
                } else if (holeSize < hole.get(minSize).getSize()) {
                    minSize = i;
                }

            }
        }

        if (minSize == -1) {
            System.out.println("\nError!! the process cannot be allocated in the memory.\n");
        } else {

            // allocation process
            int processBase = hole.get(minSize).getBase();
            int processLimit = processBase + processSize - 1;
            process.add(new Process(processName, processNum, processBase, processLimit, processSize));

            // check whether some part of the hole is remaining after the allocation or not
            int freeSize = hole.get(minSize).getSize() - processSize;

            if (freeSize == 0) {
                hole.remove(minSize);
            } else {
                hole.get(minSize).setBase(processLimit + 1);
                hole.get(minSize).setSize(freeSize);
            }

            System.out.println("\nSuccessful allocation\n");

        }
        
        // sort the hole list by addreses
        Collections.sort(hole);

    } // end of best fit method
    

    /* ----------------- Worst fit Method ------------------------------------*/
    /* -----------------------------------------------------------------------*/
    public static void worstFit(String processName, int processNum, int processSize) {

        // best size hole index variable
        int maxSize = -1;

        for (int i = 0; i < hole.size(); i++) {

            int holeSize = hole.get(i).getSize();

            if (processSize <= holeSize) {

                if (maxSize == -1) {
                    maxSize = i;
                } else if (holeSize > hole.get(maxSize).getSize()) {
                    maxSize = i;
                }

            }
        }

        if (maxSize == -1) {
            System.out.println("\nError!! the process cannot be allocated in the memory.\n");
        } else {

            // allocation process
            int processBase = hole.get(maxSize).getBase();
            int processLimit = processBase + processSize - 1;
            process.add(new Process(processName, processNum, processBase, processLimit, processSize));

            // check whether some part of the hole is remaining after the allocation or not
            int freeSize = hole.get(maxSize).getSize() - processSize;

            if (freeSize == 0) {
                hole.remove(maxSize);
            } else {
                hole.get(maxSize).setBase(processLimit + 1);
                hole.get(maxSize).setSize(freeSize);
            }

            System.out.println("\nSuccessful allocation\n");

        }
        
        // sort the hole list by addreses
        Collections.sort(hole);

    } // end of worst fit method

    /* ----------------------------- Release Method ----------------------------------*/
    /* -------------------------------------------------------------------------------*/
    public static void release(int processNum) {

        int processBase = 0;
        int processLimit = 0;
        int processSize = 0;
        boolean processFound = false;

        // search for the process and release it
        for (int i = 0; i < process.size(); i++) {

            if (process.get(i).getProcessNum() == processNum) {
                processBase = process.get(i).getBase();
                processLimit = process.get(i).getLimit();
                processSize = process.get(i).getSize();
                processFound = true;
                // release the process
                process.remove(i);
            }
        }

        // in case process is not found 
        if (!processFound) {
            System.out.println("\nError!! process is not found in the memory");
        } 
        
        else {
            // add a new hole in place of the released process and check if there is an adjacent holes to combine them together
            int newHoleBase = processBase;
            int newHoleLimit = processLimit;
            int newHoleSize = processSize;
            // before adding the hole lets check if there is an adjacent holes to the released process place
            // a hole in the upper side? a hole in the lower side?
            boolean upperFound = false; int upperIndx = 0;
            boolean lowerFound = false; int lowerIndx = 0;
            for (int i = 0; i < hole.size(); i++) {

                // check if there is an upper hole to the new hole
                if (!upperFound) {
                    if (hole.get(i).getLimit() == processBase - 1) {
                        newHoleBase = hole.get(i).getBase();
                        upperIndx = i;
                        upperFound = true;
                    }
                }
                // check if there is a lower hole to the new hole
                if (!lowerFound) {
                    if (hole.get(i).getBase() == processLimit + 1) {
                        newHoleLimit = hole.get(i).getLimit();
                        lowerIndx = i;
                        lowerFound = true;
                    }
                }

                if (upperFound && lowerFound) {
                    break;
                }

            }
            
            // Remove adjacent holes if found because they are combined with the new hole
            if(upperFound)
                hole.remove(upperIndx);
            else if(lowerFound)
                hole.remove(lowerIndx);

            // add the new hole to the list
            hole.add(new Hole(newHoleSize, newHoleBase, newHoleLimit));
            
            System.out.println("\nSuccessful release");

        }
        
        // sort the hole list by addreses
        Collections.sort(hole);

    }// end of release method
    
    /* ----------------------------- Compact Method ----------------------------------*/
    /* -------------------------------------------------------------------------------*/
    public static void compact(){
    
        //count the total size of holes in the memory
        int totalHoleSize = 0;
        for (int i = 0; i < hole.size(); i++) {
            totalHoleSize = totalHoleSize + hole.get(i).getSize();
        }
        
        // sort the processes in the list based on there addresses
        Collections.sort(process);

        //shift all processes adjacent to each other in order to produce a sigle large hole
        // shift first proces to top
        if(!(process.get(0).getBase() == 0)){
            process.get(0).setBase(0);
            process.get(0).setLimit(process.get(0).getSize() - 1);
        }
        
        // shift the remaining processes
        for (int i = 1; i < process.size(); i++) {
            
            int base = process.get(i-1).getLimit() + 1;
            int limit = base + (process.get(i).getSize() - 1);
            
            process.get(i).setBase(base);
            process.get(i).setLimit(limit);
        }
        
        // set the addresses of new single hole
        // first clear hole list
        hole.clear();
        int holeBase = process.get(process.size()-1).getLimit() + 1;
        int holeLimit = holeBase + (totalHoleSize - 1);
        
        // add the hole to the list
        hole.add(new Hole(totalHoleSize, holeBase, holeLimit));
        
        System.out.println("\nSuccessful Compaction");
        
    }// end of compact method

    /* ----------------- STAT Method -----------------------------------------*/
    /* -----------------------------------------------------------------------*/
    public static void stat(int memorySize) {

        System.out.println(" ");

        // addresses
        int startAddress = 0;
        int endAddress = 0;

        while (true) {

            // search for the start address in process list
            boolean found = false;

            for (int i = 0; i < process.size(); i++) {
                int processBase = process.get(i).getBase();
                int processLimit = process.get(i).getLimit();

                if (processBase == startAddress) {
                    endAddress = processLimit;
                    System.out.println("Addresses [" + startAddress + ":" + endAddress + "] Process " + process.get(i).getProcessName());
                    found = true;
                    startAddress = processLimit + 1;
                    break;
                }
            }

            // if the start address is found then continue, no need to search in hole list
            if (found) {
                // if the end address reached the max limit of the memory then break the loop
                if (endAddress < memorySize - 1) {
                    continue;
                } else {
                    break;
                }
            }

            // search for the start address in hole list
            for (int i = 0; i < hole.size(); i++) {

                int holeBase = hole.get(i).getBase();
                int holeLimit = hole.get(i).getLimit();

                if (holeBase == startAddress) {
                    endAddress = holeLimit;
                    System.out.println("Addresses [" + startAddress + ":" + endAddress + "] Unused");
                    startAddress = holeLimit + 1;
                    break;
                }
            }

            // if the end address reached the max limit of memory then break the loop
            if (!(endAddress < memorySize - 1)) {
                break;
            }

        }

    }// end of STAT method

}// class
