package com.example.submission3;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.submission3.Database.DAO;
import com.example.submission3.Database.Database;
import com.example.submission3.Model.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_USER = "extra_user";
    private String name;


    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean isAvatarShown = true;
    private int maxScrollSize;

    @BindView(R.id.img_detail) ImageView avatar;
    @BindView(R.id.tv_detail_nama) TextView detailName;
    @BindView(R.id.tv_detail_username) TextView username;
    @BindView(R.id.tv_detail_company) TextView company;
    @BindView(R.id.tv_detail_location) TextView location;
    @BindView(R.id.tv_detail_link) TextView link;
    @BindView(R.id.btn_detail) Button button;
    @BindView(R.id.btn_detail2) Button button2;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.detail_progressBar) ProgressBar progressBar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.appBar) AppBarLayout appBarLayout;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        appBarLayout.addOnOffsetChangedListener(this);
        maxScrollSize = appBarLayout.getTotalScrollRange();

        viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager(), name));
        tabLayout.setupWithViewPager(viewPager);

//        DetailPageAdapter detailPageAdapter = new DetailPageAdapter(this, getSupportFragmentManager(), user);
//        viewPager.setAdapter(detailPageAdapter);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        if (user != null) {
            name = user.getName();
            detailName.setText(user.getName());
        }

        viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager(), name));
        tabLayout.setupWithViewPager(viewPager);

        //Objects.requireNonNull(getSupportActionBar()).setTitle(name);

        showProgressBar(true);
        setDetail(name);

        DAO DAO = Room.databaseBuilder(this, Database.class, "user")
                .allowMainThreadQueries()
                .build()
                .DAO();

        User check = DAO.getUserByName(name);
        if (check != null) {
            button.setVisibility(View.GONE);
        } else {
            button.setOnClickListener(view -> {
                DAO.insertAll(user);
                Toast.makeText(this, R.string.successfully_add_to_favorite, Toast.LENGTH_SHORT).show();
                button.setVisibility(View.GONE);
            });
        }
    }

    private void setDetail(String name) {
        String url = "https://api.github.com/users/" + name;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token ee7ebc7b19e4d325136746a82484f5e3a0554d2c");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);

                    detailName.setText(responseObject.getString("name"));
                    Picasso.get()
                            .load(responseObject.getString("avatar_url"))
                            .resize(200,200)
                            .centerCrop()
                            .into(avatar);

                    if (responseObject.getString("name").equals("null")) detailName.setVisibility(View.GONE);
                    else detailName.setText(responseObject.getString("name"));

                    if (responseObject.getString("login").equals("null")) username.setVisibility(View.GONE);
                    else username.setText(responseObject.getString("login"));

                    if (responseObject.getString("company").equals("null")) company.setVisibility(View.GONE);
                    else company.setText(responseObject.getString("company"));

                    if (responseObject.getString("location").equals("null")) location.setVisibility(View.GONE);
                    else location.setText(responseObject.getString("location"));

                    if (responseObject.getString("blog").equals("null")) link.setVisibility(View.GONE);
                    else link.setText(responseObject.getString("blog"));

                    showProgressBar(false);
                } catch (Exception exception) {
                    Log.d("[Exception] : ", Objects.requireNonNull(exception.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("[Exception] : ", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    private void showProgressBar(boolean b) {
        if (b) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

//        maxScrollSize=1;

        int percentage = (Math.abs(verticalOffset)) * 100 / (maxScrollSize+1);

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;

            avatar.animate()
                    .scaleX(1).scaleY(1)
                    .start();
        }

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;

            avatar.animate()
                    .scaleX(0).scaleY(0)
                    .setDuration(200)
                    .start();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;
        private final String name;
        private final Context mContext;

        SectionsPagerAdapter(Context context, FragmentManager supportFragmentManager, String name) {
            super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mContext = context;
            this.name = name;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @StringRes
        private final int[] TAB_TITLES = new int[] {
                R.string.tab_follower,
                R.string.tab_following
        };

        @NonNull
        @Override
        public Fragment getItem(int i) {
            Bundle data = new Bundle();
            data.putString("name", name);

            if (i == 0) data.putString("query", "followers");
            else data.putString("query", "following");

            FollowFragment followFragment = new FollowFragment();
            followFragment.setArguments(data);

            return followFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mContext.getResources().getString(TAB_TITLES[position]);
        }
    }
}