package com.example.project_2;

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

import java.util.List;

public class Orders extends AppCompatActivity {
    static final String USER_ID_KEY = "com.example.project_2.userIdKey";
    static final String PRODUCT_ID_KEY = "com.example.project_2.productIdKey";
    private ProductLogDAO mProductLogDAO;
    private TextView mMainOrdersDisplay;
    private EditText mOrderNumberField;
    private Button mListOrdersButton;
    private Button mOrdersReturnButton;
    private Button mCancelOrdersButton;
    private ProductLog mProductLog;
    private int mUserId;
    private List<ShoppingCart> mShoppingLog;
    private List<ProductLog> mProductLogList;
    private int mCartId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getDatabase();
        mMainOrdersDisplay = findViewById(R.id.OrderstextView);
        mOrderNumberField = findViewById(R.id.CancelOrdersEditText);
        mCancelOrdersButton = findViewById(R.id.CancelOrdersbutton);
        mListOrdersButton = findViewById(R.id.OrdersListbutton);
        mOrdersReturnButton = findViewById(R.id.OrdersReturnbutton);


        mMainOrdersDisplay.setMovementMethod(new ScrollingMovementMethod());
        //allows scrolling
        mOrdersReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderNumberField.getText().clear();
                Intent intent = new Intent(Orders.this, MainActivity.class);
                startActivity(intent);
            }
        });//end ordersReturn button

        mListOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDisplay();
            }
        });//end ListOrdersbutton

        mCancelOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mCartId = Integer.parseInt(mOrderNumberField.getText().toString());
                    AlertDialog.Builder alertbuilder = new AlertDialog.Builder(Orders.this);
                    alertbuilder.setMessage("Are you sure?");
                    alertbuilder.setCancelable(true);

                    alertbuilder.setPositiveButton(
                            getString(R.string.Yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //make sure ID exists

                                    mShoppingLog = mProductLogDAO.getShoppingCartsByUserId(mUserId);

                                    if(mShoppingLog.size() > 0){
                                        //delete log records
                                        for( ShoppingCart log : mShoppingLog){
                                            if(log.getCartId() == mCartId) {
                                                mProductLogDAO.deleteShoppingCartsByCartId(mCartId);
                                            }
                                        }
                                        Toast toast = Toast.makeText(Orders.this,"Orders Deleted!",Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 0, 0);
                                        toast.show();
                                        refreshDisplay();
                                    }else {
                                        Toast toast = Toast.makeText(Orders.this,"No Orders to delete",Toast.LENGTH_LONG);
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
                    mOrderNumberField.getText().clear();
                    alertbuilder.create().show();
                }catch (NumberFormatException e){
                Log.d("Orders cancel","Couldn't convert ID");
                Toast toast = Toast.makeText(Orders.this,"Please enter a valid number!",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                mOrderNumberField.getText().clear();
                }
            }
        });//end cancelorders button

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mUserId = extras.getInt(USER_ID_KEY,-1);
        mOrderNumberField.getText().clear();
    }//end oncreate

    private void refreshDisplay(){
        mShoppingLog = mProductLogDAO.getShoppingCartsByUserId(mUserId);
        if( mShoppingLog.size() <= 0 ) {
            mMainOrdersDisplay.setText(R.string.no_logs_message);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for(ShoppingCart log : mShoppingLog){
            sb.append(log);
            sb.append("\n");
            int tempproductId = log.getProductId();
            mProductLogList = mProductLogDAO.getProductLogsById(tempproductId);
            for(ProductLog productlog : mProductLogList){
                sb.append("Description : " + productlog.getDescription());
                sb.append("\n");
                sb.append("Date Ordered : " + productlog.getDate());
            }
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=");
            sb.append("\n");
        }
        mMainOrdersDisplay.setText(sb.toString());
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
        Intent intent = new Intent(context, Orders.class);
        return intent;
    }
}