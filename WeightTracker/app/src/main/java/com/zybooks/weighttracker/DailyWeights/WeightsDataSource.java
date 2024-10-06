package com.zybooks.weighttracker.DailyWeights;


import com.zybooks.weighttracker.data.LoginRepository;

/*
Last Updated 10/6/2024, Laura Brooks
PLACEHOLDER - may be used to set the database connection


 */


public class WeightsDataSource {

    //private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;


/*
    public ResultList<WeightList> getWeights(int userID) {

        try {
            // TODO: handle loggedInUser authentication
            // Execute the database query on a background thread

            //getDBWeightList(userID);

            //WeightList fakeUser =
                    //new WeightList(
                           // java.util.UUID.randomUUID().toString(),
                            //"Jane Doe");
            return new ResultList.Success<>(getDBWeightList(userID));
        } catch (Exception e) {
            return new ResultList.Error(new IOException("Error logging in", e));
        }
    }
*/


    public void logout() {
        // TODO: revoke authentication
    }
}
