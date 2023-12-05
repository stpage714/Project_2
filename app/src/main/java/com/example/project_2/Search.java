package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    //binding object variable

    private TextView mMainSearchDisplayView;

    private Button mReturnButton;
    // Spinner elements
    private Spinner mColorSpinner;
    private Spinner mIDSpinner;
    private ProductLogDAO mProductLogDAO;

    //entry point into database;DAO object
    private List<ProductLog> mProductLogList;
    List<String> numbers;
    private String  mSearchColor;
    private int mIdSearchValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getDatabase();
        mMainSearchDisplayView = findViewById(R.id.SearchView);
        mMainSearchDisplayView.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling
        mReturnButton = findViewById(R.id.returnButtonSearch);
        mColorSpinner = findViewById(R.id.colorssSpinner);
        mIDSpinner = findViewById(R.id.IDSpinner);
        mProductLogList = mProductLogDAO.getAllProductLogs();
/*
        //populate numbers array
        for(Integer i= 1; i<= mProductLogList.size(); ++i){
            numbers[i] = i.toString();
        }
        Log.d("Search page",numbers.toString());
*/
        numbers = new ArrayList<String>();
        numbers.add("1");
        numbers.add("2");
        numbers.add("3");
        ArrayAdapter<CharSequence> adapterID = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapterID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIDSpinner.setAdapter(adapterID);
        mIDSpinner.setOnItemSelectedListener(new IDSpinnerClass());

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,R.array.colors, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mColorSpinner.setAdapter(adapterColor);
        mColorSpinner.setOnItemSelectedListener(new ColorsSpinnerClass());



        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to main
                Intent intent = new Intent(Search.this, MainActivity.class);
                startActivity(intent);
            }
        });// end return click


        getDatabase();
    }//end oncreate


    class IDSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String item = parent.getItemAtPosition(position).toString();
            mProductLogList = mProductLogDAO.getProductLogsById(position+1);
            refreshDisplay();
            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

    class ColorsSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSearchColor = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(),mSearchColor,Toast.LENGTH_LONG).show();
            mSearchColor = mSearchColor.toLowerCase();
            Log.d("Search color",mSearchColor);

            mProductLogList = mProductLogDAO.getProductLogsByDescription(mSearchColor);
            refreshDisplay();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    //create intent factory to be called statically
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, Search.class);
        return intent;
    }


    private void refreshDisplay(){

        if( mProductLogList.size() <= 0 ) {
            mMainSearchDisplayView.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ProductLog log : mProductLogList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainSearchDisplayView.setText(sb.toString());
    }//end refreshDisplay()
}