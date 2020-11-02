package com.example.consumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TABLE_NAME = "favorite_user";
    public static final String AUTHORITY = "com.example.submission3";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).appendPath(TABLE_NAME).build();

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_AVATAR = "avatar";

    @BindView(R.id.rv_favorite_ca) RecyclerView recyclerView;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //adapter
        MainAdapter adapter = new MainAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //recycleview
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //contentProvider
        ContentProviderClient contentProviderClient = getContentResolver().acquireContentProviderClient(CONTENT_URI);
        Cursor cursor;
        try {
            assert contentProviderClient != null;
            cursor = contentProviderClient.query(CONTENT_URI, null, null, null, null);
            adapter.setData(cursor);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
    }
}
