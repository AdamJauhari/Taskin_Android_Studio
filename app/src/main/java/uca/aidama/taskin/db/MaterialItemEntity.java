package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "material_items")
public class MaterialItemEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String type; // "VISUAL_NOTE" or "FILE"

    @NonNull
    public String title;

    @NonNull
    public String subject; // Conceptually ForeignKey to ClassEntryEntity.subject

    public long dateAdded; // Milliseconds timestamp

    public String fileUriOrPath;

    public String fileName;

    public MaterialItemEntity(@NonNull String type, @NonNull String title, @NonNull String subject, long dateAdded) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.title = title;
        this.subject = subject;
        this.dateAdded = dateAdded;
    }
} 