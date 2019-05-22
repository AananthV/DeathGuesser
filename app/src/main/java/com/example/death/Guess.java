package com.example.death;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Guess extends AppCompatActivity {
    private int age = 0;
    private int guessesLeft = -1;
    private int lastGuess = -1;
    private int sliderGuess = 0;
    private int orientation = 1;
    private TextView showGuess;
    private float RED[] = {0.0f, 0.8470f, 0.7176f};
    private float GREEN[] = {122.4f, 0.566f, 0.686f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        Intent intent = getIntent();
        this.age = intent.getIntExtra("age", -1);
        this.guessesLeft = intent.getIntExtra("maxGuesses", -1);

        if(savedInstanceState != null) {
            this.lastGuess = savedInstanceState.getInt("lastGuess", -1);
            this.guessesLeft = savedInstanceState.getInt("guessesLeft", -1);
        }

        this.orientation = this.getResources().getConfiguration().orientation;

        // Set up SeekBar Change Listener if Orientation is Landscape
        if(this.orientation == 2) {
            this.showGuess = (TextView) findViewById(R.id.display_slider_guess);
            SeekBar seekBar = (SeekBar) findViewById(R.id.guess_age_slider);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    sliderGuess = progress;
                    showGuess.setText(String.valueOf(sliderGuess));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            if(this.lastGuess != -1) {
                seekBar.setProgress(this.lastGuess);
            }
        }

        if(this.lastGuess != -1 && this.guessesLeft != -1) {
            TextView showGuessTemp = (TextView) findViewById(R.id.last_guess_tv);
            showGuessTemp.setText("Last Guess: " + this.lastGuess);

            TextView displayGuesses = (TextView) findViewById(R.id.guesses_left_tv);
            displayGuesses.setText("Guesses Left: " + this.guessesLeft);

            setBackgroundColor(this.lastGuess);
        }

        Button submitButton = (Button) findViewById(R.id.submit_guess);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast properValues = Toast.makeText(getApplicationContext(), "Enter Proper, Positive, Integral Values between 0 and 100", Toast.LENGTH_SHORT);

                int guess = -1;

                // Portrait
                if(orientation == 1){
                    EditText guessInput = (EditText) findViewById(R.id.guess_age_text);
                    String guessString = guessInput.getText().toString();
                    if(guessString.equals("")) {
                        properValues.show();
                    } else {
                        guess = Integer.parseInt(guessString);
                        guessInput.setText("");
                    }
                }

                // Landscape
                if(orientation == 2) {
                    guess = sliderGuess;
                }

                if(guessesLeft > 0) {
                    if (guess >= 0 && guess <= 100) {
                        setBackgroundColor(guess);
                        TextView showGuess = (TextView) findViewById(R.id.last_guess_tv);
                        showGuess.setText("Last Guess: " + guess);
                        lastGuess = guess;
                        guess();
                    } else {
                        properValues.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You ran out of guesses :(", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastGuess", this.lastGuess);
        outState.putInt("guessesLeft", this.guessesLeft);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.lastGuess = savedInstanceState.getInt("lastGuess", -1);
        this.guessesLeft = savedInstanceState.getInt("guessesLeft", -1);
    }

    /*
        Function name: setBackgroundColor()
            Sets the Background Color based on the relative closeness of a guess.
            Uses Linear Interpolation to find the gradient color.
     */
    private void setBackgroundColor(int guess) {
        ScrollView guessView = (ScrollView) findViewById(R.id.guess_view);
        float guessError =  1f*Math.abs(guess - this.age)/(Math.max(100-this.age, this.age));

        float color[] = new float[3];
        for(int i = 0; i < 3; i++) {
            color[i] = this.RED[i] + (this.GREEN[i] - this.RED[i]) * (1 - guessError);
        }

        guessView.setBackgroundColor(Color.HSVToColor(color));
    }

    private void guess() {
        if(this.lastGuess == this.age) {
            Toast.makeText(getApplicationContext(), "You Won!", Toast.LENGTH_LONG).show();
            Intent result = new Intent();
            result.putExtra("result", 1);
            setResult(RESULT_OK, result);
            finish();
        } else if(--this.guessesLeft <= 0) {
            Toast.makeText(getApplicationContext(), "You lost.", Toast.LENGTH_LONG).show();
            Intent result = new Intent();
            result.putExtra("result", 0);
            setResult(RESULT_OK, result);
            finish();
        } else if(this.lastGuess < this.age) {
            Toast.makeText(getApplicationContext(), "Try something higher.", Toast.LENGTH_SHORT).show();

        } else if(this.lastGuess > this.age) {
            Toast.makeText(getApplicationContext(), "Try something lower.", Toast.LENGTH_SHORT).show();

        }
        TextView displayGuesses = (TextView) findViewById(R.id.guesses_left_tv);
        displayGuesses.setText("Guesses Left: " + this.guessesLeft);
    }
}
