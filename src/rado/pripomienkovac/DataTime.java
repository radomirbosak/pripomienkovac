package rado.pripomienkovac;

import java.util.Calendar;

/**
 *
 * @author ja
 */
public class DataTime implements IByteArrayable {
    public int hour, minute, second, milisecond;
    
    public DataTime(int hour, int minute, int second, int milisecond) throws ExceptionMyTimeInvalid {
        if (isTimeValid(hour, minute, second, milisecond)) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.milisecond = milisecond;
        } else {
            throw new ExceptionMyTimeInvalid();
        }
    }
    
    public DataTime(int hour, int minute, int second) throws ExceptionMyTimeInvalid { 
        this(hour, minute, second, 0);
    }
    
    private static boolean isTimeValid(int hour, int minute, int second, int milisecond) {
        return (hour >= 0 && hour < 24) &&
                (minute >= 0 && minute < 60) &&
                (second >= 0 && second < 60) &&
                (milisecond >=0 && milisecond < 1000);
    }
    
    public static DataTime getNow() {
        Calendar now = Calendar.getInstance();
        
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int milisecond = now.get(Calendar.MILLISECOND);
        
        try {
            return new DataTime(hour, minute, second, milisecond);
        } catch (ExceptionMyTimeInvalid ex) {
            throw new RuntimeException("Now is not a valid time?"); // nemalo by sa stat
        }
        
    }
    
    public boolean equals(DataTime cas) {
        return this.hour == cas.hour && this.minute == cas.minute && this.second == cas.second && this.milisecond == cas.milisecond;
    }
    
    public static final int byteSize = 4 * 4; // 4 bytes
    
    @Override
    public byte[] toByteArray() {
        return ArrayHelper.concat(Conversions.intToByteArray(this.hour),
                Conversions.intToByteArray(this.minute),
                Conversions.intToByteArray(this.second),
                Conversions.intToByteArray(this.milisecond));
    }
    
    public static DataTime fromByteArray(byte[] arr) throws ExceptionMyTimeInvalid {
        byte[] hour = new byte[4];
        byte[] minute = new byte[4];
        byte[] second = new byte[4];
        byte[] milisecond = new byte[4];
        
        try {
            System.arraycopy(arr, 0, hour, 0, 4);
            System.arraycopy(arr, 4, minute, 0, 4);
            System.arraycopy(arr, 8, second, 0, 4);
            System.arraycopy(arr, 12, milisecond, 0, 4);
        } catch (Exception ex) {
            throw new ExceptionMyTimeInvalid();
        }
        
        return new DataTime(
                Conversions.byteArrayToInt(hour),
                Conversions.byteArrayToInt(minute),
                Conversions.byteArrayToInt(second),
                Conversions.byteArrayToInt(milisecond)
                );
    }
}
