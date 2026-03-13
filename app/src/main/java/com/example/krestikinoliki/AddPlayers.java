package com.example.krestikinoliki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPlayers extends BaseActivity {

    private EditText playerOne, playerTwo;
    private Button startGameButton, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        startGameButton = findViewById(R.id.startGameButton);
        btnBack = findViewById(R.id.btnBack);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundManager.playClickSound();
                String getPlayerOneName = playerOne.getText().toString().trim();
                String getPlayerTwoName = playerTwo.getText().toString().trim();

                if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                    ToastHelper.showCustomToast(AddPlayers.this,
                            getString(R.string.enter_names));
                } else {
                    Intent intent = new Intent(AddPlayers.this, MainActivity.class);
                    intent.putExtra("playerOne", getPlayerOneName);
                    intent.putExtra("playerTwo", getPlayerTwoName);
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.playClickSound();
                finish();
            }
        });
    }
}