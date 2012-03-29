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
    }    
}
