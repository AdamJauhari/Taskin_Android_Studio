package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

import uca.aidama.taskin.ui.schedule.DisplayableItem;

@Entity(tableName = "class_entries")
public class ClassEntryEntity implements DisplayableItem {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String subject;

    @NonNull
    public int dayOfWeek; // e.g., Calendar.MONDAY

    @NonNull
    public String startTime; // e.g., "09:00"

    @NonNull
    public String endTime; // e.g., "10:30"

    public String roomName;

    public String teacher;

    public ClassEntryEntity(@NonNull String subject, int dayOfWeek, @NonNull String startTime, @NonNull String endTime) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassEntryEntity that = (ClassEntryEntity) o;
        return id.equals(that.id) &&
                subject.equals(that.subject) &&
                dayOfWeek == that.dayOfWeek &&
                startTime.equals(that.startTime) &&
                endTime.equals(that.endTime) &&
                Objects.equals(roomName, that.roomName) &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, dayOfWeek, startTime, endTime, roomName, teacher);
    }
} 