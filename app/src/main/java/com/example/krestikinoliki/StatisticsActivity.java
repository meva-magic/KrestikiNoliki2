package com.example.krestikinoliki;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {

    private TextView statsXWins, statsOWins, statsDraws;
    private Button btnReset, btnShare, btnBack;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        prefs = getSharedPreferences("game_stats", MODE_PRIVATE);

        statsXWins = findViewById(R.id.statsXWins);
        statsOWins = findViewById(R.id.statsOWins);
        statsDraws = findViewById(R.id.statsDraws);
        btnReset = findViewById(R.id.btnResetStats);
        btnShare = findViewById(R.id.btnShare);
        btnBack = findViewById(R.id.btnBack);

        loadStatistics();

        btnReset.setOnClickListener(v -> {
            SoundManager.getInstance().playClickSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsActivity.this);
            builder.setTitle(R.string.reset_stats);
            builder.setMessage(R.string.reset_confirm);

            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                resetStatistics();
                ToastHelper.showCustomToast(StatisticsActivity.this,
                        getString(R.string.reset_stats));
            });

            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        btnShare.setOnClickListener(v -> {
            SoundManager.getInstance().playClickSound();
            shareStatistics();
        });

        btnBack.setOnClickListener(v -> {
            SoundManager.getInstance().playClickSound();
            finish();
        });
    }

    private void loadStatistics() {
        int xWins = prefs.getInt("x_wins", 0);
        int oWins = prefs.getInt("o_wins", 0);
        int draws = prefs.getInt("draws", 0);

        statsXWins.setText(getString(R.string.x_wins_default).replace("0", String.valueOf(xWins)));
        statsOWins.setText(getString(R.string.o_wins_default).replace("0", String.valueOf(oWins)));
        statsDraws.setText(getString(R.string.draws_default).replace("0", String.valueOf(draws)));
    }

    private void resetStatistics() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("x_wins", 0);
        editor.putInt("o_wins", 0);
        editor.putInt("draws", 0);
        editor.apply();
        loadStatistics();
    }

    private void shareStatistics() {
        int xWins = prefs.getInt("x_wins", 0);
        int oWins = prefs.getInt("o_wins", 0);
        int draws = prefs.getInt("draws", 0);

        String shareText = String.format(getString(R.string.share_text), xWins, oWins, draws);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
    }

    public static void updateStatistics(Context context, String winner) {
        SharedPreferences prefs = context.getSharedPreferences("game_stats", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (winner.equals("X")) {
            int xWins = prefs.getInt("x_wins", 0);
            editor.putInt("x_wins", xWins + 1);
        } else if (winner.equals("O")) {
            int oWins = prefs.getInt("o_wins", 0);
            editor.putInt("o_wins", oWins + 1);
        } else if (winner.equals("draw")) {
            int draws = prefs.getInt("draws", 0);
            editor.putInt("draws", draws + 1);
        }
        editor.apply();
    }
}