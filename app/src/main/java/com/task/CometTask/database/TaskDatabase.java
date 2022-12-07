package com.task.CometTask.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.task.CometTask.dao.TaskDao;
import com.task.CometTask.models.Task;

// TODO Step: 4-6 Enable converters
@TypeConverters({TaskDatabaseConverters.class})
//TODO Step: 4-5 Setup the database!
@Database(entities = {Task.class}, version = 1) // if we update the version, it will delete the db!
public abstract class TaskDatabase extends RoomDatabase {
    // TODO Step: 4-6 add your DAO's!
    public abstract TaskDao taskDao();
}
