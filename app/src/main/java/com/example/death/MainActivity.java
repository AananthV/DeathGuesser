package com.example.death;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        startActivity(submitGuess);
                    }
                }
            }
        });
    }
}
