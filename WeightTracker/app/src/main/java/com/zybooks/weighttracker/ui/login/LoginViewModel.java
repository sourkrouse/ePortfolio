package com.zybooks.weighttracker.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import com.zybooks.weighttracker.WeightsActivity;
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
2) Reformatted some methods to remove previous process of getting data from local cache
3) Commented out some other code meant to set the login status
 */
public class LoginViewModel extends ViewModel {

    //private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private RegisterDao registerDao;
    private LoginRepository loginRepository;
    private LoginResult loginResult;



    //LoginViewModel(LoginRepository loginRepository) {
    LoginViewModel(LoginRepository loginRepository) {
        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        //OR use local cache
        this.loginRepository = loginRepository;


    }

/*
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

 */
    // arguments are uname/pwd passed from front end, returns user ID to front end
    public int login(View view, String username, String password) {


        if (username == null || password == null){
            //loginResult.setValue(new LoginResult(R.string.login_success));
            return -1;

        } else {
            //loginResult.setValue(new LoginResult(R.string.login_failed));
            return loginRepository.login(username, password);
        }
        /*
        if (result instanceof ResultList.Success) {
            LoggedInUser data = ((ResultList.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
        */

    }
    public void logout(){
            loginRepository.logout();
    }

/*
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

 */
}