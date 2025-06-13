package uca.aidama.taskin;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import uca.aidama.taskin.databinding.ActivityMainBinding;
import uca.aidama.taskin.ui.BaseActivity;
import uca.aidama.taskin.util.FileUtils;
import uca.aidama.taskin.util.LocaleHelper;
import uca.aidama.taskin.util.ThemeHelper;
import uca.aidama.taskin.viewmodel.SettingsViewModel;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Method;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private SettingsViewModel viewModel;
    private String jsonDataToExport;
    
    // Activity result launchers for file operations
    private ActivityResultLauncher<String> createFileLauncher;
    private ActivityResultLauncher<String[]> openFileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        
        // Initialize view model
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Initialize activity result launchers
        createFileLauncher = registerForActivityResult(
                new ActivityResultContracts.CreateDocument("application/json"),
                this::handleExportFileResult);

        openFileLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleImportFileResult);

        // Get NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Initialize appBarConfiguration with top-level destinations
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_schedule,
                R.id.navigation_materials,
                R.id.navigation_groups,
                R.id.navigation_reminders)
                .build();

        // Setup Toolbar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Setup BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Add a destination changed listener to show/hide BottomNavigationView
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Hide BottomNavigationView for ProgramInfoFragment and dialog destinations
            boolean isMainDestination = 
                    destination.getId() == R.id.navigation_home ||
                    destination.getId() == R.id.navigation_schedule ||
                    destination.getId() == R.id.navigation_materials ||
                    destination.getId() == R.id.navigation_groups ||
                    destination.getId() == R.id.navigation_reminders;
            
            binding.navView.setVisibility(isMainDestination ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        
        // Change the overflow menu icon to settings icon
        Toolbar toolbar = binding.toolbar;
        if (toolbar != null) {
            toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_settings));
        }
        
        // Force showing icons in overflow menu
        try {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem modeTerangItem = menu.findItem(R.id.action_mode_terang);
        MenuItem languageItem = menu.findItem(R.id.action_language);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            modeTerangItem.setTitle(R.string.mode_terang);
            modeTerangItem.setIcon(R.drawable.ic_mode_terang);
        } else {
            modeTerangItem.setTitle(R.string.mode_gelap);
            modeTerangItem.setIcon(R.drawable.ic_mode_gelap);
        }

        // Set checked state for language menu items
        String currentLang = LocaleHelper.loadLocale(this);
        if (currentLang.equals("en")) {
            menu.findItem(R.id.action_language_english).setChecked(true);
        } else if (currentLang.equals("in")) {
            menu.findItem(R.id.action_language_indonesian).setChecked(true);
        } else if (currentLang.equals("zh")) {
            menu.findItem(R.id.action_language_chinese).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_class_program) {
            navController.navigate(R.id.navigation_class_program);
            return true;
        } else if (itemId == R.id.action_mode_terang) {
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
        } else if (itemId == R.id.action_export_data) {
            handleExportData();
            return true;
        } else if (itemId == R.id.action_import_data) {
            openFileLauncher.launch(new String[]{"application/json"});
            return true;
        } else if (itemId == R.id.action_language) {
            return super.onOptionsItemSelected(item);
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
    
    private void handleExportData() {
        viewModel.exportAllData().observe(this, jsonData -> {
            if (jsonData != null) {
                jsonDataToExport = jsonData;
                // Launch file creation intent
                createFileLauncher.launch(getString(R.string.settings_export_file_name));
            } else {
                Toast.makeText(this, R.string.settings_file_export_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Handle the result of file export operation
     */
    private void handleExportFileResult(Uri uri) {
        if (uri != null && jsonDataToExport != null) {
            boolean success = FileUtils.writeStringToUri(this, uri, jsonDataToExport);
            if (success) {
                Toast.makeText(this, R.string.settings_file_export_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.settings_file_export_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.settings_file_export_failed, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handle the result of file import operation
     */
    private void handleImportFileResult(Uri uri) {
        if (uri != null) {
            String jsonData = FileUtils.readStringFromUri(this, uri);
            
            if (jsonData != null && !jsonData.isEmpty()) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.settings_import_confirm_title)
                        .setMessage(R.string.settings_import_confirm_message)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            viewModel.importAllData(jsonData).observe(this, success -> {
                                if (success) {
                                    Toast.makeText(this, R.string.settings_file_import_success, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(this, R.string.settings_file_import_failed, Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            } else {
                Toast.makeText(this, R.string.settings_file_import_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.settings_file_import_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}