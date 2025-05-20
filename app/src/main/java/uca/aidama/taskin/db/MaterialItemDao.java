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
public interface MaterialItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MaterialItemEntity materialItem);

    @Update
    void update(MaterialItemEntity materialItem);

    @Delete
    void delete(MaterialItemEntity materialItem);

    @Query("SELECT * FROM material_items ORDER BY dateAdded DESC")
    LiveData<List<MaterialItemEntity>> getAllMaterials();

    @Query("SELECT * FROM material_items WHERE subject = :subject ORDER BY dateAdded DESC")
    LiveData<List<MaterialItemEntity>> getMaterialsBySubject(String subject);

    @Query("SELECT * FROM material_items WHERE type = :type ORDER BY dateAdded DESC")
    LiveData<List<MaterialItemEntity>> getMaterialsByType(String type);
} 