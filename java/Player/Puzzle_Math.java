package com.example.geotreasure.Player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geotreasure.Connectives;
import com.example.geotreasure.Database.DatabaseHandler;
import com.example.geotreasure.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Puzzle_Math extends AppCompatActivity implements View.OnClickListener {

    TextView mathQuizTextView;
    TextView mathQuestionTextView;
    TextView incorrectAnswer;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;
    DatabaseHandler a = new DatabaseHandler(getApplicationContext());
    FirebaseUser user;

    String cache;
    int totalQuestions = Math_Questions.questions.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    String incorrect;

    @Override
    public void onClick(View view) {

        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;
        if (clickedButton.getId() == R.id.submit_btn) {
            if (selectedAnswer.equals(Math_Questions.correctAnswers[currentQuestionIndex])) {
                incorrectAnswer.setText("");
                currentQuestionIndex++;
//                loadNewQuestion();
                a.addPickedCache(user.getUid(), cache);
                Intent intent = new Intent(getApplicationContext(), Player_Home.class);
                startActivity(intent);
            } else {
                incorrectAnswer.setText(incorrect);
            }

        } else {
            // choices button clicked
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.rgb(211, 200, 250));

        }

    }

    void loadNewQuestion() {

        if (currentQuestionIndex == totalQuestions) {
            startActivity(new Intent(getApplicationContext(), Player_Home.class));
        }

        mathQuestionTextView.setText(Math_Questions.questions[currentQuestionIndex]);
        ansA.setText(Math_Questions.answers[currentQuestionIndex][0]);
        ansB.setText(Math_Questions.answers[currentQuestionIndex][1]);
        ansC.setText(Math_Questions.answers[currentQuestionIndex][2]);
        ansD.setText(Math_Questions.answers[currentQuestionIndex][3]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_math);
        Intent intent = getIntent();
        this.cache = intent.getStringExtra("cache");
        user = FirebaseAuth.getInstance().getCurrentUser();
        incorrectAnswer = findViewById(R.id.incorrect);
        mathQuizTextView = findViewById(R.id.math);
        mathQuestionTextView = findViewById(R.id.math_question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        incorrect = "Incorrect answer. Try again.";

        //internet connectivity
        Connectives connectionDetector = new Connectives(this);
        if (!connectionDetector.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet connection");
            builder.setMessage("Please check your internet connection and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        loadNewQuestion();

    }
}