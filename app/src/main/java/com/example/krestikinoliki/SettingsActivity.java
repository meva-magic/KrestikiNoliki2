package com.example.krestikinoliki;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Switch soundSwitch;
    private RadioGroup radioGroupLanguage;
    private RadioButton radioLangRu, radioLangEn;
    private SeekBar volumeSeekBar;
    private Button btnSave;
    private SharedPreferences prefs;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("game_settings", MODE_PRIVATE);
        soundManager = SoundManager.getInstance(this);

        soundSwitch = findViewById(R.id.settingsSoundSwitch);
        radioGroupLanguage = findViewById(R.id.radioGroupLanguage);
        radioLangRu = findViewById(R.id.radioLangRu);
        radioLangEn = findViewById(R.id.radioLangEn);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        btnSave = findViewById(R.id.btnSaveSettings);

        loadSettings();

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(SettingsActivity.this,
                        isChecked ? getString(R.string.sound) + " on" : getString(R.string.sound) + " off", Toast.LENGTH_SHORT).show());

        // Язык меняется сразу при выборе
        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            String languageCode = (checkedId == R.id.radioLangRu) ? "ru" : "en";
            setLocale(languageCode);
            recreate(); // Пересоздаем активность для применения языка
        });

        int currentVolume = (int)(prefs.getFloat("music_volume", 0.5f) * 100);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                soundManager.setMusicVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(SettingsActivity.this,
                    getString(R.string.save), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadSettings() {
        soundSwitch.setChecked(prefs.getBoolean("sound_enabled", true));

        String language = prefs.getString("language", "ru");
        if (language.equals("ru")) {
            radioLangRu.setChecked(true);
        } else {
            radioLangEn.setChecked(true);
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sound_enabled", soundSwitch.isChecked());

        int selectedLangId = radioGroupLanguage.getCheckedRadioButtonId();
        editor.putString("language", (selectedLangId == R.id.radioLangRu) ? "ru" : "en");

        editor.putFloat("music_volume", volumeSeekBar.getProgress() / 100f);
        editor.apply();

        if (soundManager != null) {
            soundManager.updateSettings();
        }
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Сохраняем выбранный язык
        prefs.edit().putString("language", languageCode).apply();
    }
}