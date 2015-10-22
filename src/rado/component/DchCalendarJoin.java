/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

import javax.swing.SwingUtilities;
import rado.pripomienkovac.DataDate;

/**
 *
 * @author ja
 */
public class DchCalendarJoin implements DateChangeHandler {

    private final JKalendarDen jkd;
    private final JKalendarMesiac jkm;
    public DchCalendarJoin(JKalendarDen jkd, JKalendarMesiac jkm) {
        this.jkd = jkd;
        this.jkm = jkm;
    }
    
    @Override
    public void fire(final DataDate newDate) {
        System.out.println("tu som1");
        jkm.setFormerDate(newDate);
        System.out.println("tu som1.5");
        jkd.setFormerDate(newDate);
        System.out.println("tu som2");
    }
    
}
