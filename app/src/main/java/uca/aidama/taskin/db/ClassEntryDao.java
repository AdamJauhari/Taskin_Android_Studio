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
public interface ClassEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClassEntryEntity classEntry);

    @Update
    void update(ClassEntryEntity classEntry);

    @Delete
    void delete(ClassEntryEntity classEntry);

    @Query("SELECT * FROM class_entries ORDER BY dayOfWeek, startTime")
    LiveData<List<ClassEntryEntity>> getAllClassEntries();

    @Query("SELECT * FROM class_entries WHERE id = :id")
    LiveData<ClassEntryEntity> getClassEntryById(String id);

    @Query("SELECT DISTINCT subject FROM class_entries ORDER BY subject")
    LiveData<List<String>> getUniqueSubjectNames();
    
    // Added for import/export functionality
    @Query("SELECT * FROM class_entries ORDER BY dayOfWeek, startTime")
    List<ClassEntryEntity> getAllClassEntriesSync();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ClassEntryEntity> classEntries);
    
    @Query("DELETE FROM class_entries")
    void deleteAll();
} 