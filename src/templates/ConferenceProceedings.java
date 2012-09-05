/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import compilation.ConferenceCompiler;
import entity.Author;
import entity.Publication;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class ConferenceProceedings extends BaseTemplate
{
    public ConferenceProceedings(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC, ConferenceCompiler cC, Publication pub)
    {
        super(uniqueNumberGen, pub.getURI());

        if(!pub.getExistsInVIVO())
        {
            addType("<http://vivoweb.org/ontology/core#ConferencePaper>");
            addType("<http://purl.org/ontology/bibo/Article>");                    
            addType("<http://purl.org/ontology/bibo/Document>");
            addType("<http://vivoweb.org/ontology/core#InformationResource>");

            addTitle(pub.getTitle());
        }
        
        for(Author a : pub.getAuthors())
        {
            addAuthor(a);
        }        
        
        for(String line : newEntry)
        {
            if (line.startsWith("KW"))
            {
                addKeywords(line);
            }
            else if (line.startsWith("Y1") || line.startsWith("PY"))
            {
                addDate(line);
            }                 
            else if (line.startsWith("JF") || line.startsWith("JO") || line.startsWith("T2"))
            {
                if(!pub.getExistsInVIVO())
                {                
                    String title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
                    String conferenceURI = cC.addConference(title, this.getURI());                                                
                    addN3("\t<http://purl.org/ontology/bibo/presentedAt> " + conferenceURI + " ;");
                }
            }            
        }                
        
        completeEntry();        
    }    
}
