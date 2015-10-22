/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class ExceptionInvalidData extends Exception {
    public ExceptionInvalidData() {
        super();
    }
    public ExceptionInvalidData(String message) {
        super(message);
    }
}
class ExceptionIncorrectPassword extends ExceptionInvalidData {
    public ExceptionIncorrectPassword() {
        super();
    }
    public ExceptionIncorrectPassword(String message) {
        super(message);
    }
}