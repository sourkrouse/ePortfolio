package com.zybooks.weighttracker.ui.login;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.weighttracker.AuthenticateUser;
import com.zybooks.weighttracker.DailyWeights.WeightsViewModel;
import com.zybooks.weighttracker.MainActivity;
import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.SMSActivity;
import com.zybooks.weighttracker.SettingsActivity;
import com.zybooks.weighttracker.SettingsFragment;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.RegisterActivity;
import com.zybooks.weighttracker.WeightAndSeeDatabase;
import com.zybooks.weighttracker.WeightsActivity;
import com.zybooks.weighttracker.databinding.ActivityLoginBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
Last Updated 10/6/2024, Laura Brooks
PAGE DISPLAYS - username/password text fields and button to login

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Added the special back button to go back to the home page
3) Updated the header so it only displays the title and settings button
4) Adjusted manifest file to ensure all pages have an intended direction
5) Started functions to get user ID from login entered
6) Getting userID and password entered from user, running through DB code on
    view model, returning the user ID to the Weights Activity page
7) ERROR - requires a double click on the button to enter
** TODO Items line 192,205
 */
public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.studyhelper.register_id";

    private WeightAndSeeDatabase mRegisterDb;
    //private Register mRegister;

    private int mRId = -1;

    private LoginViewModel loginViewModel;
    private WeightsViewModel weightsViewModel;
    private ActivityLoginBinding binding;
    private boolean mDarkTheme;
    private SharedPreferences mSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mDarkTheme = mSharedPrefs.getBoolean(SettingsFragment.PREFERENCE_THEME, false);
        if (mDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        super.onCreate(savedInstanceState);

        // setting a custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // connecting the view to fields
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initializing the View Model where there main DB work is done
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);


        savedInstanceState = getIntent().getExtras();
        // Get profile ID from argument passed
        if (savedInstanceState != null)
            mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        // get user entry from input fields - replace binding.username
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        //final ProgressBar loadingProgressBar = binding.loading;

        // running methods from view model to get state of the user entry
/*
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }


                updateUiWithUser(loginResult.getSuccess());
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });
*/

/*
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });
*/
        // on button click, get the userID of the logged in user, if found (using view model)
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(),
                 //       passwordEditText.getText().toString());
                mRId = loginViewModel.login(usernameEditText.getText().toString(),passwordEditText.getText().toString());
                if (mRId != -1)
                    weightsScreen(mRId);
             }
        });
    }

/*
    private void updateUiWithUser(LoggedInUserView model) {
        //String welcome = getString(R.string.welcome) + model.getDisplayName();
        String welcome = "Welcome!";
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        //startActivity(new Intent(LoginActivity.this, WeightsActivity.class));
        if (mRId != -1)
            weightsScreen(mRId);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
*/
    // button click action, sends user ID to next screen action
    /*

    public void loginButtonClick(View view){


        weightsScreen(mRId);
    }
    */


    // TODO login screen errors before going to the next screen
    private void weightsScreen(int userID) {
        Intent intent = new Intent(LoginActivity.this, WeightsActivity.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, userID);
        startActivity(intent);
        setResult(RESULT_OK, intent);

        //startActivity(new Intent(LoginActivity.this, WeightsActivity.class));
        //finish();
    }
    // move to registration screen
    public void newRegisterButtonClick(View view){
        newRegister();
    }

    private void newRegister() {
        //Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_closed);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        return true;
    }

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
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                //finish(); // returns the user to parent page
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}