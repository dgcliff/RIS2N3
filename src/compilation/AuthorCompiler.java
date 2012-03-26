/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilation;

import entity.Author;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import templates.UniqueURIGenerator;

/**
 *
 * @author dgcliff
 */
public class AuthorCompiler
{
    UniqueURIGenerator uniqueURIGen;
    private ArrayList<Author> uniqueAuthors;
    
    public AuthorCompiler(UniqueURIGenerator uUg)
    {
        uniqueURIGen = uUg;
        uniqueAuthors = new ArrayList<>();
    }
    
    public void addPublication(String title, ArrayList<String> authorList)
    {
        boolean uniqueAuthor = true;
        
        for(String authorName : authorList)
        {
            Author au = new Author(authorName, uniqueURIGen.generateNewURI());
            
            //for loop - if au is the same person (programmatically defined) as
            //an existing author do not add to list, but add the publication
            //else add both
            
            for(Author listedAuthor: uniqueAuthors)
            {
                if(listedAuthor.isTheSamePersonAs(au))
                {
                    uniqueAuthor = false;
                    au = listedAuthor;
                    break;
                }
            }
            
            if(uniqueAuthor)
            {
                uniqueAuthors.add(au);
            }
            
            au.addRelatedItem(title);            
        }
    }
    
    public void printAuthorListToFile(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for(Author au : uniqueAuthors)
            {
                out.println(au.getFullName() + " " +  au.getURI());
                
                //for loop for relatedItems
                ArrayList<String> relatedItems = au.getRelatedItems();
                
                for(String item : relatedItems)
                {
                    out.println("\t" + item);
                }
                
                out.println();
            }
        }        
        catch(IOException e)
        {
            System.out.println(e);
        }
    }    
}
