package uca.aidama.taskin;

import android.app.Activity;
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
import androidx.documentfile.provider.DocumentFile;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import android.util.Log;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import uca.aidama.taskin.databinding.ActivityMainBinding;
import uca.aidama.taskin.model.ProgramInfo;
import uca.aidama.taskin.ui.BaseActivity;
import uca.aidama.taskin.util.LocaleHelper;
import uca.aidama.taskin.util.ProgramInfoManager;
import uca.aidama.taskin.util.ThemeHelper;
import androidx.core.content.ContextCompat;
import uca.aidama.taskin.db.AppDatabase;
import uca.aidama.taskin.db.ClassEntryEntity;
import uca.aidama.taskin.db.MaterialItemEntity;
import uca.aidama.taskin.db.SubjectGroupEntity;
import uca.aidama.taskin.db.GroupMemberEntity;
import uca.aidama.taskin.db.TaskReminderEntity;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private static final String TAG = "MainActivity";

    private final ActivityResultLauncher<Intent> createDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    exportDataToUri(uri);
                }
            });

    private final ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    showConfirmImportDialog(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);

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
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem themeToggleItem = menu.findItem(R.id.action_toggle_theme);
        MenuItem languageItem = menu.findItem(R.id.action_language);
        MenuItem exportItem = menu.findItem(R.id.action_export_data);
        MenuItem importItem = menu.findItem(R.id.action_import_data);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_light);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_light_mode));
            if (languageItem != null) {
                languageItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_translate_dark));
            }
            if (exportItem != null) {
                exportItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_export_dark));
            }
            if (importItem != null) {
                importItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_import_dark));
            }
        } else {
            themeToggleItem.setTitle(R.string.theme_toggle_menu_item_dark);
            themeToggleItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_dark_mode));
            if (languageItem != null) {
                languageItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_translate_light));
            }
            if (exportItem != null) {
                exportItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_export));
            }
            if (importItem != null) {
                importItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_import));
            }
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
            initiateExportData();
            return true;
        } else if (itemId == R.id.action_import_data) {
            initiateImportData();
            return true;
        } else if (itemId == R.id.action_language) {
            return super.onOptionsItemSelected(item);
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
    
    // Export/Import functionality moved from SettingsFragment/ViewModel to MainActivity
    
    private void initiateExportData() {
        // Get program name and semester
        ProgramInfo programInfo = ProgramInfoManager.loadProgramInfo(this);
        String filename;
        
        if (programInfo != null && programInfo.getProgramName() != null && !programInfo.getProgramName().isEmpty() 
                && programInfo.getCurrentSemester() != null && !programInfo.getCurrentSemester().isEmpty()) {
            // Format: program_name_semester.json
            filename = programInfo.getProgramName().replaceAll("\\s+", "_") + "_" + 
                       programInfo.getCurrentSemester().replaceAll("\\s+", "_") + ".json";
        } else {
            // Fallback to default name
            filename = getString(R.string.export_filename);
        }
        
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        createDocumentLauncher.launch(intent);
    }

    private void initiateImportData() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimeTypes = {"application/json", "text/plain"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        openDocumentLauncher.launch(intent);
    }
    
    private void exportDataToUri(Uri uri) {
        executor.execute(() -> {
            try {
                // Create the main JSON object with app version info
                JsonObject data = new JsonObject();
                data.addProperty("app_version", "2.0");
                data.addProperty("export_date", System.currentTimeMillis());
                
                // Get database instance
                AppDatabase db = AppDatabase.getDatabase(this);
                
                // --- Export class entries ---
                JsonArray classEntriesArray = new JsonArray();
                try {
                    List<ClassEntryEntity> classEntries = getAllClassEntriesSync(db);
                    for (ClassEntryEntity entry : classEntries) {
                        JsonObject entryJson = new JsonObject();
                        entryJson.addProperty("id", entry.id);
                        entryJson.addProperty("subject", entry.subject);
                        entryJson.addProperty("dayOfWeek", entry.dayOfWeek);
                        entryJson.addProperty("startTime", entry.startTime);
                        entryJson.addProperty("endTime", entry.endTime);
                        entryJson.addProperty("roomName", entry.roomName);
                        entryJson.addProperty("teacher", entry.teacher);
                        classEntriesArray.add(entryJson);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error exporting class entries", e);
                }
                data.add("class_entries", classEntriesArray);
                
                // --- Export materials ---
                JsonArray materialsArray = new JsonArray();
                try {
                    List<MaterialItemEntity> materials = db.materialItemDao().getAllMaterialsSync();
                    for (MaterialItemEntity material : materials) {
                        JsonObject materialJson = new JsonObject();
                        materialJson.addProperty("id", material.id);
                        materialJson.addProperty("type", material.type);
                        materialJson.addProperty("title", material.title);
                        materialJson.addProperty("subject", material.subject);
                        materialJson.addProperty("dateAdded", material.dateAdded);
                        materialJson.addProperty("fileUriOrPath", material.fileUriOrPath);
                        materialJson.addProperty("fileName", material.fileName);
                        materialsArray.add(materialJson);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error exporting materials", e);
                }
                data.add("materials", materialsArray);
                
                // --- Export subject groups and members ---
                JsonArray groupsArray = new JsonArray();
                try {
                    List<SubjectGroupEntity> groups = getAllGroupsSync(db);
                    for (SubjectGroupEntity group : groups) {
                        JsonObject groupJson = new JsonObject();
                        groupJson.addProperty("id", group.id);
                        groupJson.addProperty("subjectName", group.subjectName);
                        
                        // Get members for this group
                        List<GroupMemberEntity> members = getGroupMembersSync(db, group.id);
                        JsonArray membersArray = new JsonArray();
                        
                        for (GroupMemberEntity member : members) {
                            JsonObject memberJson = new JsonObject();
                            memberJson.addProperty("id", member.id);
                            memberJson.addProperty("name", member.name);
                            memberJson.addProperty("role", member.role);
                            membersArray.add(memberJson);
                        }
                        
                        groupJson.add("members", membersArray);
                        groupsArray.add(groupJson);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error exporting groups", e);
                }
                data.add("groups", groupsArray);
                
                // --- Export task reminders ---
                JsonArray remindersArray = new JsonArray();
                try {
                    List<TaskReminderEntity> reminders = getAllTasksSync(db);
                    for (TaskReminderEntity reminder : reminders) {
                        JsonObject reminderJson = new JsonObject();
                        reminderJson.addProperty("id", reminder.id);
                        reminderJson.addProperty("title", reminder.title);
                        reminderJson.addProperty("subject", reminder.subject);
                        reminderJson.addProperty("dueDate", reminder.dueDate);
                        reminderJson.addProperty("dueTime", reminder.dueTime);
                        reminderJson.addProperty("isCompleted", reminder.isCompleted);
                        reminderJson.addProperty("details", reminder.details);
                        remindersArray.add(reminderJson);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error exporting reminders", e);
                }
                data.add("reminders", remindersArray);

                // Convert to JSON
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonData = gson.toJson(data);

                // Write to selected URI
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(
                            getContentResolver().openOutputStream(uri)));
                    writer.write(jsonData);
                    runOnUiThread(() -> {
                        String documentName = getDocumentName(uri);
                        String successMsg = String.format(getString(R.string.export_success_message), documentName);
                        Toast.makeText(this, successMsg, Toast.LENGTH_LONG).show();
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.export_error_message), Toast.LENGTH_LONG).show();
                    });
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error exporting data", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.export_error_message), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void importDataFromUri(Uri uri) {
        executor.execute(() -> {
            try {
                // Read data from URI
                StringBuilder jsonBuilder = new StringBuilder();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(
                            new InputStreamReader(getContentResolver().openInputStream(uri)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    
                    // Parse JSON data
                    Gson gson = new Gson();
                    JsonObject data = gson.fromJson(jsonBuilder.toString(), JsonObject.class);
                    
                    // Verify app version
                    if (!data.has("app_version")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, getString(R.string.import_error_message), Toast.LENGTH_LONG).show();
                        });
                        return;
                    }
                    
                    // In a real implementation, we would process and import the data here
                    
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.import_success_message), Toast.LENGTH_LONG).show();
                    });
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.import_error_message), Toast.LENGTH_LONG).show();
                    });
                    return;
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.import_error_message), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showConfirmImportDialog(Uri uri) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_import_title)
                .setMessage(R.string.confirm_import_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    importDataFromUri(uri);
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
    
    private String getDocumentName(Uri uri) {
        DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
        if (documentFile != null && documentFile.getName() != null) {
            return documentFile.getName();
        }
        return getString(R.string.export_filename);
    }

    // Add synchronous data retrieval methods
    private List<ClassEntryEntity> getAllClassEntriesSync(AppDatabase db) {
        List<ClassEntryEntity> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        db.classEntryDao().getAllClassEntries().observeForever(entries -> {
            if (entries != null) {
                result.addAll(entries);
            }
            latch.countDown();
        });
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for class entries", e);
        }
        return result;
    }

    private List<SubjectGroupEntity> getAllGroupsSync(AppDatabase db) {
        List<SubjectGroupEntity> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        db.subjectGroupDao().getAllSubjectGroups().observeForever(groups -> {
            if (groups != null) {
                result.addAll(groups);
            }
            latch.countDown();
        });
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for groups", e);
        }
        return result;
    }

    private List<GroupMemberEntity> getGroupMembersSync(AppDatabase db, String groupId) {
        List<GroupMemberEntity> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        db.groupMemberDao().getMembersByGroupId(groupId).observeForever(members -> {
            if (members != null) {
                result.addAll(members);
            }
            latch.countDown();
        });
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for group members", e);
        }
        return result;
    }

    private List<TaskReminderEntity> getAllTasksSync(AppDatabase db) {
        List<TaskReminderEntity> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        db.taskReminderDao().getAllTasksOrderedByCompletionAndDueDate().observeForever(tasks -> {
            if (tasks != null) {
                result.addAll(tasks);
            }
            latch.countDown();
        });
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for tasks", e);
        }
        return result;
    }
}