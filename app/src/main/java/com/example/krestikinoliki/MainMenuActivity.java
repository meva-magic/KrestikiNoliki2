package com.example.krestikinoliki;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SoundManager soundManager;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main_menu);

        soundManager = SoundManager.getInstance(this);
        prefs = getSharedPreferences("game_settings", MODE_PRIVATE);

        titleText = findViewById(R.id.titleText);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnStatistics = findViewById(R.id.btnStatistics);
        Button btnExit = findViewById(R.id.btnExit);

        // Обновляем текст заголовка
        updateTitleText();

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playClickSound();
                Intent intent = new Intent(MainMenuActivity.this, AddPlayers.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playClickSound();
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playClickSound();
                Intent intent = new Intent(MainMenuActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playClickSound();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // При возврате в главное меню перезагружаем язык
        loadLanguage();
        updateTitleText();
        if (soundManager != null) {
            soundManager.updateSettings();
            soundManager.startBackgroundMusic();
        }
    }

    private void updateTitleText() {
        if (titleText != null) {
            titleText.setText(getString(R.string.app_name_full));
        }
    }

    private void loadLanguage() {
        SharedPreferences prefs = getSharedPreferences("game_settings", MODE_PRIVATE);
        String languageCode = prefs.getString("language", "ru");
        setLocale(languageCode);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundManager != null) {
            soundManager.pauseBackgroundMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.stopBackgroundMusic();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            soundManager.playClickSound();
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_statistics) {
            soundManager.playClickSound();
            startActivity(new Intent(this, StatisticsActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.about);
            builder.setMessage(R.string.about_text);
            builder.setPositiveButton("OK", null);
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}