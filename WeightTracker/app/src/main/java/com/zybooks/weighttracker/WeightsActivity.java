package com.zybooks.weighttracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.zybooks.weighttracker.DailyWeights.WeightsViewAdapter;
import com.zybooks.weighttracker.DailyWeights.WeightsViewModel;
import com.zybooks.weighttracker.DailyWeights.WeightsViewModelFactory;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.model.Weights;
import com.zybooks.weighttracker.ui.login.LoginViewModel;
import com.zybooks.weighttracker.ui.login.LoginViewModelFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Last Updated 10/6/2024, Laura Brooks
Page Argument is a Bundle storing the user details. The user ID is then used
to query the database for the user name.
PAGE DISPLAYS - active users name at the top of the page. Placeholders for
weights entered by date.

UPDATES INCLUDE:
1) Changed colors to a softer green
2) Added the special back button to go back to the home page
3) Updated the header so it displays the title, SMS opt out, and settings button
4) Adjusted manifest file to ensure all pages have an intended direction
5) Add new weight floating button at the bottom of the screen that connects to
add new weight screen.
6) Started functions to get weights from user ID (in progress)
7) TODO Items line 91,95,111,130
 */
public class WeightsActivity extends AppCompatActivity {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private RegisterDao registerDao;

    private List<Weights> mWeightList;
    private int mRId;
    private WeightsViewModel weightsViewModel;
    private WeightsViewAdapter weightsViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        // Get profile ID from the passed Bundle
        savedInstanceState = getIntent().getExtras();
        mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        // get the display name with the passed user ID, only if found

        if (mRId != -1) {
            getDisplayName(mRId);
            getWeightList(mRId);
        }


        // custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializing the View Model where the main DB work is done
        weightsViewModel = new ViewModelProvider(this, new WeightsViewModelFactory())
               .get(WeightsViewModel.class);

        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.ori = LinearLayoutManager.VERTICAL;

        //RecyclerView recyclerView = findViewById(R.id.list);
        //recyclerView.layoutManager = layoutManager;
        //recyclerView.setAdapter(weightsViewAdapter);
    /*
    private List<Register> loadWeights() {
        String order = mSharedPrefs.getString(SettingsFragment.PREFERENCE_SUBJECT_ORDER, "1");
        // TODO options will be how to order weights - desc, asc
        switch (Integer.parseInt(order)) {
            case 0: return mRegisterDb.weightDao().getWeights();
            case 1: return mRegisterDb.weightDao.getWeightsNewerFirst();
            default: return mRegisterDb.weightDao.getWeightsOlderFirst();
        }

    }
    */

    }

    private void getWeightList(int userID){
        Log.d("WEIGHTLIST",Integer.toString(userID));
        mWeightList = weightsViewModel.pullList(userID);

    }

    private void getDisplayName(int userID){
        // Execute the database query on a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Register register = registerDao.getProfile(userID);
                //final List<Weights> weights = weightsDao.getWeightsNewerFirst(mRId);
                Log.d("REGISTNAME",register.getFirst());
                // Handle the result on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (register != null) {
                            // TODO: need password check -  && password.equals(register.getPassword())

                            // Successful login, navigate to the main activity
                            if (userID == -1) {
                                // TODO - setting the register ID to an existing ID for testing.
                                TextView textView = (TextView)findViewById(R.id.profile_name);
                                textView.setText("NAME NOTE FOUND!"); //set text for text view


                            } else {

                                // set name field to display name at top
                                String dFirst = register.getFirst();
                                String dLast = register.getLast();
                                Log.d("REGISTNAME2",dFirst + ' ' + dLast);
                                TextView textView = (TextView)findViewById(R.id.profile_name);
                                textView.setText(dFirst + ' ' + dLast); //set text for text view
                                //mNameField.setText(dFirst + ' ' + dLast);

                                //TODO - for action bar, trying to add name to action bar
                                //actionBar.setTitle(dFirst + ' ' + dLast);

                            }
                            //finish();
                        } else {
                            // Invalid login credentials
                            Log.d("LOGINERROR","Invalid Login");
                            //Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void addWeightClick(View view){
        newWeightScreen();
    }

    private void newWeightScreen() {

        Intent intent = new Intent(WeightsActivity.this, AddDailyWeight.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, mRId);
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weights_menu, menu);

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
                intent = new Intent(WeightsActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                //intent = new Intent(WeightsActivity.this, MainActivity.class);
                //startActivity(intent);
                finish(); // returns the user to parent page
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}