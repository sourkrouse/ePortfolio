package com.zybooks.weighttracker.ui.login;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Separate class object to run an executor function with Callable() so that the DB Query can be called
 * and a user ID can be returned to the repository object. The Callable function allows a return
 * and username/password variables are set with a constructor.
 *
 * Last updated 10/16/2024, by Laura Brooks
 *
 *
 */
public class RunOnThread {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Register> foundUserList = new ArrayList<>();
    private RegisterDao registerDao = InitDb.appDatabase.registerDao();
    private String username;
    private String password;
    private volatile int currentUserID = -1;

    // constructor sets the user/pwd variables
    public RunOnThread(String username, String password){
            this.username = username;
            this.password = password;

    }

    // runs the ExecutorFunction object in a Callable function on a different thread
    // a separate thread is necessary so the app is not forced closed if there is a DB error.
    public void getUserId() {
        // using a Future object to call the DB Query in executor
        Future<Integer> result = executorService.submit(new Callable<Integer>() {
            // Callable requires the call() function
            public Integer call() throws Exception {
                // the other thread - running DB Query, get user ID for return
                foundUserList = registerDao.getUserList(username, password);

                if(foundUserList.size() != 1){
                    // more than 1 user found with same username and password
                    return -1;
                } else {
                    // one user found, return ID
                    return foundUserList.get(0).getId();
                }

            }
        });
        // try/catch to get the result from the Future object or the exception
        try {
            currentUserID = result.get();
        } catch (Exception e) {
            // failed
            currentUserID = -1;
        }
        executorService.shutdown(); // shutdown executor once complete
    }

        // getter method for main activity
        public int getUser() {
            getUserId();
            return currentUserID;
        }

}
