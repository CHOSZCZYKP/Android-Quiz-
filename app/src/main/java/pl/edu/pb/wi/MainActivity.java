package pl.edu.pb.wi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String Quiz_Tag = "MainActivity";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown;

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;

    private int currentIndex = 0;
    private int countCorrectAnswer = 0;
    private boolean quizFinish = false;

    private Button podpowiedzButton;

    private Question[] questions = new Question[] {
            new Question(R.string.q_activity,  true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false)
    };

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTureAnswer();
        int resultMessageId = 0;
        if (answerWasShown)
        {
            resultMessageId = R.string.answer_was_shown;
        }
        else
        {
            if (userAnswer == correctAnswer){
                resultMessageId = R.string.correct_answer;
                countCorrectAnswer++;
            }else {
                resultMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion()
    {
        if (quizFinish)
        {
            quizFinish = true;
            String resultMessage = "Poprawne odpowiedzi: " + countCorrectAnswer + " na " + questions.length;
            Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();
            currentIndex = 0;
            countCorrectAnswer = 0;
            quizFinish = false;
            setNextQuestion();
        }
        else
        {
            questionTextView.setText(questions[currentIndex].getQuestionId());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onCreate");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        podpowiedzButton = findViewById(R.id.podpowiedz_button);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quizFinish) {
                    checkAnswerCorrectness(true);
                }
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quizFinish) {
                    checkAnswerCorrectness(false);
                }
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerWasShown = false;
                if (!quizFinish) {
                    currentIndex++;
                    if (currentIndex >= questions.length){
                        quizFinish = true;
                    }
                    trueButton.setEnabled(true);
                    falseButton.setEnabled(true);
                    setNextQuestion();
                }
            }
        });
        podpowiedzButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTureAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });
        setNextQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Quiz_Tag, "Wywolana zostala metoda cyklu zycia: onDestroy");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Quiz_Tag, "Wywolana zostala metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) {
                return;
            }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}