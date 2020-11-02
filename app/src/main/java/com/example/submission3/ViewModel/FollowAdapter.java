package com.example.submission3.ViewModel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission3.DetailActivity;
import com.example.submission3.Model.User;
import com.example.submission3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {
    private final ArrayList<User> Data = new ArrayList<>();

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow, parent, false);

        return new FollowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        holder.setItem(Data.get(position));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_USER, Data.get(position));
            view.getContext().startActivity(intent);
        });
    }

    public FollowAdapter() {
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    static class FollowViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_follow_avatar)
        ImageView avatar;
        @BindView(R.id.tv_follow_name)
        TextView name;
        @BindView(R.id.tv_follow_username)
        TextView username;

        FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setItem(User item) {
            name.setText(item.getName());
            username.setText(item.getUsername());
            Picasso.get()
                    .load(item.getAvatar())
                    .resize(100,100)
                    .centerCrop()
                    .into(avatar);
        }
    }

    public void setData(ArrayList<User> items) {
        Data.clear();
        Data.addAll(items);
        notifyDataSetChanged();
    }
}
