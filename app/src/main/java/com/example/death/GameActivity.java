package com.example.death;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {
    private int wins = 0;
    private int games = 0;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.death.gamesSharedPrefs";
    private int colorSet = 0; // 0 - Not Set, 1 - RED, 2 -  GREEN
    private float RED[] = {0.0f, 0.8470f, 0.7176f};
    private float GREEN[] = {122.4f, 0.566f, 0.686f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.mPreferences = getSharedPreferences(this.sharedPrefFile, MODE_PRIVATE);

        this.wins = this.mPreferences.getInt("wins", 0);
        this.games = this.mPreferences.getInt("games", 0);
        this.colorSet = this.mPreferences.getInt("colorSet", 0);
        this.displayWins();

        final Button submitButton = (Button) findViewById(R.id.submit_age);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age = -1, maxGuesses = -1;
                EditText ageInput = (EditText) findViewById(R.id.enter_age);
                String ageString = ageInput.getText().toString();

                EditText maxGuessInput = (EditText) findViewById(R.id.enter_max_guesses);
                String maxGuessString = maxGuessInput.getText().toString();

                CheckBox useBinarySearch = (CheckBox) findViewById(R.id.binarySearch);

                Toast properValues = Toast.makeText(getApplicationContext(), "Enter Proper, Positive, Integral Values", Toast.LENGTH_SHORT);

                if(ageString.equals("")) {
                    properValues.show();
                } else {
                    age = Integer.parseInt(ageString);
                    if(!(age >= 0 && age <= 100)) {
                        properValues.show();
                    } else {
                        if(useBinarySearch.isChecked()) {
                            maxGuesses = findOptimalGuesses(age);
                        } else {
                            if(maxGuessString.equals("")) {
                                properValues.show();
                            } else {
                                maxGuesses = Integer.parseInt(maxGuessString);
                            }
                        }
                    }
                }
                if(age > 0 && maxGuesses > 0) {
                    Intent submitGuess = new Intent(getApplicationContext(), Guess.class);
                    submitGuess.putExtra("age", age);
                    submitGuess.putExtra("maxGuesses", maxGuesses);
                    startActivityForResult(submitGuess, SECOND_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        final Button resetButton = (Button) findViewById(R.id.reset_wins);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wins = 0;
                games = 0;
                colorSet = 0;
                displayWins();
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                int result = data.getIntExtra("result", 0);
                this.wins += result;
                this.games += 1;
                this.colorSet = result + 1;
                this.displayWins();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("wins", this.wins);
        preferencesEditor.putInt("games", this.games);
        preferencesEditor.putInt("colorSet", this.colorSet);
        preferencesEditor.apply();
    }

    private void displayWins() {
        TextView winsTV = (TextView) findViewById(R.id.wins_tv);
        TextView lossesTV = (TextView) findViewById(R.id.losses_tv);
        winsTV.setText("" + this.wins);
        lossesTV.setText("" + (this.games - this.wins));

        if(this.colorSet == 1) {
            findViewById(R.id.main_view).setBackgroundColor(Color.HSVToColor(this.RED));
        } else if(this.colorSet == 2) {
            findViewById(R.id.main_view).setBackgroundColor(Color.HSVToColor(this.GREEN));
        } else {
            findViewById(R.id.main_view).setBackgroundColor(Color.parseColor("#303030"));
        }
    }

    /*
        Uses binary search to find optimal number of guesses
     */
    private int findOptimalGuesses(int guess) {
        int min = 0, max = 100, current = 50, guesses = 1;
        while(current != guess) {
            if(current < guess) {
                min = current;
            } else {
                max = current;
            }
            current = (min+max)/2;
            guesses++;
        }
        return guesses;
    }
}
