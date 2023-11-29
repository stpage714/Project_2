package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mSubmitButton;
    private Button mAdminButton;
    private ProductLogDAO mProductLogDAO;
    private String mUsername;
    private String mPassword;
    private User mUser;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getDatabase();
        wireupDisplay();

    }//end oncreate

    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextTextLoginUserName);
        mPasswordField = findViewById(R.id.editTextTextLoginPassword);
        mSubmitButton = findViewById(R.id.buttonLogin);
        mAdminButton = findViewById(R.id.mainAdminButton);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if valid user in productlogdao
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                    }else{
                        if((mUsername.equals("admin2") && mPassword.equals("admin2")) || mUser.getMisAdmin()){
                            //admin
                            mAdminButton.setVisibility(View.VISIBLE);
                        }else {
                            //user ok main login
                            intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                            startActivity(intent);
                        }
                    }
                }else{
                    //user not in database; new user add
                    intent = NewLoginPage.intentFactory(getApplicationContext());
                    startActivity(intent);
                }
            }//end on click login submit
        });


        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }//end wireupDisplay()

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }//end validatePassword()

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }//end getValuesFromDisplay

    private boolean checkForUserInDatabase(){
        mUser = mProductLogDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this,"no user " + mUsername + " found",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }//checkForUserInDatabase()


    private void getDatabase(){
        //same database as mainactivity
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
    }//end getDatabase

    //create intent factory to be called from main statically
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

}//end loginactivity