/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import compilation.AuthorCompiler;
import compilation.JournalCompiler;
import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class JournalArticle extends BaseTemplate
{
    public JournalArticle(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC, JournalCompiler jC)
    {
        super(uniqueNumberGen, aC);
        
        addType("<http://purl.org/ontology/bibo/AcademicArticle>");
        
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
                String journalURI = jC.addJournal(title, this.getURI());                                                
                addN3("\t<http://vivoweb.org/ontology/core#hasPublicationVenue> " + journalURI + " ;");
            }
        }                
        
        completeEntry();
    }
}
