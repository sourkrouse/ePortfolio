package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.zybooks.weighttracker.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newRegisterButtonClick(View view){
        newRegister();
    }

    private void newRegister() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
    }


    public void loginButtonClick(View view){
        loginScreen();
    }

    private void loginScreen() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_menu, menu);
        return true;
    }
    // Added the option to change the SMS approval on the main page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sms_preference:
                Intent intent = new Intent(this,
                        SMSActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}