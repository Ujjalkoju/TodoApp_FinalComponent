package com.example.todo_finalapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoUser {
    @Query("Select * from user where email= :mail and password= :password")
    User getUser(String mail, String password);

    @Insert
    void insert(User user);
    @Update
    void updateU (User user);

    @Delete
    void deleteU    (User user);
}