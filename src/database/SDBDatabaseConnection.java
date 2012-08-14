/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import java.sql.Connection;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author sandy
 */
public class SDBDatabaseConnection
{
    public ModelMaker maker;
    public SDBConnection conn;
    public Store store;

    public SDBDatabaseConnection(String userName, String password, String dbString)
    {
        String className = "com.mysql.jdbc.Driver"; // path of driver class

        try
        {
            StoreDesc storeDesc = new StoreDesc(LayoutType.fetch("layout2/hash"),DatabaseType.fetch("MySQL")) ;

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(className);
            ds.setUrl(dbString);
            ds.setUsername(userName);
            ds.setPassword(password);

            conn = new SDBConnection(ds.getConnection());

            store = SDBFactory.connectStore(conn, storeDesc);
            if(store == null)
            {
                System.out.println("Store is null...");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public Connection getConnection()
    {
        return conn.getSqlConnection();
    }

    public Store getStore()
    {
        return store;
    }

    public void closeDatabaseConnection()
    {
        try
        {
            store.close();
            conn.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

}