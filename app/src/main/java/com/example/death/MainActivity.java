package com.example.death;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private int wins = 0;
    private int games = 0;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.death.gamesSharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mPreferences = getSharedPreferences(this.sharedPrefFile, MODE_PRIVATE);

        this.wins = this.mPreferences.getInt("wins", 0);
        this.games = this.mPreferences.getInt("games", 0);
        this.displayWins();

        final Button submitButton = (Button) findViewById(R.id.submit_age);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ageInput = (EditText) findViewById(R.id.enter_age);
                String ageString = ageInput.getText().toString();

                EditText maxGuessInput = (EditText) findViewById(R.id.enter_max_guesses);
                String maxGuessString = maxGuessInput.getText().toString();

                Toast properValues = Toast.makeText(getApplicationContext(), "Enter Proper, Positive, Integral Values", Toast.LENGTH_SHORT);

                if(ageString.equals("") || maxGuessString.equals("")) {
                    properValues.show();
                } else {
                    Integer age = Integer.parseInt(ageString);
                    Integer maxGuesses = Integer.parseInt(maxGuessString);
                    if(!(age > 0 && age < 100 && maxGuesses > 0)) {
                        properValues.show();
                    } else {
                        Intent submitGuess = new Intent(getApplicationContext(), Guess.class);
                        submitGuess.putExtra("age", age);
                        submitGuess.putExtra("maxGuesses", maxGuesses);
                        startActivityForResult(submitGuess, SECOND_ACTIVITY_REQUEST_CODE);
                    }
                }
            }
        });

        final Button resetButton = (Button) findViewById(R.id.reset_wins);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wins = 0;
                games = 0;
                displayWins();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                this.wins += data.getIntExtra("result", 0);
                this.games += 1;
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
        preferencesEditor.apply();
    }

    private void displayWins() {
        TextView winsTV = (TextView) findViewById(R.id.wins_tv);
        TextView lossesTV = (TextView) findViewById(R.id.losses_tv);
        winsTV.setText("Wins: " + this.wins);
        lossesTV.setText("Losses: " + (this.games - this.wins));
    }
}
