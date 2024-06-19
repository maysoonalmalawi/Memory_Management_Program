/*
    -- Designing and simulating a contiguous region of memory --
    Operating System CPCS-361

    Compiler name and version: Netbeans IDE 8.2
    Hardware processor: Intel(R) Core(TM) i7-10510U CPU @ 1.80GHz   2.30 GHz
    Operating system and version: Microsoft Windows 10 Home version 10.0.19045 Build 19045

 */
package cpcs361_poject_part1_mainmemory_program;

public class Process implements Comparable{
    
    // declare variables
    private String processName;
    private int processNum;
    private int base;
    private int limit;
    private int size;

    public Process() {
    }

    public Process(String processName, int processNum, int base, int limit, int size) {
        this.processName = processName;
        this.processNum = processNum;
        this.base = base;
        this.limit = limit;
        this.size = size;
    }
    
    // setters
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    //Getters
    public String getProcessName() {
        return processName;
    }

    public int getProcessNum() {
        return processNum;
    }

    public int getBase() {
        return base;
    }

    public int getLimit() {
        return limit;
    }

    public int getSize() {
        return size;
    }
    
    @Override
    public int compareTo(Object t) {
        int comparebase=((Process)t).getBase();
        return this.base-comparebase;
    }

    
    
    
}
