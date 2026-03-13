package com.example.krestikinoliki;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    protected SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        soundManager = SoundManager.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (soundManager != null) {
            soundManager.onActivityStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (soundManager != null) {
            soundManager.onActivityStop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (soundManager != null) {
            soundManager.updateSettings();
        }
    }

    protected void loadLanguage() {
        SharedPreferences prefs = getSharedPreferences("game_settings", MODE_PRIVATE);
        String languageCode = prefs.getString("language", "ru");
        setLocale(languageCode);
    }

    protected void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}