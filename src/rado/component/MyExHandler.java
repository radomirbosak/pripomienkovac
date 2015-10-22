/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.component;

/**
 *
 * @author ja
 */
public class MyExHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("s" + e.getMessage());
        System.exit(1);
    }
    
}
