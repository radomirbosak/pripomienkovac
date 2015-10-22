package rado.pripomienkovac;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ja
 */
public class GeneralIByteArrayableGenerator {
    public static Map<Integer,IByteArrayableGenerator<IByteArrayable>> generators = new HashMap<>();
    public static void registerGenerator(int signature, IByteArrayableGenerator<IByteArrayable> generator) {
        generators.put(signature, generator);
    }
    
    /**
     * Registers Trigger and Content generators
     * by using respective voluntary .register() functions
     */
    public static void initGenerators() {
        // Triggers
        TriggerSimpleDate.register();   // sig = 1
        
        // Contents
        ContentSimpleString.register(); // sig = 2
        ContentTitleString.register();  // sig = 3
    }
    
    public static IByteArrayable fromByteArray(byte[] arr) throws ExceptionInvalidData {
        byte[] sig = new byte[4];
        System.arraycopy(arr, 0, sig, 0, 4);
        
        byte[] rest = new byte[arr.length - 4];
        System.arraycopy(arr, 4, rest, 0, arr.length - 4);
        
        int intsig = Conversions.byteArrayToInt(sig);
        if (generators.containsKey(intsig)) {
            return generators.get(intsig).fromByteArray(rest);
        } else {
            throw new ExceptionInvalidData("generator with signature " + intsig + " not registered");
        }
    }
}
