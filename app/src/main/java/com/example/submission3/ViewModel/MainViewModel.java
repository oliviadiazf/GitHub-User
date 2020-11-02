package com.example.submission3.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission3.Model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listUsers = new MutableLiveData<>();

    public void fetchUser(final String name) {
        final ArrayList<User> listUser = new ArrayList<>();

        String url = "https://api.github.com/search/users?q=" + name;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token xxx");
        client.addHeader("User-Agent", "request");

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray items = responseObject.getJSONArray("items");

                    for (int i=0; i<items.length(); i++) {
                        JSONObject object = items.getJSONObject(i);
                        User user = new User();
                        user.setName(object.getString("login"));
                        user.setAvatar(object.getString("avatar_url"));
                        user.setUsername(object.getString("html_url"));

                        listUser.add(user);
                    }

                    listUsers.postValue(listUser);
                } catch (Exception exception) {
                    Log.d("[Exception] : ", Objects.requireNonNull(exception.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("[onFailure]", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<User>> getUsers() {
        return listUsers;
    }
}
