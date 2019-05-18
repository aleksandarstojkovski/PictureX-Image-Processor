package ch.picturex;

import ch.picturex.controller.MainController;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ResourceBundleService {

    private static ResourceBundle resourceBundle=null;
    private static Locale locale;

    private ResourceBundleService(){}

    public static ResourceBundle getInstance(){
        if (resourceBundle == null){
            Preferences preference = Preferences.userNodeForPackage(ResourceBundleService.class);
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
