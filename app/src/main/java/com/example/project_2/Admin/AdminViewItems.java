package com.example.project_2.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_2.DB.AppDataBase;
import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.ProductLog;
import com.example.project_2.R;
import com.example.project_2.Recycler_Adapter;
import com.example.project_2.Recycler_Item;
import com.example.project_2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class AdminViewItems extends AppCompatActivity {
    private ActivityMainBinding binding;//this binds widgets to display
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductLogDAO mProductLogDAO;

    private Button mListReturnButton;
    private  ArrayList<Recycler_Item> viewList;

    private List<ProductLog> mProductLogList;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("view", "in oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_items);

        mListReturnButton = findViewById(R.id.ReturnButtonViewItemsAdmin);
        mRecyclerView = findViewById(R.id.AdminViewRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getDatabase();
        refreshDisplay();

        mAdapter = new Recycler_Adapter(viewList);
        mRecyclerView.setAdapter(mAdapter);
        mListReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminPage.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }//end oncreate

    private void refreshDisplay(){
        viewList = new ArrayList<>();
        mProductLogList = mProductLogDAO.getAllProductLogs();
        if( mProductLogList.size() <= 0 ) {
            viewList.add(new Recycler_Item(R.drawable.cancel_icon,"No logs found!", "Please insert some","$0.00",0));
            return;
        }


        for(ProductLog log : mProductLogList){
            if(log.getDescription().equalsIgnoreCase("red widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.red_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("orange widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.orange_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("yellow widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.yellow_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("green widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.green_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("blue widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.blue_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("violet widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.violet_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("white widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.white_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
            if(log.getDescription().equalsIgnoreCase("black widget")){
                Integer temp = log.getQuantity();
                Double tempPrice = log.getPrice();
                viewList.add(new Recycler_Item(R.drawable.black_widget,log.getDescription(),temp.toString(),
                                tempPrice.toString(),log.getProductId()));
            }
        }
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
        Intent intent = new Intent(context, AdminViewItems.class);
        return intent;
    }
}