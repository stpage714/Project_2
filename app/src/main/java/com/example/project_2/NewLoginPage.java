package com.example.project_2;

import static com.example.project_2.MainActivity.USER_ID_KEY;

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
import com.example.project_2.databinding.ActivityMainBinding;

public class NewLoginPage extends AppCompatActivity {
    private ProductLogDAO mProductLogDAO;
    private User mUser;
    private String mNewUsername;
    private String mNewPassword;
    private ActivityMainBinding binding;//this binds widgets to display
    private EditText mNewUsernameField;
    private EditText mNewPasswordField;
    private Button mNewSubmit;
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
                Toast.makeText(NewLoginPage.this,"User added!",Toast.LENGTH_SHORT).show();

                mUser = new User(mNewUsername, mNewPassword);
                mProductLogDAO.insert(mUser);// new user added

                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }//end oncreate

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