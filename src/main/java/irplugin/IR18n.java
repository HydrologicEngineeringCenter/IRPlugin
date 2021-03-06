package irplugin;

import com.rma.util.I18n;

import java.util.ResourceBundle;

public class IR18n extends I18n {
    public static final String BUNDLE_NAME = "IRProperties";
    private static final ResourceBundle SAMPLE_RESOURCE_BUNDLE;
    private ResourceBundle _resourceBundle;
    static
    {
        SAMPLE_RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
    }
    protected IR18n(String prefix, String bundleName)
    {
        super(prefix, bundleName);
    }
    public static I18n getI18n(String prefix)
    {
        return new IR18n(prefix, BUNDLE_NAME);
    }
}
