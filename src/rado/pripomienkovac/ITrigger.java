package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public interface ITrigger extends IByteArrayable {
    boolean isTriggered(ICircumstances env);
}
