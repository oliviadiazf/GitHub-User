package com.example.submission3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.submission3.ViewModel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Dashboard extends AppCompatActivity {
    private UserAdapter userAdapter;
    private MainViewModel mainViewModel;
    SearchView mySearchView;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.notFound) LinearLayout notFound;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.Title);

        ButterKnife.bind(this);

        //search
        mySearchView = (SearchView)findViewById(R.id.SearchView);
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                notFound.setVisibility(View.GONE);
                showLoading(true);
                mainViewModel.fetchUser(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        //recyclerview
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //viewmodel
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getUsers().observe(this, users -> {
            userAdapter.setData(users);

            if (users.size() == 0) notFound.setVisibility(View.VISIBLE);
            else notFound.setVisibility(View.GONE);

            showLoading(false);
        });

        //adapter
        userAdapter = new UserAdapter();
        userAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(userAdapter);

        userAdapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(Dashboard.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_USER, data);
            startActivity(intent);
        });
    }

    private void showLoading(boolean b) {
        if (b) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                Intent favorite = new Intent(Dashboard.this, FavoriteActivity.class);
                startActivity(favorite);
                break;
            case R.id.menu_setting:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, new SettingFragment());
                fragmentTransaction.addToBackStack("optional tag");
                fragmentTransaction.commit();
        }
        return  super.onOptionsItemSelected(item);
    }
}