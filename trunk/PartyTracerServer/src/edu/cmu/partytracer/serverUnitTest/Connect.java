package edu.cmu.partytracer.serverUnitTest;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect
{
    public static void main (String[] args)
    {
        Connection conn = null;

        try
        {
            String userName = "root";
            String password = "admin";
            String url = "jdbc:mysql:///partytracer";
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url);
            System.out.println ("Database connection established");
        }
        catch (Exception e)
        {
            System.err.println ("Cannot connect to database server: "+e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
}
