package com.example.project_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class AdminAddItemsPage extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private ProductLogDAO mProductLogDAO;
    private List<ProductLog> mProductLogList;
    private TextView mMainDisplayAddItemAdmin;
    private EditText mDescriptionAdmin;
    private EditText mQuantityAdmin;
    private EditText mPriceAdmin;

    private Button mSubmit;
    private Button mLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_items_page);
        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mMainDisplayAddItemAdmin = findViewById(R.id.maintextView4);
        mDescriptionAdmin = findViewById(R.id.descriptionEditTextViewAdmin);
        mQuantityAdmin = findViewById(R.id.quantityEditTextViewAdmin);
        mPriceAdmin = findViewById(R.id.priceEditTextViewAdmin);
        mSubmit = findViewById(R.id.SubmitProductButtonAdmin);
        mLogOut = findViewById(R.id.returnButtonAdmin4);
        refreshDisplay();
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductLog temp = getValuesFromDisplay();
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(AdminAddItemsPage.this);
                alertbuilder.setMessage("Add Item?");
                alertbuilder.setCancelable(true);

                alertbuilder.setPositiveButton(
                        getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminAddItemsPage.this, "Item added", Toast.LENGTH_LONG).show();
                                mProductLogDAO.insert(temp);
                                refreshDisplay();
                                mDescriptionAdmin.getText().clear();//clear field
                                mQuantityAdmin.getText().clear();//clear field
                                mPriceAdmin.getText().clear();//clear field
                            }
                        });

                alertbuilder.setNegativeButton(
                        getString(R.string.No),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDescriptionAdmin.getText().clear();//clear field
                                mQuantityAdmin.getText().clear();//clear field
                                mPriceAdmin.getText().clear();//clear field
                            }
                        });

                alertbuilder.create().show();
                refreshDisplay();

            }
        });//end msubmit onclicklistener

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });//end mLogout onclicklistener
    }//end oncreate

    private ProductLog getValuesFromDisplay(){
        String description = "No record found";
        int quantity = 0;
        double price = 0.0;
        description = mDescriptionAdmin.getText().toString();
        try{
            quantity = Integer.parseInt(mQuantityAdmin.getText().toString());
        }catch (NumberFormatException e){
            Log.d("Product log","Couldnt convert quantity");
        }
        try{
            price = Double.parseDouble(mPriceAdmin.getText().toString());
        }catch (NumberFormatException e){
            Log.d("Product log","Couldnt convert price");
        }

        ProductLog log = new ProductLog(description,quantity,price);
        return log;
    }// end getValuesFromDisplay()


    private void refreshDisplay(){
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            mMainDisplayAddItemAdmin.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ProductLog log : mProductLogList){
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainDisplayAddItemAdmin.setText(sb.toString());
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
        Intent intent = new Intent(context, AdminAddItemsPage.class);
        return intent;
    }
}