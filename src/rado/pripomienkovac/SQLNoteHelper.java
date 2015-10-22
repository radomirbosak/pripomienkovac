/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author ja
 */
public class SQLNoteHelper {
    private MySQLPristup pristup;
    
    public SQLNoteHelper(MySQLPristup pristup) {
        this.pristup = pristup;
        
    }
    
    public void tryConnect() throws ExceptionSQLInitFailed {
        if (!this.canConnect()) {
            // run wampmysqld service if not running
            try {
                if (!isServiceRunning("wampmysqld")) {
                    if (!runService("wampmysqld")) {
                        throw new ExceptionSQLInitFailed("Failed to start the wampmysqld service\n\n"
                                + "Try running\n\"sc start wampmysqld\"\nwith administrator privileges"); }
                }
            } catch (IOException ex) {
                throw new ExceptionSQLInitFailed("Unknown error occured, when checking wampmysqld service");
            }
            
            if (!this.canConnect()) {
                // ak som aj pustil service ale stale sa neda connectnut, tak
                // vypytam si root user, pass
                rootCreateDBandUser();
            }
        }
    }
    
    public void prepareDB() throws ExceptionSQLInitFailed {
        // we assume, that database and user already exist
        // because root must have created them
        // and we don't know his password
        
        // table exist? if not, create
        try {
            Connection con = this.pristup.getConnection();
            if (!pristup.existsTable(con, "notes")) {
                Statement st = con.createStatement();
                st.execute("CREATE TABLE `notes` (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `triggers` blob NOT NULL,\n" +
                        "  `content` blob NOT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci; ");
            }
        } catch (SietkaMySQLException| SQLException ex) {
            throw new ExceptionSQLInitFailed("create table failed");
        }
        
    }
    
    private boolean canConnect() {
        try {
            pristup.getConnection();
        } catch (SietkaMySQLException ex) {
            return false;
        }
        return true;
        
    }
    
    private static boolean isServiceRunning(String serviceName) throws IOException {
        // query for existing mysql service
        // @FIXME nemam osetrene ak funkcia neexistuje
        String[] script = {"cmd.exe", "/c", "sc", "query", serviceName, "|", "find", "/C", "\"RUNNING\""};
        
        Process queryexprocess = Runtime.getRuntime().exec(script);
        BufferedReader br = new BufferedReader(new InputStreamReader(queryexprocess.getInputStream()));
        String line;

        while ((line = br.readLine()) != null) {
            switch (line) {
                case "1":
                    br.close();
                    return true;
                case "0":
                    br.close();
                    return false;
                default:
                    br.close();
                    throw new IOException("RunClass.isServiceRunning:\n Unexpected result");
            }
        }
        br.close();
        throw new IOException("RunClass.isServiceRunning:\n Unexpected result");
        // end query
        
    }
    
    private static boolean runService(String serviceName) throws IOException {
        String[] script = {"cmd.exe", "/c", "sc", "start", serviceName, "|", "find", "/C", "\"START_PENDING\""};
        
        Process queryexprocess = Runtime.getRuntime().exec(script);
        BufferedReader br = new BufferedReader(new InputStreamReader(queryexprocess.getInputStream()));
        String line;

        while ((line = br.readLine()) != null) {
            switch (line) {
                case "1":
                    br.close();
                    return true;
                case "0":
                    br.close();
                    return false;
                default:
                    br.close();
                    throw new IOException("RunClass.isServiceRunning:\n Unexpected result");
            }
        }
        
        br.close();
        throw new IOException("RunClass.isServiceRunning:\n Unexpected result");
        
    }
    
    private static void rootCreateDBandUser() throws ExceptionSQLInitFailed {
        final String dbName = "pripomienkovac";
        final String userName = "priposvist";
        final String userPass = "restorehonor";
        final String hostname = "localhost";
        int res = JOptionPane.showConfirmDialog(null, "MySQL service is running,\n"
                                + " but access to the pripomienkovac database\n"
                                + " and user is denied, or they do not exist.\n "
                                + "Do you want to create the user and database if they don't exist?","SQL root access", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.NO_OPTION) {
            throw new ExceptionSQLInitFailed("database/user not accessible or does not exist, add them manually");
        }
        String user = JOptionPane.showInputDialog("Please enter your mysql root username");
        String pass = JOptionPane.showInputDialog("Please enter your mysql root password");

        MySQLPristup rootprist = new MySQLPristup("localhost", "", user, pass);
        
        try {
            Connection con = rootprist.getConnection();

            // check database
            Statement st = con.createStatement();
            st.execute("CREATE DATABASE IF NOT EXISTS " + dbName + ";");
            
            // create user and his permissions
            st.execute("GRANT ALL PRIVILEGES ON " + dbName + ".* To '" + userName + "'@'" + hostname + "' IDENTIFIED BY '" + userPass + "';");
            
            rootprist.closeConnection();
        } catch (SQLException| SietkaMySQLException ex) {
            throw new ExceptionSQLInitFailed("Failed creating database and/or user");
        }
    }
}
