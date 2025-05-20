package uca.aidama.taskin.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupMemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GroupMemberEntity groupMember);

    @Update
    void update(GroupMemberEntity groupMember);

    @Delete
    void delete(GroupMemberEntity groupMember);

    @Query("SELECT * FROM group_members WHERE subjectGroupId = :subjectGroupId ORDER BY name")
    LiveData<List<GroupMemberEntity>> getMembersByGroupId(String subjectGroupId);
} 