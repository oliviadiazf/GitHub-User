package com.example.submission3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class SettingFragment extends Fragment implements View.OnClickListener {
    public static String DAILY;
    private static String SETTING_PREFS = "";
    private DailyReminderReceiver dailyReminderReceiver;
    private Switch mSwitchDaily;
    private boolean dailyCheck = false;

    public  SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_setting_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        Objects.requireNonNull(((Dashboard) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.setting);
        Objects.requireNonNull(((Dashboard) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ImageButton imageButton = view.findViewById(R.id.btn_setting_language);
        mSwitchDaily = view.findViewById(R.id.switch_daily);

        dailyReminderReceiver = new DailyReminderReceiver();

        getPreferences();

        mSwitchDaily.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dailyCheck = dailyCheck;
            setPreferences();
            if (isChecked) {
                dailyReminderReceiver.dailyReminderOn(getActivity());
                Toast.makeText(getActivity(), "Daily Reminder is Active", Toast.LENGTH_LONG).show();
            } else {
                dailyReminderReceiver.dailyReminderOff(getActivity());
                Toast.makeText(getActivity(), "Daily Reminder is Non Active", Toast.LENGTH_LONG).show();
            }
        });
        imageButton.setOnClickListener(this);
    }

    private void setPreferences() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DAILY, dailyCheck);
        editor.apply();
    }

    private void getPreferences() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        mSwitchDaily.setChecked(sharedPreferences.getBoolean(DAILY, false));
    }
}