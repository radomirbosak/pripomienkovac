/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import rado.pripomienkovac.DataDate;
import rado.pripomienkovac.ExceptionMyDateInvalid;
/**
 *
 * @author ja
 */
public class JKalendarDen extends JComponent {
    private final int CELL_SIZE = 32;
    
    private DataDate formerDate;
    private int dayOfWeekOf1st;
    private int selectedDay;
    public DateChangeHandler onSelect;
    
    public JKalendarDen(DataDate formerDate) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(5 * CELL_SIZE, 7 * CELL_SIZE);
        
        this.setFormerDate(formerDate);
        //this.formerDate = formerDate;
        this.selectedDay = formerDate.day;
        
        final JKalendarDen transferCalendar = this;
        
        
        
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // zistit selected den
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                int navrh = y + x * 7 - dayOfWeekOf1st + 1;
                if (navrh > 0 && navrh <= DataDate.getMonthLastDay(transferCalendar.formerDate.month, transferCalendar.formerDate.year)) {
                    transferCalendar.selectedDay = navrh;
                }
                if (transferCalendar.onSelect != null) 
                    onSelect.fire(transferCalendar.getSelectedDate());
                //transferCalendar.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(5 * CELL_SIZE, 7 * CELL_SIZE);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        int x, y;
        for (int iDay = 1; iDay <= DataDate.getMonthLastDay(formerDate.month, formerDate.year); iDay++) {
            x = (dayOfWeekOf1st + iDay - 1) / 7;
            y = (dayOfWeekOf1st + iDay - 1) % 7;
            
            if (iDay == this.selectedDay) {
                g.setColor(Color.yellow);
                
            } else if (iDay < this.formerDate.day) {
                g.setColor(Color.lightGray);
                
            } else {
                g.setColor(Color.white);
            }
            g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.black);
            g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            centeredText(x * CELL_SIZE, y * CELL_SIZE, String.valueOf(iDay), (Graphics2D)g, CELL_SIZE);
        }
        
    }
    private void centeredText(int x, int y, String text, Graphics2D g, int maxwidth) {
        //String stext = Integer.valueOf(text).toString();
        int stringwidth = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
        int stringheight = (int)g.getFontMetrics().getStringBounds(text, g).getHeight();
        g.drawString(text, (maxwidth - stringwidth) / 2 + x, y + stringheight * 1 + 6);
    }
    
    public void setFormerDate(DataDate newDate) {
        this.formerDate = newDate;
        
        DataDate pp2000 = null;
        try {
            // zistit aky den je prveho
            pp2000 = new DataDate(2000, 1, 1);
            System.out.println("bp1");
        } catch (ExceptionMyDateInvalid ex) {
            int q = 0;
            //throw new RuntimeException("Invalid DataDate checking code");
        }
        System.out.println("bp2");
        int vtedyDen = 5; // 1.1.2000 was Saturday
        dayOfWeekOf1st = (vtedyDen + DataDate.getDayDifference(formerDate, pp2000) - formerDate.day + 1) % 7;
        if (dayOfWeekOf1st < 0) {dayOfWeekOf1st += 7;}
        System.out.println("bp3");
        //this.repaint();
    }
    
    public DataDate getSelectedDate() {
        try {
            return new DataDate(formerDate.year, formerDate.month, this.selectedDay);
        } catch (ExceptionMyDateInvalid ex) {
            throw new RuntimeException("unvalidated Calendar input");
        }
    }
}
