/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class Report extends BaseTemplate
{
    public Report(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC)
    {
        super(uniqueNumberGen, aC);
        
        addType("<http://purl.org/ontology/bibo/Report>");
        addType("<http://vivoweb.org/ontology/core#InformationResource>");
        
        for(String line : newEntry)
        {
            if (line.startsWith("T1") || line.startsWith("TI"))
            {
                addTitle(line);
            }
            else if (line.startsWith("Y1") || line.startsWith("PY"))
            {
                addDate(line);
            }
            else if (line.startsWith("AU") || line.startsWith("A1") || line.startsWith("A2") || line.startsWith("A3") || line.startsWith("A4"))
            {
                addAuthor(line);
            }   
        }        
        
        completeEntry();        
    }    
}
