package uca.aidama.taskin.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {

    private static final String PREFS_NAME = "LocalePrefs";
    private static final String KEY_LANGUAGE_CODE = "languageCode";
    private static final String DEFAULT_LANGUAGE = "in"; // Default to Indonesian
    
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_INDONESIAN = "in";
    public static final String LANGUAGE_CHINESE = "zh";
    public static final String LANGUAGE_SYSTEM = "system";

    public static void setLocale(Context context, String languageCode) {
        persistLanguage(context, languageCode);
        updateResources(context, languageCode);
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }

    public static String loadLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE_CODE, DEFAULT_LANGUAGE);
    }
    
    public static String getLanguage(Context context) {
        return loadLocale(context);
    }

    private static void persistLanguage(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE_CODE, languageCode);
        editor.apply();
    }

    public static Context onAttach(Context context) {
        String lang = loadLocale(context);
        return updateResources(context, lang);
    }

    public static Context getContextWithLocale(Context context) {
        String lang = loadLocale(context);
        return updateResources(context, lang);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }
} 