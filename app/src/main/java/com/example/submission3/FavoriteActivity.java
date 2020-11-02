package com.example.submission3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.submission3.Database.DAO;
import com.example.submission3.Database.Database;
import com.example.submission3.Model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.rv_favorite) RecyclerView recyclerView;
    @BindView(R.id.progressBar_favorite) ProgressBar progressBar;
    @BindView(R.id.linearNotFound_favorite) LinearLayout linearNotFound;
    @BindView(R.id.txtNotFound_favorite) TextView notFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Favorites");

        FavoriteAdapter adapter = new FavoriteAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DAO DAO = Room.databaseBuilder(this, Database.class, "user")
                .allowMainThreadQueries()
                .build()
                .DAO();

        List<User> listUser = DAO.getAll();
        ArrayList<User> arrayUser = new ArrayList<>(listUser);

        notFound.setText(R.string.empty_favorite_list);

        showLoading(true);
        if (listUser.isEmpty()) {
            showLoading(false);
            linearNotFound.setVisibility(View.VISIBLE);
        }

        adapter.setData(arrayUser);
        adapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_USER, data);
            startActivity(intent);
        });

        showLoading(false);
    }

    private void showLoading(Boolean state) {
        if (state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }
}