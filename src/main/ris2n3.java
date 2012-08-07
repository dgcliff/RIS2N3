package main;

import compilation.AuthorCompiler;
import extraction.RISExtractor;
import java.io.File;
import templates.UniqueURIGenerator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dgcliff
 */
public class ris2n3
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        File dir = new File(args[0]);
        
        RISExtractor risEx = new RISExtractor(dir);
                
        risEx.extractAuthorNames();        
        
        risEx.extractToN3(args[1]);
    }
}
