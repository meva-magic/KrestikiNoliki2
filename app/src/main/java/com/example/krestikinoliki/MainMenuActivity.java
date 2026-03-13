package com.example.krestikinoliki;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends BaseActivity {

    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        titleText = findViewById(R.id.titleText);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnStatistics = findViewById(R.id.btnStatistics);
        Button btnExit = findViewById(R.id.btnExit);

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

    private void updateTitleText() {
        if (titleText != null) {
            titleText.setText(getString(R.string.app_name_full));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTitleText();
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