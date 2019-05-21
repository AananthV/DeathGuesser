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
    private Integer age;
    private Integer lastGuess = -1;
    private Integer sliderGuess;
    private Integer orientation;
    private TextView showGuess;
    private Float RED[] = {0.0f, 0.8470f, 0.7176f};
    private Float GREEN[] = {122.436f, 0.5657f, 0.6863f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        Intent intent = getIntent();
        this.age = intent.getIntExtra("age", -1);

        if(savedInstanceState != null) {
            this.lastGuess = savedInstanceState.getInt("lastGuess", -1);
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
                    showGuess.setText(sliderGuess.toString());
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

        if(this.lastGuess != -1) {
            TextView showGuessTemp = (TextView) findViewById(R.id.temp);
            showGuessTemp.setText("Last guess = " + this.lastGuess.toString());
            setBackgroundColor(this.lastGuess);
        }

        Button submitButton = (Button) findViewById(R.id.submit_guess);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast properValues = Toast.makeText(getApplicationContext(), "Enter Proper, Positive, Integral Values between 0 and 100", Toast.LENGTH_SHORT);

                Integer guess = -1;

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

                if(guess >= 0 && guess <= 100) {
                    setBackgroundColor(guess);
                    TextView showGuess = (TextView) findViewById(R.id.temp);
                    showGuess.setText("Last Guess: " + guess.toString());
                    lastGuess = guess;
                } else {
                    properValues.show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastGuess", this.lastGuess);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.lastGuess = savedInstanceState.getInt("lastGuess", -1);
    }

    /*
        Function name: setBackgroundColor()
            Sets the Background Color based on the relative closeness of a guess.
            Uses Linear Interpolation to find the gradient color.
     */
    private void setBackgroundColor(Integer guess) {
        ScrollView guessView = (ScrollView) findViewById(R.id.guess_view);

        Float guessError =  1f*Math.abs(guess - this.age)/(Math.max(100-this.age, this.age));

        float color[] = new float[3];
        for(Integer i = 0; i < 3; i++) {
            color[i] = this.RED[i] + (this.GREEN[i] - this.RED[i]) * (1 - guessError);
        }

        Integer colorRGB = Color.HSVToColor(color);

        guessView.setBackgroundColor(colorRGB);
    }

}
