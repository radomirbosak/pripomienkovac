/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

import java.awt.FlowLayout;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import rado.pripomienkovac.ContentSimpleString;
import rado.pripomienkovac.ExceptionMyDateInvalid;
import rado.pripomienkovac.DataDate;
import rado.pripomienkovac.Note;
import rado.pripomienkovac.TriggerSimpleDate;

public class ComponentRun {
    static Note n;
    private static JKalendarDen jkd;
    private static JKalendarMesiac jkm;
    
    public static void main(String[] args) throws ExceptionMyDateInvalid {
        Thread.setDefaultUncaughtExceptionHandler(new MyExHandler());
        n = new Note(1, 
                new TriggerSimpleDate(new DataDate(2013, 5, 23)),
                new ContentSimpleString("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a diam lectus. Sed sit amet ipsum mauris. Maecenas congue ligula ac quam viverra nec consectetur ante hendrerit. Donec et mollis dolor. Praesent et diam eget libero egestas mattis sit amet vitae augue. Nam tincidunt congue enim, ut porta lorem lacinia consectetur. Donec ut libero sed arcu vehicula ultricies a non tortor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ut gravida lorem. Ut turpis felis, pulvinar a semper sed, adipiscing id dolor. Pellentesque auctor nisi id magna consequat sagittis. Curabitur dapibus enim sit amet elit pharetra tincidunt feugiat nisl imperdiet. Ut convallis libero in urna ultrices accumsan. Donec sed odio eros. Donec viverra mi quis quam pulvinar at malesuada arcu rhoncus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. In rutrum accumsan ultricies. Mauris vitae nisi at sem facilisis semper ac in est."));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new MyExHandler());
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250,250);
        f.setLayout(new FlowLayout());
        f.getContentPane().add(new JPripomienka(n));
        DataDate dd;
        
        
        
        try {
            dd = new DataDate(2000, 1, 1);
        } catch (ExceptionMyDateInvalid ex) {
            throw new RuntimeException("mea culpa v ComponentRun.createAndShowGUI");
        }
        ComponentRun.jkd = new JKalendarDen(dd);
        ComponentRun.jkm = new JKalendarMesiac(dd);
        
        JKalendarDen jkdf = jkd;
        JKalendarMesiac jkmf = jkm;
        
        
        jkd.onSelect = new DchCalendarJoin(jkd, jkm);
        jkm.onSelect = new DchCalendarJoin(jkd, jkm);
        
        
        f.getContentPane().add(jkm);
        f.getContentPane().add(jkd);
        
        f.pack();
        //f.setSize(250,250);
        f.setVisible(true);
    }
}