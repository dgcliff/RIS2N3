package main;

import compilation.AuthorCompiler;
import database.SDBDatabaseConnection;
import database.SPARQLController;
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
        //TODO: need to read in these values via the arguments
        SDBDatabaseConnection sdbC = new SDBDatabaseConnection("vivouser", "vitro123", "jdbc:mysql://localhost/vivo");
        SPARQLController sparqlC = new SPARQLController(sdbC.getStore());
        
        //sparqlC.checkForURI("PREFIX core: <http://vivoweb.org/ontology/core#> SELECT ?publication WHERE { ?publication a core:InformationResource ; core:title \"Managing reservoir sediment release in dam removal projects: An approach informed by physical and numerical modelling of non‐cohesive sediment\" . }");
        
        File dir = new File(args[0]);
        
        RISExtractor risEx = new RISExtractor(dir, sparqlC);
                
        risEx.extractAuthorNames();        
        
        risEx.extractToN3(args[1]);
    }
}
