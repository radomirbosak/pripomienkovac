package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class ContentTitleString extends ContentSimpleString {
    private String title;
    private static final int signature = 3;

    public ContentTitleString(String title, String obsah) {
        super(obsah);
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public static void register() {
        GeneralIByteArrayableGenerator.registerGenerator(signature, new ContentTitleStringGenerator());
    }
    
    private static class ContentTitleStringGenerator implements IByteArrayableGenerator<IByteArrayable> {

        @Override
        public IByteArrayable fromByteArray(byte[] arr) throws ExceptionInvalidData {
            byte[] conLenArr = new byte[4];
            byte[] titLenArr = new byte[4];
            System.arraycopy(arr, 0, conLenArr, 0, 4);
            System.arraycopy(arr, 4, titLenArr, 0, 4);
            int conLen = Conversions.byteArrayToInt(conLenArr);
            int titLen = Conversions.byteArrayToInt(titLenArr);
            
            byte[] conArr = new byte[conLen];
            byte[] titArr = new byte[titLen];
            System.arraycopy(arr, 8, conArr, 0, conLen);
            System.arraycopy(arr, 8 + conLen, titArr, 0, titLen);
            
            return new ContentTitleString(new String(titArr), new String(conArr));
        }
        
    }
    
    @Override
    public byte[] toByteArray() {
        byte[] obsahBytes = this.getString().getBytes();
        byte[] titleBytes = this.getTitle().getBytes();
        
        return ArrayHelper.concat(
                Conversions.intToByteArray(signature),          // signature = 3
                Conversions.intToByteArray(obsahBytes.length),  // content length
                Conversions.intToByteArray(titleBytes.length),  // title length
                obsahBytes,                                     // content bytes
                titleBytes);                                    // title bytes
    }
    
}
