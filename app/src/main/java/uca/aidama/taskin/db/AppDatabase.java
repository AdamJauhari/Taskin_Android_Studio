package uca.aidama.taskin.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
        ClassEntryEntity.class,
        MaterialItemEntity.class,
        SubjectGroupEntity.class,
        GroupMemberEntity.class,
        TaskReminderEntity.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClassEntryDao classEntryDao();
    public abstract MaterialItemDao materialItemDao();
    public abstract SubjectGroupDao subjectGroupDao();
    public abstract GroupMemberDao groupMemberDao();
    public abstract TaskReminderDao taskReminderDao();

    private static volatile AppDatabase INSTANCE;

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
} 