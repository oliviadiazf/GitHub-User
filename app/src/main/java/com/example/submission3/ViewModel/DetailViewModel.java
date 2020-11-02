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

public class DetailViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    public void fetchFollow(final String name, String query) {
        final ArrayList<User> listItems = new ArrayList<>();

        String url = "https://api.github.com/users/" + name + "/" + query;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token xxx");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONArray list = new JSONArray(response);

                    for (int i=0; i<list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        User user = new User();
                        user.setName(object.getString("login"));
                        user.setAvatar(object.getString("avatar_url"));
                        user.setUsername(object.getString("html_url"));

                        listItems.add(user);
                    }

                    listUser.postValue(listItems);
                } catch (Exception exception) {
                    Log.d("[Exception] : ", Objects.requireNonNull(exception.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("[onFailure] : ", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<User>> getUsers() {
        return listUser;
    }
}
