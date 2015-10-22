/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
//import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.*;
import rado.pripomienkovac.*;

/**
 *
 * @author ja
 */
public class JPripomienka extends JComponent {
    private final int NOTEHEIGHT = 64;
    private final int HEADWIDTH  = 64;
    private final int TAILWIDTH  = 64 * 5;
    private final int NOTEWIDTH  = HEADWIDTH + TAILWIDTH;
    private final Font headerfont = new Font("default", Font.BOLD, 12);
    
    private Note n;
    public JPripomienka(Note n) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(NOTEWIDTH, NOTEHEIGHT);
        this.n = n;
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(NOTEWIDTH, NOTEHEIGHT);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(this.getBackground());
        
        g2.fillRect(0, 0, NOTEWIDTH, NOTEHEIGHT);
        g2.setColor(Color.black);
        g2.drawRect(0, 0, HEADWIDTH, NOTEHEIGHT);
        g2.drawRect(HEADWIDTH, 0, TAILWIDTH, NOTEHEIGHT);
        
        if (n.trigger instanceof TriggerSimpleDate) {
            TriggerSimpleDate trigger = (TriggerSimpleDate)n.trigger;
            //multiLine(0, 0, trigger.toString(), g2);
            drawDate(0, 0, trigger, g2);
        }
        
        if (n.content instanceof ContentTitleString) {
            ContentTitleString content = (ContentTitleString)n.content;
            multiLine(HEADWIDTH + 3, 0 + 1, (content).getTitle(), g2, TAILWIDTH - 4);
        } else if (n.content instanceof ContentSimpleString) {
            ContentSimpleString content = (ContentSimpleString)n.content;
            multiLine(HEADWIDTH + 3, 0 + 1, (content).getString(), g2, TAILWIDTH - 4);
        }
        
    }
    
    void drawDate(int x, int y, TriggerSimpleDate trigger, Graphics2D g) {
        int TEXTMARGIN = 18;
        Font oldfont = g.getFont();
        g.setFont(headerfont);
        centeredTextInt(0, TEXTMARGIN * 1, trigger.datum.day,   g, HEADWIDTH);
        centeredText(0, TEXTMARGIN * 2, Conversions.monthToAbr(trigger.datum.month), g, HEADWIDTH);
        centeredTextInt(0, TEXTMARGIN * 3, trigger.datum.year,  g, HEADWIDTH);
        g.setFont(oldfont);
    }
    void centeredText(int x, int y, String text, Graphics2D g, int maxwidth) {
        //String stext = Integer.valueOf(text).toString();
        int stringwidth = (int)g.getFontMetrics().getStringBounds(text, g).getWidth();
        g.drawString(text, (maxwidth - stringwidth) / 2 + x, y);
    }
    void centeredTextInt(int x, int y, int text, Graphics2D g, int maxwidth) {
        String stext = Integer.valueOf(text).toString();
        centeredText(x, y, stext, g, maxwidth);
        //int stringwidth = (int)g.getFontMetrics().getStringBounds(stext, g).getWidth();
        //g.drawString(stext, (maxwidth - stringwidth) / 2 + x, y);
    }
    
    void multiLine (int x, int y, String label, Graphics2D g, int maxwidth) {

        //AffineTransform fontAT = new AffineTransform();
        //Font theFont = g.getFont();
//        fontAT.rotate(-Math.PI / 2);
//        Font theDerivedFont = theFont.deriveFont(fontAT);
//        g.setFont(theDerivedFont);
        if (label.isEmpty()) return;
        
        AttributedString attrStr = new AttributedString(label);
        // Get iterator for string:
        AttributedCharacterIterator characterIterator = attrStr.getIterator();

        // Get font context from graphics:
        FontRenderContext fontRenderContext = g.getFontRenderContext();

        // Create measurer:
        LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator,
                fontRenderContext);

        while (measurer.getPosition() < characterIterator.getEndIndex()) {
            TextLayout textLayout = measurer.nextLayout(maxwidth);
            y += textLayout.getAscent();
            textLayout.draw(g, x, y);

            y += textLayout.getDescent() + textLayout.getLeading();
        }
        //g.setFont(theFont);

    }
}
