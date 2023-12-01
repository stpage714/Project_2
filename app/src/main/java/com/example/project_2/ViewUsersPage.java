package com.example.project_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersPage extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display

    private Button mReturnButton;
    private Button mListUsersButton;
    private Button mRemoveUserButton;
    private TextView mAdminMainDisplay;
    private ProductLogDAO mProductLogDAO;
    private List<User> mUserList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_page);
        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mReturnButton = findViewById(R.id.returnButtonAdmin2);
        mListUsersButton = findViewById(R.id.mainListButtonAdmin2);
        mRemoveUserButton = findViewById(R.id.RemoveUserButtonAdmin2);
        mAdminMainDisplay = findViewById(R.id.AdmintextView2);
        mAdminMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling





        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        mListUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDisplay();
            }
        });
        mRemoveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RemoveUsers.intentFactory(getApplicationContext());
                startActivity(intent);
            }//end onclick
        });//end onclicklistener
    }//end oncreate



    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    private void refreshDisplay(){
        mUserList = mProductLogDAO.getAllUsers();
        if( mUserList.size() <= 0 ) {
            mAdminMainDisplay.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(User log : mUserList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mAdminMainDisplay.setText(sb.toString());
    }//end refreshDisplay()

    //create intent factory to be called statically
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, ViewUsersPage.class);
        return intent;
    }
}