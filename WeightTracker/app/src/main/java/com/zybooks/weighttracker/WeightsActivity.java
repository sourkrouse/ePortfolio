package com.zybooks.weighttracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.weighttracker.DailyWeights.DeleteWeightActivity;
import com.zybooks.weighttracker.DailyWeights.OnItemClickListener;
import com.zybooks.weighttracker.DailyWeights.WeightsViewAdapter;
import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.DAO.WeightsDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.model.Weights;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
Last Updated 10/13/2024, Laura Brooks
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
6) Add Recycler View to get list of weights in the DB by user ID
7) Add button clicks to delete button
 8) After checking for weights found, using an adapter page to display list of weights
 (DailyWeights folder)
 9) Put name of user in the action bar
 10) Add button to change order of the list

 */
public class WeightsActivity extends AppCompatActivity implements OnItemClickListener {

    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private RegisterDao registerDao;
    private WeightsDao weightsDao;
    private List<Weights> mWeightList;
    private int mRId;
    private int listOrder = 1;
    private Button changeOrderButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        // Get profile ID from the passed Bundle
        savedInstanceState = getIntent().getExtras();
        mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        // Initialize the Database tables
        registerDao = InitDb.appDatabase.registerDao();
        weightsDao = InitDb.appDatabase.weightsDao();

        // custom back arrow in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back_arrow);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get button ID to change order
        changeOrderButton = findViewById(R.id.buttonChangeOrder);


        // get the display name with the passed user ID, only if found
        if (mRId != -1) {
            getDisplayName(mRId);
            mWeightList = getWeightArr(mRId);
        }

        // on button click, reset the db query to a different order
        changeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listOrder == 1) {
                    listOrder = 0;
                } else {
                    listOrder = 1;

                }
                mWeightList = getWeightArr(mRId);
            }
        });


    }

    // used in Weights Adapter file for the delete button
    @Override
    public void onItemClick(View view, int position, Weights objRow) {

        Log.d("ON CLICK",Integer.toString(position));
        Intent intent = new Intent(view.getContext(), DeleteWeightActivity.class);
        intent.putExtra(DeleteWeightActivity.SINGLE_WEIGHT_ID, objRow.getWId());
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    // used in Weights Adapter file for the delete button
    @Override
    public void onLongItemClick(View view, int position, Weights objRow) {

        Intent intent = new Intent(view.getContext(), DeleteWeightActivity.class);
        intent.putExtra(DeleteWeightActivity.SINGLE_WEIGHT_ID, objRow.getWId());
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    // getter method for weight array from database, used in recycler adapter
    public List<Weights> getWeightArr(@Nullable int userID){
        getWeights(userID);
        return mWeightList;
    }

    // run query against databse to get the weight list
    private void getWeights(@Nullable int userID) {

            // Execute the database query on a background thread
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    List<Weights> getList;
                    // change query based on button click
                    if (listOrder == 1) {
                        getList = weightsDao.getWeightsNewerFirst(userID, 5);
                    } else {
                        getList = weightsDao.getWeightsOlderFirst(userID, 5);
                    }

                    Log.d("WEIGHTLIST","weightsfound");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (getList != null) {
                                // userID is not null

                                mWeightList = getList;
                                // Lookup the recyclerview in activity_weights layout
                                RecyclerView listWeights = findViewById(R.id.weight_list);

                                // Create adapter passing in the sample user data
                                WeightsViewAdapter adapter = new WeightsViewAdapter(mWeightList, WeightsActivity.this);

                                // Attach the adapter to the recyclerview to populate items
                                listWeights.setAdapter(adapter);
                                // Set layout manager to position the items
                                listWeights.setLayoutManager(new LinearLayoutManager(WeightsActivity.this));
                            } else {
                                // not found
                                mWeightList = null;
                                Log.d("WEIGHTERROR","NULL USER");

                            }
                        }
                    });




                }
            });


    }

    // getting name of user from Register table using user ID
    private void getDisplayName(int userID){
        // Execute the database query on a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Register register = registerDao.getProfile(userID);
                Log.d("REGISTNAME",register.getFirst());
                // Handle the result on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (register != null) {

                                // set name field to display name at top
                                String dFirst = register.getFirst();
                                String dLast = register.getLast();
                                Log.d("REGISTNAME2", dFirst + ' ' + dLast);
                                // set name in the action bar
                                setActionBarTitle(dFirst + ' ' + dLast);


                            } else {
                                // Invalid login credentials
                                Log.d("LOGINERROR", "Invalid Login");
                                Toast.makeText(getApplicationContext(), "Invalid login credentials", Toast.LENGTH_LONG).show();

                            }
                        }
                });
            }
        });
    }

    // setting name in the action bar
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    // getter method for button click
    public void addWeightClick(View view){
        newWeightScreen();
    }

    // goes to Add New Weight screen
    private void newWeightScreen() {

        Intent intent = new Intent(WeightsActivity.this, AddDailyWeight.class);
        intent.putExtra(WeightsActivity.EXTRA_PROFILE_ID, mRId);
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    // options menu setup
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

    // switch statement runs functions of menu
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