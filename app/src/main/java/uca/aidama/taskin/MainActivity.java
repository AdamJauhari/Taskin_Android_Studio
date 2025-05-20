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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        
        // Setup Toolbar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Setup BottomNavigationView with NavController
        com.google.android.material.navigation.NavigationBarView navBarView = binding.navView;
        NavigationUI.setupWithNavController(navBarView, navController);

        TextView helloText = findViewById(R.id.text_hello_world);
        if (helloText != null) {
            helloText.setText(R.string.hello_world);
        }

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
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_light);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_light_mode));
        } else {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_dark);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dark_mode));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_toggle_theme) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                ThemeHelper.applyTheme(ThemeHelper.THEME_LIGHT);
                ThemeHelper.saveThemePreference(this, ThemeHelper.THEME_LIGHT);
            } else {
                ThemeHelper.applyTheme(ThemeHelper.THEME_DARK);
                ThemeHelper.saveThemePreference(this, ThemeHelper.THEME_DARK);
            }
            recreate();
            return true;
        } else if (itemId == R.id.action_language_english) {
            LocaleHelper.setLocale(this, "en");
            recreate();
            return true;
        } else if (itemId == R.id.action_language_indonesian) {
            LocaleHelper.setLocale(this, "in");
            recreate();
            return true;
        } else if (itemId == R.id.action_language_chinese) {
            LocaleHelper.setLocale(this, "zh");
            recreate();
            return true;
        }
        // Allow NavController to handle up button and other navigation actions in the ActionBar
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }
}