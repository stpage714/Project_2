package com.example.project_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.Login.LoginActivity;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String USER_ID_KEY = "com.example.project_2.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.project_2.PREFERENCES_KEY";
    private ActivityMainBinding binding;//this binds widgets to display
    //binding object variable
    private TextView mMainDisplay;
    private TextView mGreeting;
    private EditText mDescription;
    private EditText mQuantity;
    private EditText mPrice;
    private TextView mDebug;
    private Button mSubmit;
    private Button mLogOut;
    private Button mAdmin;
    private int mUserId = -1;
    private SharedPreferences mPreferences = null;
    private User mUser;
    private ProductLog temp;
    private ProductLogDAO mProductLogDAO;
    //entry point into database;DAO object
    private List<ProductLog> mProductLogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatabase();
        checkForUser();
        loginUser(mUserId);
        //debug();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity
        setContentView(binding.getRoot());
        mMainDisplay = binding.mainProductLogDisplay;
        mDescription = binding.mainDescriptionEditText;
        mQuantity = binding.mainQuantityEditText;

        mSubmit = binding.mainSubmitButton;
        mLogOut = binding.mainLogoutButton;

        mDebug = binding.DEBUG;
        mGreeting = binding.GreetingstextView;
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling
        refreshDisplay();



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = getValuesFromDisplay();
                mProductLogDAO.insert(temp);
                refreshDisplay();
            }
        });// end onclick submit

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });//end on onclick logout

        mGreeting.setText("Welcome " + mUser.getUserName() +"!");
    }//end onCreate

    private void debug() {
        mDebug = findViewById(R.id.DEBUG);
        mDebug.setMovementMethod(new ScrollingMovementMethod());
        List<User> users = mProductLogDAO.getAllUsers();

        StringBuilder sb = new StringBuilder();

        sb.append("All users:\n");

        for(User u : users){
            sb.append(u);
            sb.append("\n");
        }


        sb.append("all Logs\n");
        List<ProductLog> logs = mProductLogDAO.getAllProductLogs();
        for(ProductLog log : logs){
            sb.append(log);
        }

        mDebug.setText(sb.toString());
    }//end debug

    private void loginUser(int userId) {
        //check if userId is valid
        mUser = mProductLogDAO.getUserByUserId(userId);
        //check if user is not null
        addUserToPreference(mUserId);
    }//end loginUser

    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }//end addUserToPreference

    private void getDatabase() {
        mProductLogDAO = Room.databaseBuilder(this, AppDataBase.class,AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .getProductLogDAO();
        //allows main thread queries; not advisable for complicated queries because main thread can
        //halt and crash; allows access to database object
    }//end getDatabase

    private void checkForUser() {
        //do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        //do we have a user in shared preferences?
        if(mUserId != -1){
            return; //do have a user; all good
        }

        if(mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
        if(mUserId != -1){
            return; //do have a user; all good
        }
        //do we have a user at all?
        List<User> users = mProductLogDAO.getAllUsers();
        if(users.size() <= 0){
            User adminUser = new User("admin2", "admin2",true);
            User defaultUser = new User("testuser1","testuser1",false);
            mProductLogDAO.insert(adminUser,defaultUser);
        }
        //go to login screen finally
        Intent intent = LoginActivity.intentFactory(this);//call static intent in Loginactivity
        startActivity(intent);
    }//end checkForUser

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE);
    }//end getPrefs


    private void logoutUser()
    {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setMessage(R.string.logout);
        alertbuilder.setCancelable(true);

        alertbuilder.setPositiveButton(
                getString(R.string.Yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });

        alertbuilder.setNegativeButton(
                getString(R.string.No),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dont need to do anything here
                    }
                });

        alertbuilder.create().show();
    }//end logoutUser

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY,-1);//reset user back to -1
    }//end clearUserFromIntent


    private void clearUserFromPref() {
        addUserToPreference(-1);
    }//end clearUserFromPref()

    private ProductLog getValuesFromDisplay(){
        String description = "No record found";
        int quantity = 0;
        description = mDescription.getText().toString();
        try{
            quantity = Integer.parseInt(mQuantity.getText().toString());
        }catch (NumberFormatException e){
            Log.d("Product log","Couldnt convert quantity");
        }


        ProductLog log = new ProductLog(description,quantity,0);
        return log;
    }// end getValuesFromDisplay()


    private void refreshDisplay(){
        //mProductLogList = mProductLogDAO.getProductLogsById(mUserId);
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            mMainDisplay.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ProductLog log : mProductLogList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainDisplay.setText(sb.toString());
    }//end refreshDisplay()


    //create intent factory to be called statically from loginactivity
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }
}//end mainactivity