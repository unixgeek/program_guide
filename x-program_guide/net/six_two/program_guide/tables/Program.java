/*
 * $Id: Program.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Program {
    private int id;
    private String name;
    
    public Program() {
        
    }
    
    public Program(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
