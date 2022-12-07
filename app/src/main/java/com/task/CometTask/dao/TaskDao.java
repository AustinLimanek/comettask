package com.task.CometTask.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.task.CometTask.models.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insertASuperPet(Task superPet);

    //find all
    @Query("SELECT * FROM Task")
    public List<Task> findAll();

    //findById
    @Query("SELECT * FROM Task WHERE id = :id")
    public Task findById(long id);

    // findAllByType
    @Query("SELECT * FROM Task WHERE status = :status")
    public List<Task> findAllByStatus(Task.TaskStatusEnum status);

    @Delete
    public void delete(Task superPet);

    @Update
    public void update(Task superPet);
}
