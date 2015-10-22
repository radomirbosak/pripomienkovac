/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class TriggerTimeDate extends TriggerSimpleDate {
    public DataTime cas;
    private static final int signature = 4;
    
    public static void register() {
        GeneralIByteArrayableGenerator.registerGenerator(signature, new TriggerTimeDateGenerator());
    }
    
    public TriggerTimeDate(DataDate datum, DataTime cas) {
        super(datum);
        this.cas = cas;
    }
    
    private static class TriggerTimeDateGenerator implements IByteArrayableGenerator<IByteArrayable> {

        @Override
        public TriggerSimpleDate fromByteArray(byte[] arr) throws ExceptionInvalidData {
            byte[] dateArr = new byte[DataDate.byteSize];
            byte[] timeArr = new byte[DataTime.byteSize];
            
            System.arraycopy(arr, 0,                dateArr, 0, DataDate.byteSize);
            System.arraycopy(arr, DataDate.byteSize,  timeArr, 0, DataTime.byteSize);
            
            try {
                return new TriggerTimeDate(
                        DataDate.fromByteArray(dateArr),
                        DataTime.fromByteArray(timeArr));
            } catch (ExceptionMyDateInvalid| ExceptionMyTimeInvalid ex) {
                throw new ExceptionInvalidData();
            }
        }
    }
    
    @Override
    public boolean isTriggered(ICircumstances factors) {
        if (factors instanceof CircumstancesNowToday) {
            DataDate today = ((CircumstancesNowToday)factors).datum;
            DataTime now = ((CircumstancesNowToday)factors).cas;
            return this.datum.equals(today) && this.cas.equals(now);
        } else if (factors instanceof CircumstancesToday) {
            DataDate today = ((CircumstancesToday)factors).datum;
            return datum.equals(today);
        } else {
            return false;
        }
    }

    @Override
    public byte[] toByteArray() {
        return ArrayHelper.concat(
                Conversions.intToByteArray(signature),
                datum.toByteArray(),
                cas.toByteArray());
    }
}
