/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import templates.BaseTemplate;

/**
 *
 * @author dgcliff
 */
public class N3Compiler
{
    private ArrayList<String> n3Compilation = new ArrayList<>();
    
    public void addEntry(BaseTemplate bt)
    {
        ArrayList<String> newEntry = bt.getEntry();
        
        for(String s : newEntry)
        {
            n3Compilation.add(s);
        }
        
        n3Compilation.add("");
        
        if(bt.hasDate())
        {
            //need to create new individual to match the URI
            n3Compilation.add(bt.getDateUri());
            n3Compilation.add("\t a <http://vivoweb.org/ontology/core#DateTimeValue> ;");
            n3Compilation.add("\t <http://vivoweb.org/ontology/core#dateTime> " + bt.getDateValue() + " .\n");
        }
    }
    
    public void outputValues(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for(String s : n3Compilation)
            {
                out.println(s);
            }
        }        
        catch(IOException e)
        {
            System.out.println(e);
        }        
    }
}
