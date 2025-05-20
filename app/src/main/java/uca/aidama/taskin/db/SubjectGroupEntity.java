package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "subject_groups", indices = {@Index(value = {"subjectName"}, unique = true)})
public class SubjectGroupEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    @ColumnInfo(name = "subjectName")
    public String subjectName;

    public SubjectGroupEntity(@NonNull String subjectName) {
        this.id = UUID.randomUUID().toString();
        this.subjectName = subjectName;
    }
} 