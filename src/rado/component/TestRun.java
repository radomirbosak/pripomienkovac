/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

import javax.swing.JFrame;

/**
 *
 * @author ja
 */
public class TestRun {
    public TestRun() {
        JFrame f = new JFrame();
        f.setSize(500,400);
        f.add(new TestComponent());
        f.pack();
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    
    public static void main(String[] args) {
        new TestRun();
    }
}
