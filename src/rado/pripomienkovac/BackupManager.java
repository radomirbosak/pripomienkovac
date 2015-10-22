package rado.pripomienkovac;

import java.io.IOException;

public class BackupManager {
    private final String dbName, dbUserName, dbPassword;
    public BackupManager(String dbName, String dbUserName, String dbPassword) {
        this.dbName = dbName;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
    }
    
    public boolean backup(String filename) throws IOException, InterruptedException {
        return backupDB(this.dbName, this.dbUserName, this.dbPassword, filename);
    }
    public boolean restore(String filename) throws IOException, InterruptedException {
        return restoreDB(this.dbName, this.dbUserName, this.dbPassword, filename);
    }
    
    private boolean backupDB(String dbName, String dbUserName, String dbPassword, String path) throws IOException, InterruptedException {
        // @FIXME osetrit nenajdeny command mysqldump
        //String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r " + path;
        String tables =  "notes dbinfo"; // if case of more tables, separate them by space
        String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbPassword + " " + dbName + " --tables " + tables + " -r " + path;
        Process runtimeProcess;

            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("Backup created successfully");
                return true;
            } else {
                System.out.println("Could not create the backup");
            }


        return false;
    }
    private boolean restoreDB(String dbName, String dbUserName, String dbPassword, String source) throws IOException, InterruptedException {

        //String[] executeCmd = new String[]{"mysql", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source "+source};
        //String[] executeCmd = new String[]{"mysql", "--user=" + dbUserName, "--password=" + dbPassword, "-D " + dbName, "-e", "source "+source};
        
        String executeCmd = "mysql -u " + dbUserName + " -p" + dbPassword + " -D " + dbName + " -e \"source " + source + "\"";
        Process runtimeProcess;

            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                return true;
            } else {
                System.out.println("Could not restore the backup");
            }


        return false;
    }
}
