package com.zybooks.weighttracker.data;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.model.LoggedInUser;
import com.zybooks.weighttracker.data.model.Register;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PLACEHOLDER
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int currentUserID = -1;

    public int login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            RegisterDao registerDao = InitDb.appDatabase.registerDao();

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    // check if username exists
                    if (registerDao.getProfileByUsername(username) != null) {
                        // get user profile
                        Register getUser = registerDao.getUser(username, password);
                        if (getUser != null){
                            setCurrentUser(getUser.mId);
                        } else {
                            setCurrentUser(-1);
                        }

                    } else {

                        //TODO: not working when trying to pull on same activity
                        //error user not found();
                        setCurrentUser(-1);
                    }


                }
            });

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return currentUserID;
        } catch (Exception e) {
            return -1;
        }
    }

    private void setCurrentUser(int Rid){
        currentUserID = Rid;

    }

    public void logout() {
        // TODO: revoke authentication
    }

}