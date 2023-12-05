package com.example.project_2.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.R;
import com.example.project_2.User;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class NewLoginPage extends AppCompatActivity {
    private ProductLogDAO mProductLogDAO;
    private User mUser;
    private String mNewUsername;
    private String mNewPassword;
    private ActivityMainBinding binding;//this binds widgets to display
    private EditText mNewUsernameField;
    private EditText mNewPasswordField;
    private Button mNewSubmit;
    private List<User> userlog;
    private int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login_page);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity
        wireupDisplay();
        getDatabase();

        mNewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(containsUserName()){
                    Toast toast = Toast.makeText(NewLoginPage.this,"Username already in use",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    ++count;
                }else{
                    Toast toast = Toast.makeText(NewLoginPage.this,"User added!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mUser = new User(mNewUsername, mNewPassword,false);
                    mProductLogDAO.insert(mUser);// new user added
                    Intent intent = LoginActivity.intentFactory(getApplicationContext());
                    startActivity(intent);
                }
                mNewUsernameField.getText().clear();
                mNewPasswordField.getText().clear();
                if(count > 2){
                    Toast toast = Toast.makeText(NewLoginPage.this,"Too many bad attempts",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }//end onclick
        });
    }//end oncreate
    private boolean containsUserName(){
        userlog=mProductLogDAO.getAllUsers();
        for( User element : userlog){
            if(element.getUserName().equals(mNewUsername)){
                return true;
            }
        }
        return false;
    }

    private void wireupDisplay(){
        mNewUsernameField = findViewById(R.id.editTextLoginNewUserName);
        mNewPasswordField = findViewById(R.id.editTextLoginNewPassword);
        mNewSubmit = findViewById(R.id.newLoginSubmitButton);
    }//end wireupDisplay

    private void getValuesFromDisplay(){
        mNewUsername = mNewUsernameField.getText().toString();
        mNewPassword = mNewPasswordField.getText().toString();
    }//end getValuesFromDisplay

    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    //create intent factory to be called statically from loginactivity
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, NewLoginPage.class);
        return intent;
    }
}//end NewLoginPage