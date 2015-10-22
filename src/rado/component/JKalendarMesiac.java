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
public class JKalendarMesiac extends JComponent {
    private final int CELL_SIZE = 32;
    
    private DataDate formerDate;
    private int tentoMesiacPrvyDen;
    private int selectedMonth;
    public DateChangeHandler onSelect;
    
    public JKalendarMesiac(DataDate formerDate) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(2 * CELL_SIZE, 7 * CELL_SIZE);
        this.formerDate = formerDate;
        this.selectedMonth = formerDate.month;
        
        final JKalendarMesiac transferCalendar = this;
        
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // zistit selected den
                int x = e.getX() / CELL_SIZE;
                int y = e.getY() / CELL_SIZE;
                int navrh = y + x * 6 + 1;
                if (navrh > 0 && navrh <= 12 && y < 6) {
                    transferCalendar.selectedMonth = navrh;
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
        return new Dimension(2 * CELL_SIZE, 7 * CELL_SIZE);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int x, y;
        for (int iMonth = 1; iMonth <= 12; iMonth++) {
            x = (iMonth - 1) / 6;
            y = (iMonth - 1) % 6;
            
            if (iMonth == this.selectedMonth) {
                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.white);
            }
            g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.black);
            g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            centeredText(x * CELL_SIZE, y * CELL_SIZE, String.valueOf(iMonth), (Graphics2D)g, CELL_SIZE);
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
        //this.repaint();
    }
    
    public DataDate getSelectedDate() {
        try {
            return new DataDate(formerDate.year, this.selectedMonth, formerDate.day);
        } catch (ExceptionMyDateInvalid ex) {
            try {
                return new DataDate(formerDate.year, this.selectedMonth, DataDate.getMonthLastDay(this.selectedMonth, formerDate.year));
            } catch (ExceptionMyDateInvalid ex1) {
                throw new RuntimeException("univalidated input in KalendarMesiac");
            }
        }
        
    }
}
