/*
    -- Designing and simulating a contiguous region of memory --
    Operating System CPCS-361
    Team members:
        1. Amani Biraik - B2
        2. Ghaida Alsharif - B3
        3. Leen Hosiki - B2
        4. Maysoon Almalawi - B2

    Compiler name and version: Netbeans IDE 8.2
    Hardware processor: Intel(R) Core(TM) i7-10510U CPU @ 1.80GHz   2.30 GHz
    Operating system and version: Microsoft Windows 10 Home version 10.0.19045 Build 19045

 */
package cpcs361_poject_part1_mainmemory_program;


public class Hole implements Comparable{
    
    // declare variables
    private int size;
    private int base;
    private int limit;

    // constructers
    public Hole() {
    }

    public Hole(int size, int base, int limit) {
        this.size = size;
        this.base = base;
        this.limit = limit;
    }
    

    //setters
    public void setSize(int size) {
        this.size = size;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    // Getters
    public int getSize() {
        return size;
    }

    public int getBase() {
        return base;
    }

    public int getLimit() {
        return limit;
    }
    
    @Override
    public int compareTo(Object t) {
        int comparebase=((Hole)t).getBase();
        return this.base-comparebase;
    }

    
    
}
