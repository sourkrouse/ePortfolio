package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zybooks.weighttracker.ui.login.LoginActivity;

/*
Last Updated 9/18/2024, Laura Brooks
PAGE DISPLAYS - Default/Home page, displays logo and switches view between
    the logo and the login options

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Added the View Switcher
3) Updated the header so it only displays the title and settings button
4) Adjusted manifest file to ensure all pages have an intended direction
 */
public class MainActivity extends AppCompatActivity {

    private TextView mHeaderText;

    // main thread creates the default objects and sets the main view
    // The argument "savedInstanceState" is meant to pull in any variables
    // that would be passed into the page.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get The references of Buttons for the ViewSwitcher
        Button btnNext = (Button) findViewById(R.id.buttonNext);
        Button btnPrev = (Button) findViewById(R.id.buttonPrevious);
        ViewSwitcher simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.simpleViewSwitcher);

        // Declare in and out animations and load them using AnimationUtils class
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        // set the animation type to ViewSwitcher
        simpleViewSwitcher.setInAnimation(in);
        simpleViewSwitcher.setOutAnimation(out);

        // ClickListener for Account Options button
        // When clicked on Button ViewSwitcher will switch between views
        // The current view will go out and next view will come in with specified animation
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // show the next view of ViewSwitcher
                simpleViewSwitcher.showNext();

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // show the previous view of ViewSwitcher
                simpleViewSwitcher.showPrevious();
            }
        });


    }
// Create account button, when clicked, moves to the Register Activity
    public void newRegisterButtonClick(View view){
        newRegister();
    }

    private void newRegister() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        finish();
    }

// Login button, when clicked, moves to Login Activity
    public void loginButtonClick(View view){
        loginScreen();
    }

    private void loginScreen() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


// Displays the Main Menu file as the header
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


  // Actions for options available in the header
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}