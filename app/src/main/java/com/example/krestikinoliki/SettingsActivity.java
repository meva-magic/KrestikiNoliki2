package com.example.krestikinoliki;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {

    private Switch soundSwitch;
    private RadioGroup radioGroupLanguage;
    private RadioButton radioLangRu, radioLangEn;
    private SeekBar volumeSeekBar;
    private Button btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("game_settings", MODE_PRIVATE);

        soundSwitch = findViewById(R.id.settingsSoundSwitch);
        radioGroupLanguage = findViewById(R.id.radioGroupLanguage);
        radioLangRu = findViewById(R.id.radioLangRu);
        radioLangEn = findViewById(R.id.radioLangEn);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        btnSave = findViewById(R.id.btnSaveSettings);

        loadSettings();

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Сохраняем и применяем изменения сразу
            prefs.edit().putBoolean("sound_enabled", isChecked).apply();
            soundManager.updateSettings();
            Toast.makeText(SettingsActivity.this,
                    isChecked ? getString(R.string.sound) + " on" : getString(R.string.sound) + " off",
                    Toast.LENGTH_SHORT).show();
        });

        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            String languageCode = (checkedId == R.id.radioLangRu) ? "ru" : "en";
            prefs.edit().putString("language", languageCode).apply();
            setLocale(languageCode);
            recreate();
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                prefs.edit().putFloat("music_volume", volume).apply();
                soundManager.setMusicVolume(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            soundManager.playClickSound();
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

        int volume = (int)(prefs.getFloat("music_volume", 0.5f) * 100);
        volumeSeekBar.setProgress(volume);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sound_enabled", soundSwitch.isChecked());

        int selectedLangId = radioGroupLanguage.getCheckedRadioButtonId();
        editor.putString("language", (selectedLangId == R.id.radioLangRu) ? "ru" : "en");

        editor.putFloat("music_volume", volumeSeekBar.getProgress() / 100f);
        editor.apply();

        soundManager.updateSettings();
    }
}