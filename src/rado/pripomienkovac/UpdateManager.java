package rado.pripomienkovac;

/**
 *
 * @author ja
 */
public class UpdateManager {
    private static int numUpdatedTriggers = 0;
    private static int numUpdatedContents = 0;
    
    public static IContent updateContent(IContent oldContent) {
        // list and check all contents to be update and return updated content
        IContent newContent = oldContent;
        if (oldContent instanceof ContentSimpleString && !(oldContent instanceof ContentTitleString)) {
            
            newContent = new ContentTitleString("", ((ContentSimpleString)oldContent).getString());
            numUpdatedContents++;
        }
        return newContent;
    }
    public static ITrigger updateTrigger(ITrigger oldTrigger) {
        // list and check all triggers to be update and return updated trigger
        if (false) {
            numUpdatedTriggers++;
            throw new UnsupportedOperationException("how did i get i here?");
        }
        return oldTrigger;
    }
    
    public static int getUpdatedTriggerCount() {
        return numUpdatedTriggers;
    }
    public static int getUpdatedContentCount() {
        return numUpdatedContents;
    }
}
