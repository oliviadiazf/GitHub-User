package com.example.submission3.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.submission3.Model.User;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT * FROM user WHERE name = :name")
    User getUserByName(String name);

    @Query("SELECT * FROM user ORDER BY userId DESC")
    List<User> getAll();

    @Query("SELECT * FROM user ORDER BY userId DESC")
    Cursor selectAll();

    @Query("DELETE FROM user WHERE userId = :userId")
    void deleteByName(int userId);

    @Insert
    void insertAll(User... users);
}
