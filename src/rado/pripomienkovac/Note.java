package rado.pripomienkovac;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ja
 */
public class Note {
    public final int id;
    public ITrigger trigger;
    public IContent content;
    
    public static final String _tablename = "notes";
    private static final boolean _encrypted = true;
    private static String _password;
    
    public static Map<Integer,Note> notes;
    
    private static PreparedStatement dbSelectAll;
    private static PreparedStatement dbAddNote;
    private static PreparedStatement dbDeleteNote;
    private static PreparedStatement dbChange;
    
    private static MySQLPristup _pristup;
    
    private static int _allNoteCount;
    private static int _badNoteCount;
    
    public Note(int id, ITrigger trigger, IContent content) {
        this.id = id;
        this.trigger = trigger;
        this.content = content;
    }
    
    public static void init(MySQLPristup pristup, String dbPassword) throws SietkaException, SQLException, ExceptionInvalidData {
        _pristup = pristup;
        _password = dbPassword;
        Connection con = _pristup.getConnection();
        notes = new HashMap<>();
        
        if (_encrypted) {
            dbSelectAll = con.prepareStatement("SELECT id, "+
                    "AES_DECRYPT(triggers, '" + _password + "') AS 'triggers'," +
                    "AES_DECRYPT(content, '" + _password + "') AS 'content'"+
                    " FROM " + _tablename);
            dbAddNote = con.prepareStatement("INSERT INTO "+_tablename+" (triggers, content) VALUES ("+
                    "AES_ENCRYPT(?, '" + _password + "')," +
                    "AES_ENCRYPT(?, '" + _password + "')" +
                    ")",Statement.RETURN_GENERATED_KEYS);
            dbDeleteNote = con.prepareStatement("DELETE FROM "+_tablename+" WHERE id=?");
            dbChange = con.prepareStatement("UPDATE "+_tablename+" SET " +
                    "triggers=AES_ENCRYPT(?, '" + _password + "'), " +
                    "content=AES_ENCRYPT(?, '" + _password + "') WHERE id=?");
        } else {
            dbSelectAll = con.prepareStatement("SELECT * FROM "+_tablename);
            dbAddNote = con.prepareStatement("INSERT INTO "+_tablename+" (triggers, content) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            dbDeleteNote = con.prepareStatement("DELETE FROM "+_tablename+" WHERE id=?");
            dbChange = con.prepareStatement("UPDATE "+_tablename+" SET triggers=?, content=? WHERE id=?");
        }
        
        
        notes = loadFromDB();
    }
    
    private static HashMap<Integer,Note> loadFromDB() throws SietkaException, SQLException, ExceptionInvalidData {
        HashMap<Integer,Note> ls = new HashMap<>();
        ResultSet rs;
        
        rs = dbSelectAll.executeQuery();

        Blob triggerBlob=null, contentBlob=null;
        
        ITrigger trigger = null;
        IContent content = null;
        
        Note._allNoteCount = 0;
        Note._badNoteCount = 0;
        
        
        //@FIXME optimalizovat nazvy stlpcov, ale je to na ukor citatelnosti potom
        while (rs.next()) {
            Note._allNoteCount++;
            
            triggerBlob = rs.getBlob("triggers");
            contentBlob = rs.getBlob("content");
            if (triggerBlob==null || contentBlob==null) {
                Note._badNoteCount++;
                continue;
                
            }
            
            try {
                trigger = (ITrigger)GeneralIByteArrayableGenerator.fromByteArray(
                    triggerBlob.getBytes(1, (int)triggerBlob.length()) ) ;
                trigger = UpdateManager.updateTrigger(trigger);

                content = (IContent)GeneralIByteArrayableGenerator.fromByteArray(
                        contentBlob.getBytes(1, (int)contentBlob.length()));
                content = UpdateManager.updateContent(content);
            } catch (ExceptionInvalidData ex) {
                System.out.println(ex.getMessage());
                Note._badNoteCount++;
                continue;
            }
            
            
            ls.put(rs.getInt("id"), new Note(
                    rs.getInt("id"),
                    trigger,
                    content)
                    );
            
        }
        if (triggerBlob!=null) {triggerBlob.free();}
        if (contentBlob!=null) {contentBlob.free();}
        
        if (Note._badNoteCount > 0) {
            throw new ExceptionIncorrectPassword(
                    Note._badNoteCount + " of " + Note._allNoteCount + " notes corrupted or bad password");
        }
        
        if (UpdateManager.getUpdatedTriggerCount() > 0 || UpdateManager.getUpdatedContentCount() > 0) {
            System.out.println(
                    UpdateManager.getUpdatedTriggerCount() + " triggers and " +
                    UpdateManager.getUpdatedContentCount() + " contents updated");
        }
        
        return ls;
    }
    
    public static void add(ITrigger trigger, IContent content) throws SietkaMySQLException, SQLException {
        Connection con = _pristup.getConnection();
        
        Blob triggerBlob = con.createBlob();
        triggerBlob.setBytes(1, trigger.toByteArray());
        
        Blob contentBlob = con.createBlob();
        contentBlob.setBytes(1, content.toByteArray());
        
        dbAddNote.setBlob(1, triggerBlob);
        dbAddNote.setBlob(2, contentBlob);
        
        dbAddNote.executeUpdate();
        ResultSet rs = dbAddNote.getGeneratedKeys();
        if (rs.next()) {
            notes.put(rs.getInt(1), new Note(rs.getInt(1), trigger, content));
            System.out.println("Added note no #"+String.valueOf(rs.getInt(1)));
        } else {
            throw new SietkaMySQLException();
        }
        
        triggerBlob.free();
        contentBlob.free();
    }
    
    public static void remove(int id) throws SietkaException, SQLException {
        if (!notes.containsKey(id)) 
            { throw new ArrayIndexOutOfBoundsException("Note.remove(int id):\n index " + id + " not present in HashMap notes"); }
        
        dbDeleteNote.setInt(1, id);
        dbDeleteNote.executeUpdate();
        notes.remove(id);
    }
    
    public static void edit(int id, ITrigger newtrigger, IContent newcontent) throws SietkaException, SQLException {
        if (!notes.containsKey(id)) {
            throw new ArrayIndexOutOfBoundsException("Note.edit(int id, ITrigger newtrigger, IContent newcontent):\n" +
                    " index " + id + " not present in HashMap notes"); 
        }
        
        Connection con = _pristup.getConnection();
        
        Blob triggerBlob = con.createBlob();
        triggerBlob.setBytes(1, newtrigger.toByteArray());
        
        Blob contentBlob = con.createBlob();
        contentBlob.setBytes(1, newcontent.toByteArray());
        
        dbChange.setBlob(1, triggerBlob);
        dbChange.setBlob(2, contentBlob);
        dbChange.setInt(3, id);
        
        dbChange.executeUpdate();
        
        triggerBlob.free();
        contentBlob.free();
        
        notes.remove(id);
        notes.put(id, new Note(id, newtrigger, newcontent));
    }
    
    @Override
    public String toString() {
        if (content instanceof ContentSimpleString) {
            return ((ContentSimpleString)content).getString();
        }
        return "";
    }
}
