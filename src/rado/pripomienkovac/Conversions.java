/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class Conversions {
    public static int byteArrayToInt(byte[] b) 
    {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a)
    {
        return new byte[] {
            (byte) ((a >> 24) & 0xFF),
            (byte) ((a >> 16) & 0xFF),   
            (byte) ((a >> 8) & 0xFF),   
            (byte) (a & 0xFF)
        };
    }
    
    private static final String[] mnames = {"Január", "Február", "Marec", "Apríl", "Máj", "Jún",
        "Júl", "August", "September", "Október", "November", "December"};
    private static final String[] mabr = {"Jan", "Feb", "Mar", "Apr", "Máj", "Jún",
        "Júl", "Aug", "Sep", "Okt", "Nov", "Dec"};
    public static String monthToString(int month) {
        if (month<1 || month>12) throw new IllegalArgumentException();
        return mnames[month-1];
    }
    public static String monthToAbr(int month) {
        if (month<1 || month>12) throw new IllegalArgumentException();
        return mabr[month-1];
    }
}
