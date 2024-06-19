# OperatingSystemsProject
# First Part:
The first part of the project is to design and implement a contiguous region of 
memory with a known size (called MAX) that creates a range of addresses from 0 
to MAX-1.  
Multiple requests are applied in the program: requesting a contiguous block 
of memory, releasing a contiguous block of memory, compacting empty holes, and 
reporting allocated and free memory. Our implemented program will respond to 
these different requests. 
 Request a contiguous a block of memory: is allocating memory approach by 
using one of the following algorithms: first-fit (F), worst-fit (W) and best
fit (B) that is entered in the request command. 
 Release a contiguous block of memory: is the process of releasing the 
memory that is allocated to the process.  
 Compact: its role is to find all unused small holes in the memory and 
compact them as a large hole that may be used to allocate processes. 
 Report:  reporting all the regions in the memory, both allocated and free 
regions. 

To ensure that the program works properly, the following tests will take place: 
First Part: 
The goal of this part of testing is to ensure that the storing process works 
properly by retrieving from the physcial memory the expected value for each logical 
address. 
This is done by creating a new HashMap called randomTestCase and 
populating it with five random enteries from myTestData. For each logical address in 
randomTestCase, the page number will be extracted to find the frame number stored 
in the page table. The frame number will be used to compute the physical address in 
order to retrieve the value from the physical memory. Finally, the retrieved value will 
be compared to the value in randomTestCase.

# Second Part
The second part of this project is to simulate the process of translating logical 
addresses in virtual memory to physical addresses in the physical memory and 
storing the corresponding value in the latter. 
Two input files will be used: - - 
addresses.txt which contains logical addresses 
correct.txt which contains the corresponding values 
At the begnning of the program, the following initlizations are implemented: - - - - - - 
myTestData: HashMap (Key: logical address, Value: correct value) 
Frame Size = 256 bytes 
Number of Frames = 256 
Page size = 256 byets 
Size of Page Table = 256 
Size of Physcial Memory = Frame Size * Number of Frames (256 * 256) 
The program will read one hundred 32-bit logical addresses from 
addresses.txt input file. In each logical address, the 16 leftmost bits will be ignored 
(masked) and the 16 rightmost bits will be extracted as an 8-bit page number and an 
8-bit page offset and will be used in the translation process. The computed physical 
address will be used to store the corresponding value in correct.txt input file in the 
physcial memory. Additionally, every pair of logical address and correct value will be 
stored in myTestData.

# To ensure that the program works properly, the following tests will take place: 
# First Part:
The goal of this part of testing is to ensure that the storing process works 
properly by retrieving from the physcial memory the expected value for each logical 
address. 
This is done by creating a new HashMap called randomTestCase and 
populating it with five random enteries from myTestData. For each logical address in 
randomTestCase, the page number will be extracted to find the frame number stored 
in the page table. The frame number will be used to compute the physical address in 
order to retrieve the value from the physical memory. Finally, the retrieved value will 
be compared to the value in randomTestCase.
# Second Part:
The goal of this part of testing is to ensure that the program can correctly deal 
with partially loaded processes by identifying the occuerence of page faults. 
This is done by creating a new array of size 80 and populating it 50 randomly 
chosen logical addresses from myTestData and 30 new logical addresses from the 
addresses.txt input file. In addition, two counters are created to keep track fo the 
number of page faults and page hits. For each logical address, extract the page number 
and check its content in the page table. If it is empty, increment the page faults 
counter. Otherwise, increment the page hits counter. 
The criteria to evaulate the success of this test: 
1. Maxiumum of 30 page faults. 
2. Minimum of 50 page hits. 
