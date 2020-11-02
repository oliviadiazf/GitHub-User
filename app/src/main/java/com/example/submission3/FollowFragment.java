package com.example.submission3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission3.ViewModel.DetailViewModel;
import com.example.submission3.ViewModel.FollowAdapter;

import java.util.Objects;

public class FollowFragment extends Fragment {
    private RecyclerView recyclerView;
    private String name, query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        recyclerView = view.findViewById(R.id.rv_follow);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

        assert getArguments() != null;
        name = getArguments().getString("name");
        query = getArguments().getString("query");


        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FollowAdapter adapter = new FollowAdapter();
        recyclerView.setAdapter(adapter);

        DetailViewModel detailViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DetailViewModel.class);
        detailViewModel.fetchFollow(name, query);
        detailViewModel.getUsers().observe(getViewLifecycleOwner(), adapter::setData);
    }
}
