package com.example.consumerapp;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private Cursor aCursor;

    void setData(Cursor cursor) {
        aCursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_consumerapp, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.setItem(aCursor.moveToPosition(position));
    }

    @Override
    public int getItemCount() {
        return aCursor == null ? 0 : aCursor.getCount();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_favca_name) TextView name;
        @BindView(R.id.tv_favca_username) TextView username;
        @BindView(R.id.img_favca_avatar) ImageView avatar;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void setItem(boolean moveToPosition) {
            if (moveToPosition) {
                name.setText(aCursor.getString(aCursor.getColumnIndexOrThrow(MainActivity.COLUMN_NAME)));
                username.setText(aCursor.getString(aCursor.getColumnIndexOrThrow(MainActivity.COLUMN_USERNAME)));
                Picasso.get()
                        .load(aCursor.getString(aCursor.getColumnIndexOrThrow(MainActivity.COLUMN_AVATAR)))
                        .resize(100,100)
                        .centerCrop()
                        .into(avatar);
            }
        }
    }
}
