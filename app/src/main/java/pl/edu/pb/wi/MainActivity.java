package pl.edu.pb.wi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private TextView questionTextView;

    private int currentIndex = 0;
    private int countCorrectAnswer = 0;
    private boolean quizFinish = false;

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
        if (userAnswer == correctAnswer){
            resultMessageId = R.string.correct_answer;
            countCorrectAnswer++;
        }else {
            resultMessageId = R.string.incorrect_answer;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);

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
        setNextQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}