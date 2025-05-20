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
public interface SubjectGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubjectGroupEntity subjectGroup);

    @Update
    void update(SubjectGroupEntity subjectGroup);

    @Delete
    void delete(SubjectGroupEntity subjectGroup);

    @Query("SELECT * FROM subject_groups ORDER BY subjectName")
    LiveData<List<SubjectGroupEntity>> getAllSubjectGroups();

    @Query("SELECT * FROM subject_groups WHERE id = :id")
    LiveData<SubjectGroupEntity> getGroupById(String id);
} 