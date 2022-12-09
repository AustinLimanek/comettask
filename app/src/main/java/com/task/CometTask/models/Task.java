package com.task.CometTask.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


public class Task {

    public Long id;

    private String name;
    private String body;

    private TaskStatusEnum status;
    private Date date;

    public enum TaskStatusEnum{
        OPEN("Open"),
        INPROGRESS("In Progress"),
        COMPLETED("Completed");


        private final String taskStatus;

        TaskStatusEnum(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public static TaskStatusEnum fromString(String possibleTaskStatus){
            for (TaskStatusEnum status : TaskStatusEnum.values()
            ) {
                if(status.taskStatus.equals(possibleTaskStatus)) {
                    return status;
                }
            }
            return null;
        }


        @NonNull
        @Override
        public String toString() {
            if (taskStatus == null){
                return "";
            }
            return taskStatus;
        }
    }

    public Task(String name, String body, TaskStatusEnum status, Date date) {
        this.name = name;
        this.body = body;
        this.status = status;
        this.date = date;
    }

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
