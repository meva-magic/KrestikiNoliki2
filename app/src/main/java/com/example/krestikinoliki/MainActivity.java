package com.example.krestikinoliki;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TextView playerOneName, playerTwoName;
    private LinearLayout playerOneLayout, playerTwoLayout;
    private ImageView playerOneIndicator, playerTwoIndicator;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private Button btnExitToMenu;

    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initCombinations();

        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");

        setPlayerNames(getPlayerOneName, getPlayerTwoName);
        setupExitButton();
        setClickListeners();
        updatePlayerTurnIndicator();
    }

    private void initUI() {
        playerOneName = findViewById(R.id.playerOneName);
        playerTwoName = findViewById(R.id.playerTwoName);
        playerOneLayout = findViewById(R.id.playerOneLayout);
        playerTwoLayout = findViewById(R.id.playerTwoLayout);
        playerOneIndicator = findViewById(R.id.playerOneIndicator);
        playerTwoIndicator = findViewById(R.id.playerTwoIndicator);
        btnExitToMenu = findViewById(R.id.btnExitToMenu);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        image9 = findViewById(R.id.image9);
    }

    private void initCombinations() {
        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});
    }

    private void setPlayerNames(String name1, String name2) {
        if (name1 != null && !name1.isEmpty()) {
            playerOneName.setText(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            playerTwoName.setText(name2);
        }
    }

    private void setupExitButton() {
        btnExitToMenu.setOnClickListener(v -> {
            soundManager.playClickSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.exit_to_menu);
            builder.setMessage(R.string.exit_confirm);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void setClickListeners() {
        image1.setOnClickListener(v -> {
            if (isBoxSelectable(0)) performAction(image1, 0);
        });
        image2.setOnClickListener(v -> {
            if (isBoxSelectable(1)) performAction(image2, 1);
        });
        image3.setOnClickListener(v -> {
            if (isBoxSelectable(2)) performAction(image3, 2);
        });
        image4.setOnClickListener(v -> {
            if (isBoxSelectable(3)) performAction(image4, 3);
        });
        image5.setOnClickListener(v -> {
            if (isBoxSelectable(4)) performAction(image5, 4);
        });
        image6.setOnClickListener(v -> {
            if (isBoxSelectable(5)) performAction(image6, 5);
        });
        image7.setOnClickListener(v -> {
            if (isBoxSelectable(6)) performAction(image7, 6);
        });
        image8.setOnClickListener(v -> {
            if (isBoxSelectable(7)) performAction(image8, 7);
        });
        image9.setOnClickListener(v -> {
            if (isBoxSelectable(8)) performAction(image9, 8);
        });
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        // Звук нажатия уже есть в SoundManager, но добавим и здесь для надежности
        soundManager.playClickSound();

        boxPositions[selectedBoxPosition] = playerTurn;

        String playerName = (playerTurn == 1) ?
                playerOneName.getText().toString() :
                playerTwoName.getText().toString();
        ToastHelper.showCustomToast(this, playerName + " " + getString(R.string.made_move));

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage);
            if (checkResults()) {
                StatisticsActivity.updateStatistics(MainActivity.this, "X");
                showWinnerDialog(playerOneName.getText().toString() + " " + getString(R.string.won));
            } else if (totalSelectedBoxes == 9) {
                StatisticsActivity.updateStatistics(MainActivity.this, "draw");
                showWinnerDialog(getString(R.string.draw));
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.oimage);
            if (checkResults()) {
                StatisticsActivity.updateStatistics(MainActivity.this, "O");
                showWinnerDialog(playerTwoName.getText().toString() + " " + getString(R.string.won));
            } else if (totalSelectedBoxes == 9) {
                StatisticsActivity.updateStatistics(MainActivity.this, "draw");
                showWinnerDialog(getString(R.string.draw));
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        updatePlayerTurnIndicator();
    }

    private void updatePlayerTurnIndicator() {
        if (playerTurn == 1) {
            playerOneLayout.setBackgroundResource(R.drawable.black_border_thick);
            playerTwoLayout.setBackgroundResource(R.drawable.white_box);
            playerOneIndicator.setAlpha(1.0f);
            playerTwoIndicator.setAlpha(0.5f);
        } else {
            playerTwoLayout.setBackgroundResource(R.drawable.black_border_thick);
            playerOneLayout.setBackgroundResource(R.drawable.white_box);
            playerOneIndicator.setAlpha(0.5f);
            playerTwoIndicator.setAlpha(1.0f);
        }
    }

    private void showWinnerDialog(String message) {
        ResultDialog resultDialog = new ResultDialog(MainActivity.this, message, this);
        resultDialog.setCancelable(false);
        resultDialog.show();
    }

    private boolean checkResults() {
        for (int[] combination : combinationList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    public void restartMatch() {
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0};
        playerTurn = 1;
        totalSelectedBoxes = 1;

        image1.setImageResource(R.drawable.white_box);
        image2.setImageResource(R.drawable.white_box);
        image3.setImageResource(R.drawable.white_box);
        image4.setImageResource(R.drawable.white_box);
        image5.setImageResource(R.drawable.white_box);
        image6.setImageResource(R.drawable.white_box);
        image7.setImageResource(R.drawable.white_box);
        image8.setImageResource(R.drawable.white_box);
        image9.setImageResource(R.drawable.white_box);

        updatePlayerTurnIndicator();
    }
}