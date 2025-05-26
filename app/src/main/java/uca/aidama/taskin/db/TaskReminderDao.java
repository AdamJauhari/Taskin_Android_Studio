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
public interface TaskReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskReminderEntity taskReminder);

    @Update
    void update(TaskReminderEntity taskReminder);

    @Delete
    void delete(TaskReminderEntity taskReminder);

    @Query("SELECT * FROM task_reminders ORDER BY dueDate, dueTime")
    LiveData<List<TaskReminderEntity>> getAllTasks();

    @Query("SELECT * FROM task_reminders WHERE id = :id")
    LiveData<TaskReminderEntity> getTaskById(String id);
    
    @Query("SELECT * FROM task_reminders ORDER BY isCompleted ASC, dueDate ASC, dueTime ASC")
    LiveData<List<TaskReminderEntity>> getAllTasksOrderedByCompletionAndDueDate();
    
    @Query("UPDATE task_reminders SET isCompleted = :isCompleted WHERE id = :taskId")
    void updateTaskCompletionStatus(String taskId, boolean isCompleted);
} 