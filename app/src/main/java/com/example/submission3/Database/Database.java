package com.example.submission3.Database;

import androidx.room.RoomDatabase;

import com.example.submission3.Model.User;

@androidx.room.Database(entities = {User.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract DAO DAO();
}
