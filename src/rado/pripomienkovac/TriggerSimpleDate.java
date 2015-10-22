package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class TriggerSimpleDate implements ITrigger {
    public DataDate datum;
    private static final int signature = 1;
    
    public static void register() {
        GeneralIByteArrayableGenerator.registerGenerator(signature, new TriggerSimpleDateGenerator());
    }
    
    public TriggerSimpleDate(DataDate datum) {
        this.datum = datum;
    }
    
    private static class TriggerSimpleDateGenerator implements IByteArrayableGenerator<IByteArrayable> {

        @Override
        public TriggerSimpleDate fromByteArray(byte[] arr) throws ExceptionInvalidData {
//            byte[] year = new byte[4];
//            byte[] month = new byte[4];
//            byte[] day = new byte[4];
//            System.arraycopy(arr, 0, year, 0, 4);
//            System.arraycopy(arr, 4, month, 0, 4);
//            System.arraycopy(arr, 8, day, 0, 4);
            try {
//                return new TriggerSimpleDate(new MyDate(
//                        Conversions.byteArrayToInt(year),
//                        Conversions.byteArrayToInt(month),
//                        Conversions.byteArrayToInt(day)
//                        ));
                return new TriggerSimpleDate(DataDate.fromByteArray(arr));
                //return MyDate.fromByteArray(arr)
            } catch (ExceptionMyDateInvalid ex) {
                throw new ExceptionInvalidData();
            }
            
            
        }
    }
    
    @Override
    public boolean isTriggered(ICircumstances factors) {
        if (factors instanceof CircumstancesToday) {
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
                datum.toByteArray());
    }
}
