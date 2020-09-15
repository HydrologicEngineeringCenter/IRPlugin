import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class IRMessages {
    public static final String Bundle_Name = IR18n.BUNDLE_NAME;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Bundle_Name);
    public static final String Plugin_Name = ".Name";
    public static final String Plugin_Description = "IRMain.Description";
    public static final String Plugin_Short_name = "IRMain.ShortName";
    private IRMessages(){
        super();
    }
    public static String getString(String key){
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch(MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }
}



