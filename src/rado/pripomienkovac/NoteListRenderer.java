/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import rado.component.JPripomienka;

/**
 *
 * @author ja
 */
public class NoteListRenderer
    implements ListCellRenderer<Note> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Note> list, Note value, int index, boolean isSelected, boolean cellHasFocus) {
        JPripomienka pc = new JPripomienka(value);
        if (isSelected) {
            pc.setBackground(Color.gray);
        } else {
            //pc.setBackground(Color.red);
        }
        //pc.repaint();
        return pc;
    }
    
}
