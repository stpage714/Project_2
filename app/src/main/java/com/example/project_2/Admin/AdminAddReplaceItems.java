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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.Login.NewLoginPage;
import com.example.project_2.MainActivity;
import com.example.project_2.ProductLog;
import com.example.project_2.R;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.List;

public class AdminAddReplaceItems extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private ProductLogDAO mProductLogDAO;
    private List<ProductLog> mProductLogList;
    private TextView mMainDisplayAddItemAdmin;
    private EditText mDescriptionAdmin;
    private EditText mQuantityAdmin;
    private EditText mPriceAdmin;
    private EditText midAdmin;
    private Button mAdd;
    private Button mReplace;
    private Button mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_replace_items);
        getDatabase();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity

        mMainDisplayAddItemAdmin = findViewById(R.id.maintextView4);
        //allows scrolling
        mMainDisplayAddItemAdmin.setMovementMethod(new ScrollingMovementMethod());
        mDescriptionAdmin = findViewById(R.id.descriptionEditTextViewAdmin);
        midAdmin = findViewById(R.id.idFieldEditTextViewAdmin);
        mQuantityAdmin = findViewById(R.id.quantityEditTextViewAdmin);
        mPriceAdmin = findViewById(R.id.priceEditTextViewAdmin);
        mAdd = findViewById(R.id.addProductButtonAdmin);
        mReplace = findViewById(R.id.replaceProductButtonAdmin);
        mLogOut = findViewById(R.id.returnButtonAdmin4);

        refreshDisplay();
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductLog temp = getValuesFromDisplay();
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(AdminAddReplaceItems.this);
                alertbuilder.setMessage("Add Item?");
                alertbuilder.setCancelable(true);

                alertbuilder.setPositiveButton(
                        getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast toast = Toast.makeText(AdminAddReplaceItems.this, "Item added", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
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

        mReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(AdminAddReplaceItems.this);
                alertbuilder.setMessage("Replace Item?");
                alertbuilder.setCancelable(true);

                alertbuilder.setPositiveButton(
                        getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int tempID=0;
                                try{
                                    tempID = Integer.parseInt(midAdmin.getText().toString());
                                }catch (NumberFormatException e){
                                    Log.d("Product log","Couldnt convert ID");
                                }
                                mProductLogList = mProductLogDAO.getProductLogsById(tempID);
                                ProductLog tempLog = mProductLogList.get(0);
                                ProductLog compareLog = getValuesFromDisplay();
                                //compare fields
                                String tempDescription = tempLog.getDescription() ;
                                int tempQuantity = tempLog.getQuantity();
                                double tempPrice = tempLog.getPrice();

                                if(! (tempLog.getDescription().equals(compareLog.getDescription()))){
                                    tempDescription = compareLog.getDescription();
                                }
                                if(! (tempLog.getQuantity() == (compareLog.getQuantity()))){
                                    tempQuantity = compareLog.getQuantity();
                                }
                                if(! (tempLog.getPrice() == (compareLog.getPrice()))){
                                    tempPrice = compareLog.getPrice();
                                }
                                mProductLogDAO.updateProductLogsById(tempDescription,tempQuantity,tempPrice,tempID);
                                Toast toast = Toast.makeText(AdminAddReplaceItems.this, "Item Replaced", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                refreshDisplay();
                                midAdmin.getText().clear();//clear field
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
        });//end mDelete onclicklistener
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
        Intent intent = new Intent(context, AdminAddReplaceItems.class);
        return intent;
    }
}