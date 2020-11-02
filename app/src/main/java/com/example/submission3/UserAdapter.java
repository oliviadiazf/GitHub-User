package com.example.submission3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.submission3.Model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final ArrayList<User> Data = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<User> items) {
        Data.clear();
        Data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserViewHolder holder, int position) {
        holder.setItem(Data.get(position));
        holder.itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(Data.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_avatar) ImageView avatar;
        @BindView(R.id.tv_name) TextView name;
        @BindView(R.id.tv_username) TextView username;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(User user) {
            Picasso.get()
                    .load(user.getAvatar())
                    .resize(100,100)
                    .centerCrop()
                    .into(avatar);
            name.setText(user.getName());
            username.setText(user.getUsername());
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
}
