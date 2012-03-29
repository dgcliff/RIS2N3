/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import compilation.ConferenceCompiler;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class ConferenceProceedings extends BaseTemplate
{
    public ConferenceProceedings(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC, ConferenceCompiler cC)
    {
        super(uniqueNumberGen, aC);
        
        addType("<http://vivoweb.org/ontology/core#ConferencePaper>");
        addType("<http://purl.org/ontology/bibo/Article>");                    
        addType("<http://purl.org/ontology/bibo/Document>");
        addType("<http://vivoweb.org/ontology/core#InformationResource>");        
        
        for(String line : newEntry)
        {
            if (line.startsWith("T1") || line.startsWith("TI"))
            {
                addTitle(line);
            }
            else if (line.startsWith("KW"))
            {
                addKeywords(line);
            }
            else if (line.startsWith("Y1") || line.startsWith("PY"))
            {
                addDate(line);
            }
            else if (line.startsWith("AU") || line.startsWith("A1") || line.startsWith("A2") || line.startsWith("A3") || line.startsWith("A4"))
            {
                addAuthor(line);
            }     
            else if (line.startsWith("JF"))
            {
                String title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
                String conferenceURI = cC.addConference(title, this.getURI());                                                
                addN3("\t<http://purl.org/ontology/bibo/presentedAt> " + conferenceURI + " ;");
            }            
        }                
        
        completeEntry();        
    }    
}
