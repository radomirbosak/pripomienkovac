package rado.pripomienkovac;

import java.io.*;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ja
 */
public class RunClass {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        //Class.forName("org.sqlite.JDBC");
        
        MySQLPristup pristup = new MySQLPristup(
                "localhost",        // host
                "***",   // dbname
                "***",       // user
                "***");    // p assword
        
        
        SQLNoteHelper snh = new SQLNoteHelper(pristup);
        
        
        try {
            // 1. try to connect to db
            snh.tryConnect();
            
            // 2. prepare the db (create tables, ... if necessary)
            snh.prepareDB();
        } catch (ExceptionSQLInitFailed ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Pripomienkovač - error", JOptionPane.ERROR_MESSAGE);
            ObecneFunkcie.chybakonci(ex.getMessage());
        }
        
        // @FIXME toto pre bezpecnost potom opravit
        String mypass = JOptionPane.showInputDialog("enter database password", "password");
        if (mypass == null) {
            JOptionPane.showMessageDialog(null, "Password not provided\nAccess denied", "Pripomienkovač", JOptionPane.ERROR_MESSAGE);
            ObecneFunkcie.chybakonci("Password not provided\nAccess denied");
        }
        
        
        // 3. init structures
        GeneralIByteArrayableGenerator.initGenerators();
        try {
            Note.init(pristup, mypass);
        } catch (SietkaException|SQLException ex) {
            ObecneFunkcie.chybakonci("DB Init error");
        } catch (ExceptionIncorrectPassword ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Pripomienkovač", JOptionPane.ERROR_MESSAGE);
            ObecneFunkcie.chybakonci(ex.getMessage());
        } catch (ExceptionInvalidData ex) {
            ObecneFunkcie.chybakonci("DB data corrupted");
        }
        
        System.out.println("Running GUI");
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainUI().setVisible(true);
            }
        });
        
    }
    
    
    
}
