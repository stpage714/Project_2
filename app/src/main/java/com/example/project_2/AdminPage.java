package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

public class AdminPage extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private ProductLogDAO mProductLogDAO;
    private Button mLogOut;
    private Button mAdminAdd;
    private Button mAdminView;
    private Button mAdminReplace;
    private Button mExistingUsers;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mAdminAdd = findViewById(R.id.adminAddButton);
        mAdminAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //not implemented
            }
        });

        mAdminView = findViewById(R.id.adminViewExistingItemsButton);
        mAdminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = AdminViewItems.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });//end viewExistingitems listener

        mAdminReplace = findViewById(R.id.adminReplaceButton);
        mAdminReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //not implemented
            }
        });
        mExistingUsers = findViewById(R.id.adminViewExistingUsersButton);
        mExistingUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //not implemented
            }
        });

        mLogOut = findViewById(R.id.adminLogoutButton);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());//call static intent in Loginactivity
                startActivity(intent);
            }
        });

    }//end oncreate



    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    //create intent factory to be called statically from loginactivity
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, AdminPage.class);
        return intent;
    }
}//end oncreate