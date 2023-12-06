package com.example.project_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_2.Admin.AdminPage;
import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;

import java.util.List;

public class BuyScreen extends AppCompatActivity {
    static final String USER_ID_KEY = "com.example.project_2.userIdKey";
    static final String PRODUCT_ID_KEY = "com.example.project_2.productIdKey";
    private ProductLogDAO mProductLogDAO;
    private int mProductID;
    private int mBuyQuantity;
    //entry point into database;DAO object
    private ProductLog mProductLog;
    private TextView mBuyTextView;
    private TextView mBuyPromptTextView;
    private EditText mQuantityField;
    private Button mBuyButton;
    private Button mReturnButton;
    private int mUserId;
    private ShoppingCart mShoppingCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_screen);
        getDatabase();
        mBuyTextView = findViewById(R.id.BuytextView);
        mQuantityField = findViewById(R.id.quantityEditText);
        mBuyButton = findViewById(R.id.BuyButton);
        mReturnButton = findViewById(R.id.returnButtonBuy);
        mBuyPromptTextView = findViewById(R.id.BuypromtTextBox);

        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to main
                mQuantityField.getText().clear();
                Intent intent = new Intent(BuyScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mBuyQuantity = Integer.parseInt(mQuantityField.getText().toString());
                }catch (NumberFormatException e){
                    Log.d("Buy Screen","Couldn't convert quantity");
                }
                AlertDialog.Builder alertbuilder = new AlertDialog.Builder(BuyScreen.this);
                alertbuilder.setMessage("Are you sure?");
                alertbuilder.setCancelable(true);

                alertbuilder.setPositiveButton(
                        getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //make sure quantity exists
                                int logcompareQuantity = mProductLog.getQuantity();
                                int compareDifference = logcompareQuantity - mBuyQuantity;
                                if(compareDifference >= 0){
                                    mShoppingCart = new ShoppingCart();
                                    mProductLogDAO.updateProductLogsByQuantityAndId(mProductID,compareDifference);//update DAO
                                    Toast toast = Toast.makeText(BuyScreen.this,"Thank you for the purchase!",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.TOP, 0, 0);
                                    toast.show();
                                    mProductLog = mProductLogDAO.getProductLogsById(mProductID).get(0);
                                    mShoppingCart.setUserId(mUserId);
                                    mShoppingCart.setProductId(mProductID);
                                    mProductLogDAO.insert(mShoppingCart);
                                    refreshDisplay();
                                }else {
                                    Toast toast = Toast.makeText(BuyScreen.this,"Please enter a valid number",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
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
                mQuantityField.getText().clear();
                alertbuilder.create().show();
            }
        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mUserId = extras.getInt(USER_ID_KEY,-1);
        mProductID = extras.getInt(PRODUCT_ID_KEY,-1);

        mProductLog = mProductLogDAO.getProductLogsById(mProductID).get(0);
        String description = mProductLog.getDescription();
        refreshDisplay();

        mBuyPromptTextView.setText("How many " + description + "s do you want to buy?");

    }//end on create

    private void refreshDisplay(){
        StringBuilder sb = new StringBuilder();
        sb.append(mProductLog);
        sb.append("\n");
        sb.append("=-=-=-=-=-=-=-=");
        sb.append("\n");
        mBuyTextView.setText(sb.toString());
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
        Intent intent = new Intent(context, BuyScreen.class);
        return intent;
    }
}