package uca.aidama.taskin.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        ClassEntryEntity.class,
        MaterialItemEntity.class,
        SubjectGroupEntity.class,
        GroupMemberEntity.class,
        TaskReminderEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClassEntryDao classEntryDao();
    public abstract MaterialItemDao materialItemDao();
    public abstract SubjectGroupDao subjectGroupDao();
    public abstract GroupMemberDao groupMemberDao();
    public abstract TaskReminderDao taskReminderDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "taskin_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    
    // For use in the settings module
    public static AppDatabase getInstance(final Context context) {
        return getDatabase(context);
    }
} 