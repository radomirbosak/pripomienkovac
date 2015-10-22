/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

import java.io.IOException;
import java.sql.Connection;

/**
 *
 * @author ja
 */
public class BackupRun {
    public static void main(String[] args) {
        BackupManager bm = new BackupManager("pripomienkovac", "priposvist", "restorehonor");
        
        boolean bBackup = false;
        try {
            if (bBackup) {
                bm.backup("vysledok.sql");
            } else {
                bm.restore("vysledok.sql");
            }
        } catch (IOException| InterruptedException ex) {
            ex.printStackTrace();
            //@FIXME to sa nepatri hadzat debug uzivatelu
        }
    }
}
