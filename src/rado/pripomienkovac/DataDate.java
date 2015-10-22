package rado.pripomienkovac;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ja
 */
public class DataDate implements IByteArrayable {
    public final int day, month, year;
    
    public DataDate(int year, int month, int day) throws ExceptionMyDateInvalid {
        if (isDateValid(year, month, day)) {
            this.year = year;
            this.month = month;
            this.day = day;
        } else {
            throw new ExceptionMyDateInvalid();
        }
    }
    
    private static boolean isDateValid(int year, int month, int day) {
        if (day < 1) {return false;}
        
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {return day <= 31;}
        if (month == 4 || month == 6 || month == 9 || month == 11) {return day <= 30;}
        
        if (month == 2 && day < 30) {
            //A leap year is any year that is divisible by 4 but not divisible by 100 unless it is also divisible by 400
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        
        return false; // iny month
    }
    
    public static boolean isLeapYear(int year) {
        return (year % 400 == 0) || ((year % 4 == 0) && (year %100 !=0));
    }
    
    public static int getMonthLastDay(int month, int year) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) return 31;
        if (month == 4 || month == 6 || month == 9 || month == 11) return 30;
        if (month == 2) {
            //A leap year is any year that is divisible by 4 but not divisible by 100 unless it is also divisible by 400
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        throw new IllegalArgumentException("month allowed only 1-12");
    }
    
    public static int getDayDifference(DataDate d1, DataDate d2) {
        int factor;
        
        int minDay, minMonth, minYear;
        int maxDay, maxMonth, maxYear;
        
        if (d2.less(d1)) {
            minDay = d1.day;
            minMonth = d1.month;
            minYear = d1.year;
            
            maxDay = d2.day;
            maxMonth = d2.month;
            maxYear = d2.year;
            
            factor = -1;
        } else {
            minDay = d2.day;
            minMonth = d2.month;
            minYear = d2.year;
            
            maxDay = d1.day;
            maxMonth = d1.month;
            maxYear = d1.year;
            
            factor = 1;
        }
        
        
        
        // assert d1 < d2
        int tempDaysSum = 0; 
        while (minMonth != maxMonth || minYear != maxYear) {
            tempDaysSum += DataDate.getMonthLastDay(minMonth, minYear);
            if (++minMonth > 12) {
                minMonth = 1; minYear++;
            }
        }
        return factor * (tempDaysSum + maxDay - minDay);
    }
    
    public static DataDate getToday() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        try {
            return new DataDate(year, month, day);
        } catch (ExceptionMyDateInvalid ex) {
            throw new RuntimeException("Today is not a valid date?"); // nemalo by sa stat
        }
    }
    
    public boolean equals(DataDate datum) {
        return this.day==datum.day && this.month==datum.month && this.year==datum.year;
    }
    
    public boolean less(DataDate datum) {
        return (this.year < datum.year ||
                (this.year == datum.year && this.month < datum.month) ||
                (this.year == datum.year && this.month == datum.month && this.day < datum.day));
    }
    
    public static final int byteSize = 3 * 4; // 3 bytes
    
    @Override
    public byte[] toByteArray() {
        return ArrayHelper.concat(Conversions.intToByteArray(this.year),
                Conversions.intToByteArray(this.month),
                Conversions.intToByteArray(this.day));
    }
    
    public static DataDate fromByteArray(byte[] arr) throws ExceptionMyDateInvalid {
        byte[] year = new byte[4];
        byte[] month = new byte[4];
        byte[] day = new byte[4];
        System.arraycopy(arr, 0, year, 0, 4);
        System.arraycopy(arr, 4, month, 0, 4);
        System.arraycopy(arr, 8, day, 0, 4);
        
        return new DataDate(
                Conversions.byteArrayToInt(year),
                Conversions.byteArrayToInt(month),
                Conversions.byteArrayToInt(day)
                );
    }
}
