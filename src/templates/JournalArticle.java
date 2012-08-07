/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.JournalCompiler;
import entity.Author;
import entity.Publication;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class JournalArticle extends BaseTemplate
{
    public JournalArticle(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, JournalCompiler jC, Publication pub)
    {
        super(uniqueNumberGen, pub.getURI());
        
        addType("<http://purl.org/ontology/bibo/AcademicArticle>");
        addType("<http://vivoweb.org/ontology/core#InformationResource>");
        
        addTitle(pub.getTitle());
        
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
            else if (line.startsWith("JF"))
            {
                String title = (line.substring(line.indexOf("-") + 1, line.length())).trim();
                String journalURI = jC.addJournal(title, this.getURI());                                                
                addN3("\t<http://vivoweb.org/ontology/core#hasPublicationVenue> " + journalURI + " ;");
            }
            else if (line.startsWith("UR"))
            {
                addURL(line);
            }            
        }                
        
        completeEntry();
    }
}
