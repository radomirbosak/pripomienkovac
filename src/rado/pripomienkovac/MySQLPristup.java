package rado.pripomienkovac;

import java.sql.*;

public class MySQLPristup {
    private Connection livingConnection;
    private final String dbUrl;// = "jdbc:mysql://localhost/pripomienkovac";
    private final String dbUser;// = "priposvist";
    private final String dbPass;// = "restorehonor";
    
    
    
    public MySQLPristup(String hostname, String dbname, String username, String password) {
        this.dbUrl = "jdbc:mysql://" + hostname + "/" + dbname;
        //this.dbUrl = "jdbc:sqlite:sample.db";
        this.dbUser = username;
        this.dbPass = password;
    }
    
    public Connection getConnection() throws SietkaMySQLException {
        if (livingConnection==null) {
            try {
                livingConnection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                return livingConnection;
            } catch (SQLException ex) {
                System.out.println(ex.getErrorCode());
                throw new SietkaMySQLException();
            }
        }
        return livingConnection;
    }
    public void closeConnection() throws SietkaMySQLException {
        if (livingConnection!=null) {
            try {
                livingConnection.close();
                livingConnection=null;
            } catch (SQLException ex) {
                throw new SietkaMySQLException();
            }
        }
    }
    public boolean existsTable(Connection con, String tablename) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE '"+tablename+"';");
        return rs.next();
    }
    
    private static final String _validTableChars = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String _validTableNumerals = "0123456789";
    public static boolean IsValidTableName(String name) {
        if (name.length()==0) {return false;}
        if (_validTableNumerals.indexOf(name.charAt(0))>-1) {return false;}
        
        int c;
        for (int i = 0; i < name.length(); i++) {
            c = name.charAt(i);
            if (_validTableChars.indexOf(c)==-1) {return false;}
        }
        return true;
    }
    
    
}

class SietkaException extends Exception {

    public SietkaException() {
        super();
    }
    public SietkaException(String message) {
        super(message);
    }
}
class SietkaMySQLException extends SietkaException {}
class SietkaMySQLNotinitializedException extends SietkaMySQLException {}
class SietkaMySQLInvalidtablenameException extends SietkaMySQLException {}
class SietkaKeynotfoundException extends SietkaException {}
class SietkaDuplicatekeyException extends SietkaException {}
class SietkaCorruptedDataException extends SietkaException {
    public SietkaCorruptedDataException() {
        super();
    }
    public SietkaCorruptedDataException(String message) {
        super(message);
    }
}