package com.example.submission3;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.submission3.Database.DAO;
import com.example.submission3.Database.Database;
import com.example.submission3.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private final ArrayList<User> listUser = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<User> items) {
        listUser.clear();
        listUser.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        holder.setItem(listUser.get(position));
        holder.itemView.setOnClickListener(view -> onItemClickCallback.onItemClicked(listUser.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_fav_name) TextView name;
        @BindView(R.id.tv_fav_username) TextView username;
        @BindView(R.id.img_fav_avatar) ImageView avatar;
        @BindView(R.id.btn_fav_delete) Button delete;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setItem(User user) {
            name.setText(user.getName());
            username.setText(user.getUsername());
            Picasso.get()
                    .load(user.getAvatar())
                    .resize(100, 100)
                    .centerCrop()
                    .into(avatar);

            delete.setOnClickListener(view -> {
                final AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                alert.setTitle(R.string.delete);
                alert.setMessage(R.string.delete_keterangan);
                alert.setCancelable(false);
                alert.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    DAO DAO = Room.databaseBuilder(itemView.getContext(), Database.class, "user")
                            .allowMainThreadQueries()
                            .build()
                            .DAO();

                    DAO.deleteByName(user.getUserId());

                    listUser.remove(user);
                    notifyDataSetChanged();

                    Snackbar.make(view, R.string.delete_from_favorite, Snackbar.LENGTH_SHORT).show();
                });
                alert.setNegativeButton(R.string.no, ((dialogInterface, i) -> alert.setCancelable(true)));
                alert.show();
            });
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}
