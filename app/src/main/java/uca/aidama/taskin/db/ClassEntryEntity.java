package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "class_entries")
public class ClassEntryEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String subject;

    @NonNull
    public String dayOfWeek; // e.g., "Monday"

    @NonNull
    public String startTime; // e.g., "09:00"

    @NonNull
    public String endTime; // e.g., "10:30"

    public String location;

    public String teacher;

    public ClassEntryEntity(@NonNull String subject, @NonNull String dayOfWeek, @NonNull String startTime, @NonNull String endTime) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
} 