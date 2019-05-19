package ch.picturex;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SingleResourceBundle {

    private static ResourceBundle resourceBundle=null;
    private static Locale locale;

    private SingleResourceBundle(){}

    public static ResourceBundle getInstance(){
        if (resourceBundle == null){
            Preferences preference = Preferences.userNodeForPackage(SingleResourceBundle.class);
            String language = preference.get("language", "en");
            if(language.equals("en")){
                locale = Locale.ENGLISH;
            } else {
                locale = Locale.ITALIAN;
            }
            Locale.setDefault(locale);
            resourceBundle = ResourceBundle.getBundle("i18n/stringhe");
        }
        return resourceBundle;
    }

    public static Locale getLocale(){
        return  locale;
    }

}
