package com.example.project_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project_2.DB.ProductLogDAO;
import com.example.project_2.databinding.ActivityMainBinding;

public class ViewUsersPage extends AppCompatActivity {
    private ProductLogDAO mProductLogDAO;
    private ActivityMainBinding binding;//this binds widgets to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users_page);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //links textview/edittext/button to mainactivity
    }
}