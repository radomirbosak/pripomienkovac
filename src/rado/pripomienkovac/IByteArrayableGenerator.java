/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public interface IByteArrayableGenerator<T extends IByteArrayable> {
    public T fromByteArray(byte[] arr) throws ExceptionInvalidData;
}
