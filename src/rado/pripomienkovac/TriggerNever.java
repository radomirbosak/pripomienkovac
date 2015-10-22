package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class TriggerNever implements ITrigger{
    private static final int signature = 0;
    
    @Override
    public boolean isTriggered(ICircumstances factors) {
        return false;
    }

    @Override
    public byte[] toByteArray() {
        return Conversions.intToByteArray(signature);
    }
    
}
