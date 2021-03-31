package com.example.todo_finalapp.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String note;

    public int getUserRelationId() {
        return userRelationId;
    }

    public void setUserRelationId(int userRelationId) {
        this.userRelationId = userRelationId;
    }
    private int userRelationId;




    private int priority;
    @ColumnInfo(name="updated_at")
    private Date updatedAt;

    @Ignore
    public TaskEntry(String description,String note, int priority, Date updatedAt, int userRelationId) {


        this.description = description;
        this.note=note;
        this.priority = priority;
        this.updatedAt = updatedAt;
        this.userRelationId=userRelationId;

    }

    public TaskEntry(int id, String description,String note, int priority, Date updatedAt,int userRelationId) {


        this.id = id;
        this.description = description;
        this.note=note;
        this.priority = priority;
        this.updatedAt = updatedAt;
        this.userRelationId=userRelationId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
