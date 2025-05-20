package uca.aidama.taskin.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "group_members",
        foreignKeys = @ForeignKey(entity = SubjectGroupEntity.class,
                                   parentColumns = "id",
                                   childColumns = "subjectGroupId",
                                   onDelete = ForeignKey.CASCADE),
        indices = {@Index("subjectGroupId")})
public class GroupMemberEntity {

    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String subjectGroupId;

    @NonNull
    public String name;

    public String role;

    public GroupMemberEntity(@NonNull String subjectGroupId, @NonNull String name) {
        this.id = UUID.randomUUID().toString();
        this.subjectGroupId = subjectGroupId;
        this.name = name;
    }
} 