package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;


public class AdminViewItems extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private ProductLogDAO mProductLogDAO;
    private TextView mMainView;
    private Button mReturnButton;


    private Button mListItemsButton;
    private List<ProductLog> mProductLogList;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_items);
        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mMainView=findViewById(R.id.mainTextViewAdmin);
        mReturnButton = findViewById(R.id.returnButtonAdmin);
        mListItemsButton = findViewById(R.id.mainListButtonAdmin);
        mMainView.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling

        mListItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            refreshDisplay();
            }
        });
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }//end oncreate

    private void refreshDisplay(){
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            mMainView.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ProductLog log : mProductLogList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainView.setText(sb.toString());
    }//end refreshDisplay()
    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    //create intent factory to be called statically
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, AdminViewItems.class);
        return intent;
    }
}