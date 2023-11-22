package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.GemLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    //binding object variable
    TextView mMainDisplay;
    private EditText mExercise;
    private EditText mWeight;
    private EditText mReps;

    private Button mSubmit;

    private GemLogDAO mGemLogDAO;
    //entry point into database;DAO object
    private List<GemLog> mGemLogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button in mainactivity to xml file
        setContentView(binding.getRoot());

        mMainDisplay = binding.mainGemLogDisplay;
        mExercise = binding.mainExerciseEditText;
        mWeight = binding.mainWeightEditText;
        mReps = binding.mainRepsEditText;
        mSubmit = binding.mainSubmitButton;
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling

        mGemLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GemLogDAO();
        //allows main thread queries; not advisable for complicated queries because main thread can
        //halt and crash; allows access to database object
        refreshDisplay();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGemLog();
                refreshDisplay();
            }
        });
    }//end oncreate

    private void submitGemLog(){
        //this submits the widget entries to the DAO
        String exercise = mExercise.getText().toString();
        double weight = Double.parseDouble(mWeight.getText().toString());
        int reps = Integer.parseInt(mReps.getText().toString());
        GemLog log = new GemLog(exercise,weight,reps);
        mGemLogDAO.insert(log);
    }// end submitGemLog

    private void refreshDisplay(){
        mGemLogList = mGemLogDAO.getGemLogs();
        if( ! mGemLogList.isEmpty() ){
            StringBuilder sb = new StringBuilder();
            for(GemLog log : mGemLogList){
                sb.append(log.toString());
            }
            mMainDisplay.setText(sb.toString());
        }else{
            mMainDisplay.setText(R.string.no_logs_message);
        }
    }
}