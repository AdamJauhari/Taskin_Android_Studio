package uca.aidama.taskin;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
// Import Toolbar
import androidx.appcompat.widget.Toolbar;

// Removed Button import as they are no longer used

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI; // Needed for Toolbar setup with NavController

import uca.aidama.taskin.databinding.ActivityMainBinding;
import uca.aidama.taskin.ui.BaseActivity;
import uca.aidama.taskin.util.LocaleHelper;
import uca.aidama.taskin.util.ThemeHelper;
import androidx.core.content.ContextCompat;

public class MainActivity extends BaseActivity { 

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        
        // Initialize appBarConfiguration with top-level destinations from BottomNavigationView
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_schedule, R.id.navigation_materials,
                R.id.navigation_groups, R.id.navigation_reminders)
                .build();
        
        // Setup Toolbar with NavController and the defined appBarConfiguration
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        
        // Setup BottomNavigationView with NavController
        com.google.android.material.navigation.NavigationBarView navBarView = binding.navView;
        NavigationUI.setupWithNavController(navBarView, navController);

        // Placeholder button listeners are removed as they are no longer in the layout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem themeToggleItem = menu.findItem(R.id.action_toggle_theme);
        MenuItem languageItem = menu.findItem(R.id.action_language); // Get language menu item

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_light);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_light_mode));
            if (languageItem != null) { // Set dark theme translate icon
                languageItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_translate_dark));
            }
        } else {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_dark);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dark_mode));
            if (languageItem != null) { // Set light theme translate icon
                languageItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_translate_light));
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_class_program) {
            if (navController != null) {
                navController.navigate(R.id.navigation_class_program);
            }
            return true; // Consumed the event
        } else if (itemId == R.id.action_toggle_theme) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                ThemeHelper.applyTheme(ThemeHelper.THEME_LIGHT);
                ThemeHelper.saveThemePreference(this, ThemeHelper.THEME_LIGHT);
            } else {
                ThemeHelper.applyTheme(ThemeHelper.THEME_DARK);
                ThemeHelper.saveThemePreference(this, ThemeHelper.THEME_DARK);
            }
            recreate();
            return true; // Consumed the event
        } else if (itemId == R.id.action_language_english) {
            LocaleHelper.setLocale(this, "en");
            recreate();
            return true; // Consumed the event
        } else if (itemId == R.id.action_language_indonesian) {
            LocaleHelper.setLocale(this, "in");
            recreate();
            return true; // Consumed the event
        } else if (itemId == R.id.action_language_chinese) {
            LocaleHelper.setLocale(this, "zh");
            recreate();
            return true; // Consumed the event
        }

        // If the item is not one of our custom handled items,
        // let NavigationUI try to handle it (e.g., for settings submenu if structured for nav).
        // Otherwise, fall back to super.
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handles the Up button in the ActionBar. 
        // It will try to navigate up in the NavController's back stack.
        // If the current destination is a top-level destination (defined in appBarConfiguration),
        // it might not navigate up, or the behavior might depend on the specific setup.
        // This ensures that NavController's logic is prioritized for Up navigation.
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}