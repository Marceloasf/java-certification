package ocp.chapter.six;

public class Animal {
    private String name; 
    private boolean canHop;

    public Animal(String name, boolean canHop) {
        this.name = name;
        this.canHop = canHop;
    } 

    public boolean canHop() {
        return this.canHop;
    }
    
    public String toString() {
        return this.name;
    }
}