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
//Trivia puzzle generation
public class Puzzle_Trivia extends AppCompatActivity implements View.OnClickListener {

    TextView triviaQuizTextView;
    TextView questionTextView;
    TextView incorrectAnswer;
    Button ansA, ansB, ansC, ansD;
    Button submitBtn;

    DatabaseHandler a;
    FirebaseUser user;
    String cache;

    int score = 0;
    int totalQuestions = Trivia_Questions.questions.length;
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
            if (selectedAnswer.equals(Trivia_Questions.correctAnswers[currentQuestionIndex])) {
                incorrectAnswer.setText("");
                currentQuestionIndex++;
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

        questionTextView.setText(Trivia_Questions.questions[currentQuestionIndex]);
        ansA.setText(Trivia_Questions.answers[currentQuestionIndex][0]);
        ansB.setText(Trivia_Questions.answers[currentQuestionIndex][1]);
        ansC.setText(Trivia_Questions.answers[currentQuestionIndex][2]);
        ansD.setText(Trivia_Questions.answers[currentQuestionIndex][3]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_trivia);
        Intent intent = getIntent();
        this.cache = intent.getStringExtra("cache");
        user = FirebaseAuth.getInstance().getCurrentUser();
        incorrectAnswer = findViewById(R.id.incorrect_trivia);
        triviaQuizTextView = findViewById(R.id.trivia);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);
        submitBtn = findViewById(R.id.submit_btn);

        a = new DatabaseHandler(getApplicationContext());

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