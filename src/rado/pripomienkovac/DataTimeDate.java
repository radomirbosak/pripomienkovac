/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class DataTimeDate implements IByteArrayable {
    public final DataTime cas;
    public final DataDate datum;
    
    public DataTimeDate(DataDate datum, DataTime cas) {
        this.cas = cas;
        this.datum = datum;
    }
    
    public static DataTimeDate getNow() {
        return new DataTimeDate(DataDate.getToday(), DataTime.getNow());
    }
    
    public DataTimeDate translate(int dYear, int dMonth, int dDay, int dHour, int dMinute, int dSecond, int dMilisecond) {
        int year, month, day, hour, minute, second, milisecond;
        year = this.datum.year;
        month = this.datum.month;
        day = this.datum.day;
        
        hour = this.cas.hour;
        minute = this.cas.minute;
        second = this.cas.second;
        milisecond = this.cas.milisecond;
        
        milisecond += dMilisecond;
        while (milisecond < 0) {milisecond += 1000; dSecond -= 1;}
        while (milisecond > 999) {milisecond -= 1000; dSecond += 1;}
        
        second += dSecond;
        while (second < 0) {second += 60; dMinute -= 1;}
        while (second > 59) {second -= 60; dMinute += 1;}
        
        minute += dMinute;
        while (minute < 0) {minute += 60; dHour -= 1;}
        while (minute > 59) {minute -= 60; dHour += 1;}
        
        hour += dHour;
        while (hour < 0) {hour += 24; dDay -= 1;}
        while (hour > 23) {hour -= 24; dDay += 1;}
        
        
        dDay += 30 * dMonth + 365 * dYear;
        int tempMonth;
        while (day + dDay < 1) {
            tempMonth = month - 1; if (tempMonth == 0) {tempMonth = 12;}
            dDay += DataDate.getMonthLastDay(tempMonth, year);
            month--; if (month == 0) { month = 12; year--; }
        }
        
        while (day + dDay > DataDate.getMonthLastDay(month, year)) {
            dDay -= DataDate.getMonthLastDay(month, year);
            month++; if (month == 13) { month = 1; year++;}
        }
        
//        
//        month += dMonth;
//        while (month < 1) {month += 12; dYear -= 1;}
//        while (month > 12) {month -= 12; dYear += 1;}
//        
//        year += dYear;
        try {
            return new DataTimeDate(
                    new DataDate(year, month, day),
                    new DataTime(hour, minute, second, milisecond)
                    );
        } catch (ExceptionMyDateInvalid| ExceptionMyTimeInvalid ex) {
            throw new RuntimeException("DataTimeDate.translate error");
        }
    }

    public static final int byteSize = 7 * 4; // 3 bytes datum, 4 bytes cas
    
    @Override
    public byte[] toByteArray() {
        return ArrayHelper.concat(
                datum.toByteArray(),
                cas.toByteArray()
                );
    }
}
