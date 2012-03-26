/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.util.ArrayList;

/**
 *
 * @author dgcliff
 */
public class JournalArticle extends BaseTemplate
{
    public JournalArticle(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry)
    {
        super(uniqueNumberGen);
        
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
        }
        
        completeEntry();
    }
}
