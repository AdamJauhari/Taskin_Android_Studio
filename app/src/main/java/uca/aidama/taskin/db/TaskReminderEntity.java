package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "task_reminders")
public class TaskReminderEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String title;

    public String subject;

    public long dueDate; // Milliseconds timestamp for date

    public String dueTime; // e.g., "14:30"

    @ColumnInfo(defaultValue = "false")
    public boolean isCompleted;

    public String details;

    public TaskReminderEntity(@NonNull String title, long dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.dueDate = dueDate;
        this.isCompleted = false; // Explicitly set default, though ColumnInfo handles DB default
    }
} 