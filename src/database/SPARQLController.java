/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dgcliff
 */
public class SPARQLController
{
    Model mainModel;
    Store store;
    
    public SPARQLController(Store s)
    {        
        store = s;
        mainModel = SDBFactory.connectNamedModel(store, "http://vitro.mannlib.cornell.edu/default/vitro-kb-2");        
    }
    
    /*public String checkForURI(String queryString)
    {        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, mainModel);
        
        ResultSet results = null;
        
        try
        {
            results = qe.execSelect(); 
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        String lineResult = null;
        
        while (results.hasNext())
        {            
            QuerySolution qs = results.next();
            Iterator<?> varNames = qs.varNames();
            while (varNames.hasNext())
            {
                String varName = (String) varNames.next();

                try
                {
                    lineResult += qs.getLiteral(varName);
                }
                catch(Exception e)
                {
                    lineResult += qs.getResource(varName);
                }
            }
        }
        
        return lineResult;
    }*/
    
    public Map<String, String> queryVIVO(String queryString, String URIsub, String valSub)
    {
        HashMap<String, String> titles = new HashMap<>();
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, mainModel);
        
        ResultSet results = null;
        
        try
        {
            results = qe.execSelect(); 
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        while (results.hasNext())
        {            
            QuerySolution solution = results.next();
            //titles.add((solution.getLiteral(subject)).getString());
            titles.put((solution.getLiteral(URIsub)).getString(), (solution.getLiteral(valSub)).getString());
        }
        
        return titles;
    }
}
