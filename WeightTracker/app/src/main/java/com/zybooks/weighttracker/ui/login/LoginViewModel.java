package com.zybooks.weighttracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.LoginRepository;
import com.zybooks.weighttracker.data.Result;
import com.zybooks.weighttracker.data.model.LoggedInUser;
import com.zybooks.weighttracker.R;
import com.zybooks.weighttracker.data.model.Register;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
Last updated 9/29/2024 by Laura Brooks
THIS PAGE IS A VIEW MODEL - it works behind the scenes

Completed items:
1) Added DB code to get the userID from the SQL query when passing the uname/pwd
2) Reformatted some methods remove previous process of getting data from local cache
3)
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RegisterDao registerDao;
    private int currentUserID;


    LoginViewModel(LoginRepository loginRepository) {

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();
        //OR use local cache
        this.loginRepository = loginRepository;


    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    // arguments are uname/pwd passed from front end, returns user ID to front end
    public int login(String username, String password) {
        // can be launched in a separate asynchronous job
        getUserID(username, password);
        int result = currentUserID;


        if (result != -1){
            loginResult.setValue(new LoginResult(R.string.login_success));
            return currentUserID;

        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
            return -1;
        }
        /*
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
        */

    }

    // private method for database code to get the User ID value
    private void getUserID(String usernameEditText, String passwordEditText){

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                String username = usernameEditText.trim();
                String password = passwordEditText.trim();

                // check if username exists before inserting a new record
                if (registerDao.getProfileByUsername(username) != null) {
                    // Insert the test user
                    Register getUser = new Register();
                    getUser = registerDao.getUser(username, password);
                    if (getUser != null){
                        currentUserID = getUser.mId;
                    } else {
                        currentUserID = -1;
                    }

                } else {

                    //TODO: not working when trying to pull on same activity
                    //errorDuplicateUser();
                    currentUserID = -1;
                }


            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}