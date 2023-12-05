package com.example.project_2.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.MainActivity;
import com.example.project_2.R;
import com.example.project_2.User;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class RemoveUsers extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display

    private Button mReturnButton;
    private Button mRemoveUserButton;
    private EditText mRemoveUserTextField;
    private TextView mAdminMainDisplay;
    private ProductLogDAO mProductLogDAO;
    private List<User> mUserList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_users);
        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mReturnButton = findViewById(R.id.returnButtonAdmin3);
        mRemoveUserButton = findViewById(R.id.RemoveUserButtonAdmin3);
        mAdminMainDisplay = findViewById(R.id.AdmintextView3);
        mRemoveUserTextField = findViewById(R.id.removetextView3);
        mAdminMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling
        refreshDisplay();




        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mRemoveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValueFromField();
                alert();
            }//end onclick
        });//end onclicklistener
    }//end oncreate

    private int getValueFromField(){
        int id = 0;
        try{
            id = Integer.parseInt(mRemoveUserTextField.getText().toString());
        }catch (NumberFormatException e){
            Log.d("User Remove","Couldnt convert ID");
        }
        return id;
    }// end getValueFromField()

    private void alert()
    {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setMessage("Delete User?");
        alertbuilder.setCancelable(true);

        alertbuilder.setPositiveButton(
                getString(R.string.Yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int userID = getValueFromField();
                        User temp = mProductLogDAO.getUserByUserId(userID);
                        if (temp != null) {
                            mProductLogDAO.delete(temp);
                            Toast toast = Toast.makeText(RemoveUsers.this, "Username Removed", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            refreshDisplay();
                            mRemoveUserTextField.getText().clear();//clear field
                        } else {
                            Toast toast = Toast.makeText(RemoveUsers.this, "User doesn't exist", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            mRemoveUserTextField.getText().clear();//clear field
                        }
                    }
                });

        alertbuilder.setNegativeButton(
                getString(R.string.No),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRemoveUserTextField.getText().clear();//clear field
                    }
                });

        alertbuilder.create().show();
    }//end alert

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
        Intent intent = new Intent(context, RemoveUsers.class);
        return intent;
    }
}