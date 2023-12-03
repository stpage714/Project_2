package com.example.project_2.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.ProductLog;
import com.example.project_2.R;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class RemoveItems extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private Button mReturnButton;
    private Button mRemoveItemButton;
    private EditText mRemoveItemTextField;
    private TextView mAdminMainDisplay;
    private ProductLogDAO mProductLogDAO;
    private ProductLog mProductLog;
    private List<ProductLog> mProductLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_items);

        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mReturnButton = findViewById(R.id.returnButtonAdmin5);
        mRemoveItemButton = findViewById(R.id.RemoveItemButtonAdmin);
        mAdminMainDisplay = findViewById(R.id.AdmintextView5);
        mRemoveItemTextField = findViewById(R.id.removetextView5);
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

        mRemoveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert();
            }
        });
    }


    private int getValueFromField(){
        int id = 0;
        try{
            id = Integer.parseInt(mRemoveItemTextField.getText().toString());
        }catch (NumberFormatException e){
            Log.d("User Remove","Couldnt convert Product ID");
        }
        return id;
    }// end getValueFromField()

    private void alert()
    {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(this);
        alertbuilder.setMessage("Delete Item?");
        alertbuilder.setCancelable(true);

        alertbuilder.setPositiveButton(
                getString(R.string.Yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int productID = getValueFromField();
                        mProductLog = mProductLogDAO.getProductLogsById(productID).get(0);
                        if (mProductLog != null) {
                            mProductLogDAO.delete(mProductLog);
                            Toast.makeText(RemoveItems.this, "Product Removed", Toast.LENGTH_LONG).show();
                            refreshDisplay();
                            mRemoveItemTextField.getText().clear();//clear field
                        } else {
                            Toast.makeText(RemoveItems.this, "Product doesn't exist", Toast.LENGTH_LONG).show();
                            mRemoveItemTextField.getText().clear();//clear field
                        }
                    }
                });

        alertbuilder.setNegativeButton(
                getString(R.string.No),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRemoveItemTextField.getText().clear();//clear field
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
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            mAdminMainDisplay.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ProductLog log : mProductLogList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mAdminMainDisplay.setText(sb.toString());
    }//end refreshDisplay()

    //create intent factory to be called statically
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, RemoveItems.class);
        return intent;
    }
}
