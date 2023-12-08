package com.example.project_2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.Login.LoginActivity;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BuyFragment.BuyFragmentListener {
    static final String USER_ID_KEY = "com.example.project_2.userIdKey";
    static final String PRODUCT_ID_KEY = "com.example.project_2.productIdKey";
    private static final String PREFERENCES_KEY = "com.example.project_2.PREFERENCES_KEY";
    private ActivityMainBinding binding;//this binds widgets to display
    //binding object variable
    private RecyclerView mRecyclerViewMain;
    private Recycler_Adapter mAdapterMain;
    private RecyclerView.LayoutManager mLayoutManagerMain;
   // private TextView mMainDisplay;
    private TextView mGreeting;
    private EditText mIdNumberField;
    private ArrayList<Recycler_Item> mViewList;


    private TextView mDebug;
    private Button mBuyButton;
    private Button mSearchButton;
    private Button mLogOutButton;
    private Button mOrderHistoryButton;
    private int mProductId;
    private ShoppingCart mShoppingCart;
    private Integer mIDfromRecycler;
    private int mUserId = -1;
    private SharedPreferences mPreferences = null;
    private User mUser;
    private ProductLog tempProductLog;
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

        mIdNumberField = binding.mainIDnumberEditText;

        mOrderHistoryButton = binding.mainOrderHistoryButton;
        mBuyButton = binding.mainBuyButton;
        mLogOutButton = binding.mainLogoutButton;
        mSearchButton = binding.mainSearchButton;
        mDebug = binding.DEBUG;
        mGreeting = binding.GreetingstextView;

        mRecyclerViewMain = binding.RecycleViewMain;
        mRecyclerViewMain.setHasFixedSize(true);
        mLayoutManagerMain = new LinearLayoutManager(this);
        mRecyclerViewMain.setLayoutManager(mLayoutManagerMain);
        refreshDisplay();
        mAdapterMain = new Recycler_Adapter(mViewList);
        mRecyclerViewMain.setAdapter(mAdapterMain);
        mAdapterMain.setOnItemClickListener(new Recycler_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
              BuyItem(position);
            }
        });


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Search.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });//end mSearchButton

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mProductId = Integer.parseInt(mIdNumberField.getText().toString());
                }catch (NumberFormatException e){
                    Log.d("Product log","Couldn't convert ID");
                }
                if((mProductId == 0) || (mProductId > mProductLogList.size())){
                    Toast toast = Toast.makeText(MainActivity.this,"Please enter a valid item ID",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    Intent intent = new Intent(MainActivity.this, BuyScreen.class);
                    Bundle extras = new Bundle();
                    extras.putInt(USER_ID_KEY, mUserId);
                    extras.putInt(PRODUCT_ID_KEY, mProductId);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });// end onclick submit

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });//end on onclick logout

        mOrderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Orders.class);
                Bundle extras = new Bundle();
                extras.putInt(USER_ID_KEY, mUserId);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        mGreeting.setText("Welcome " + mUser.getUserName() +"!");
    }//end onCreate

    public void BuyItem(int position){
        mIDfromRecycler = mViewList.get(position).getID();
        mAdapterMain.notifyItemChanged(position);

        BuyFragment buyFragment = new BuyFragment();
        buyFragment.show(getSupportFragmentManager(), "Buy Fragment");
        refreshDisplay();
    }

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

    private void getValuesFromDisplay(){
        try{
            mProductId = Integer.parseInt(mIdNumberField.getText().toString());
        }catch (NumberFormatException e){
            Log.d("Main Screen","Couldn't convert ID #");
        }

    }// end getValuesFromDisplay()


    private void refreshDisplay(){
        mViewList = new ArrayList<>();
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            mViewList.add(new Recycler_Item(R.drawable.cancel_icon,"No logs found!", "","",0));
            return;
        }


        for(ProductLog log : mProductLogList){
            if(log.getDescription().equalsIgnoreCase("red widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.red_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("orange widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.orange_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getQuantity()));
            }
            if(log.getDescription().equalsIgnoreCase("yellow widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.yellow_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("green widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.green_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("blue widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.blue_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("violet widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.violet_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("white widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.white_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("black widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                mViewList.add(new Recycler_Item(R.drawable.black_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
        }
        mAdapterMain = new Recycler_Adapter(mViewList);

    }//end refreshDisplay()

    //create intent factory to be called statically
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }

    @Override
    public void applyInt(Integer value) {

        if ((value == 0) || (value > mProductLogList.size())) {
            Toast toast = Toast.makeText(MainActivity.this, "Please enter a valid quantity", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            Log.d("Main Screen buy quantity: ", value.toString());
            Log.d("Main Screen ID #: ", mIDfromRecycler.toString());
            tempProductLog = mProductLogDAO.getProductLogsById(mIDfromRecycler).get(0);
            Integer logCompareQuantity = tempProductLog.getQuantity();
            Log.d("Main quant : ", logCompareQuantity.toString());
            Integer compareDifference = logCompareQuantity - value;
            if (compareDifference >= 0) {
                mShoppingCart = new ShoppingCart();
                mProductLogDAO.updateProductLogsByQuantityAndId(mIDfromRecycler, compareDifference);//update DAO
                Toast toast = Toast.makeText(MainActivity.this, "Thank you for the purchase!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();

                mShoppingCart.setUserId(mUserId);
                mShoppingCart.setProductId(mIDfromRecycler);
                mProductLogDAO.insert(mShoppingCart);
               refreshDisplay();


            }
        }
    }
}//end mainactivity