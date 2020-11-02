package com.example.submission3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.submission3.Database.DAO;
import com.example.submission3.Database.Database;

import java.util.Objects;

public class FavoriteProvider extends ContentProvider {
    private static final int CODE_USER = 1;
    private static final String AUTHORITY = "com.example.submission3";
    private static final String TABLE_NAME = "favorite_user";
    private static final String DB_NAME = "user";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private DAO DAO;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_USER);
    }

    @Override
    public boolean onCreate() {
        DAO = Room.databaseBuilder(Objects.requireNonNull(getContext()), Database.class, DB_NAME)
                .build()
                .DAO();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor = null;
        if (sUriMatcher.match(uri) == CODE_USER) {
            cursor = DAO.selectAll();
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
