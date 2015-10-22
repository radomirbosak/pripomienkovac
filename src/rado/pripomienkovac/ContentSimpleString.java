package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class ContentSimpleString implements IContent {
    private String obsah;
    private static final int signature = 2;
    
    public ContentSimpleString(String obsah) {
        this.obsah = obsah;
    }
    
    public static void register() {
        GeneralIByteArrayableGenerator.registerGenerator(signature, new ContentSimpleStringGenerator());
    }
    
    private static class ContentSimpleStringGenerator implements IByteArrayableGenerator<IByteArrayable> {

        @Override
        public IByteArrayable fromByteArray(byte[] arr) throws ExceptionInvalidData {
            return new ContentSimpleString(new String(arr));
        }
        
    }
    
    public String getString() {
        return obsah;
    }
    public void setString(String newValue) {
        obsah = newValue;
    }
    
    @Override
    public byte[] toByteArray() {
        return ArrayHelper.concat(
                Conversions.intToByteArray(signature),
                obsah.getBytes());
    }
    
}
