package uca.aidama.taskin.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import uca.aidama.taskin.model.SubjectGroupWithMembers;

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
    
    @Transaction
    @Query("SELECT * FROM subject_groups ORDER BY subjectName")
    LiveData<List<SubjectGroupWithMembers>> getAllSubjectGroupsWithMembers();
    
    // Added for import/export functionality
    @Query("SELECT * FROM subject_groups ORDER BY subjectName")
    List<SubjectGroupEntity> getAllSubjectGroupsSync();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubjectGroupEntity> subjectGroups);
    
    @Query("DELETE FROM subject_groups")
    void deleteAll();
} 