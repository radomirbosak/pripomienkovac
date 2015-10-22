/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class ArrayHelper {
    public static byte[] concat(byte[] ar1, byte[] ar2) {
        byte[] vysledok = new byte[ar1.length + ar2.length];
        System.arraycopy(ar1, 0, vysledok, 0, ar1.length);
        System.arraycopy(ar2, 0, vysledok, ar1.length, ar2.length);
        return vysledok;
    }
    public static byte[] concat(byte[] ar1, byte[] ar2, byte[] ar3) {
        byte[] vysledok = new byte[ar1.length + ar2.length + ar3.length];
        System.arraycopy(ar1, 0, vysledok, 0, ar1.length);
        System.arraycopy(ar2, 0, vysledok, ar1.length, ar2.length);
        System.arraycopy(ar3, 0, vysledok, ar1.length + ar2.length, ar3.length);
        return vysledok;
    }
    public static byte[] concat(byte[]... arr) {
        int celkolen = 0;
        int i;
        for (i = 0; i < arr.length; i++) {
            celkolen += arr[i].length;
        }
        
        byte[] vysledok = new byte[celkolen];
        celkolen = 0;
        for (i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, vysledok, celkolen, arr[i].length);
            celkolen += arr[i].length;
        }
        
        return vysledok;
        
    }
    public static byte[] sub(byte[] arr, int start, int length) {
        byte[] vysledok = new byte[length];
        System.arraycopy(arr, start, vysledok, 0, length);
        return vysledok;
    }
}
