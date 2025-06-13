package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "material_items")
public class MaterialItemEntity {

    public static final String TYPE_VISUAL_NOTE = "VISUAL_NOTE";
    public static final String TYPE_FILE = "FILE";

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialItemEntity that = (MaterialItemEntity) o;
        return dateAdded == that.dateAdded &&
                id.equals(that.id) &&
                type.equals(that.type) &&
                title.equals(that.title) &&
                subject.equals(that.subject) &&
                Objects.equals(fileUriOrPath, that.fileUriOrPath) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, title, subject, dateAdded, fileUriOrPath, fileName);
    }
} 