package uca.aidama.taskin.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import uca.aidama.taskin.util.LocaleHelper;
import uca.aidama.taskin.util.ThemeHelper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // Apply locale before UI is created
        super.attachBaseContext(LocaleHelper.getContextWithLocale(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Apply theme before super.onCreate() and setContentView()
        ThemeHelper.applyTheme(ThemeHelper.loadThemePreference(this));
        // Ensure locale is loaded, though attachBaseContext is more critical for initial render
        LocaleHelper.loadLocale(this); 
        super.onCreate(savedInstanceState);
    }
} 