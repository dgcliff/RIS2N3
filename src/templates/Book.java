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
public class Book extends BaseTemplate
{
    public Book(UniqueURIGenerator uniqueNumberGen, ArrayList<String> newEntry, AuthorCompiler aC)
    {
        super(uniqueNumberGen, aC);
    }    
}
