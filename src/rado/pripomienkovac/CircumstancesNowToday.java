package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class CircumstancesNowToday extends CircumstancesToday{
    public DataTime cas;
    
    public CircumstancesNowToday(DataDate datum, DataTime cas) {
        super(datum);
        this.cas = cas;
    }
}
