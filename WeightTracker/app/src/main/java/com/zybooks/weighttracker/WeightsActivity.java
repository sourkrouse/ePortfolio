package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;
import com.zybooks.weighttracker.data.model.Weights;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightsActivity extends AppCompatActivity {

    // TODO - cannot get the register ID to pass from the registration page
    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private WeightAndSeeDatabase mRegisterDb;
    private WeightAndSeeDatabase mWeightsDb;

    private TextView mNameField;
    private TextView mWeightAmt;
    private TextView mWeightDate;
    private Weights mWeights;

    private Register mRegister;
    private RegisterDao registerDao;

    private List<Weights> mWeightList;
    private int mRId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        savedInstanceState = getIntent().getExtras();
        // Get profile ID from RegisterActivity
        //Intent intent = getIntent();
        mRId = savedInstanceState.getInt(EXTRA_PROFILE_ID, -1);

        //final Register register = registerDao.getProfile(mRId);
        //mNameField = findViewById(R.id.profile_name);
        //mRegisterDb = WeightAndSeeDatabase.getInstance(getApplicationContext());

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        // Execute the database query on a background thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Register register = registerDao.getProfile(mRId);
                Log.d("REGISTNAME",register.getFirst());
                // Handle the result on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (register != null) {
                            // TODO: need password check -  && password.equals(register.getPassword())

                            // Successful login, navigate to the main activity
                            if (mRId == -1) {
                                // TODO - setting the register ID to an existing ID for testing.
                                //mRegister = new Register();
                                mRId = 5;
                                mNameField.setText("Name not Found");


                            } else {

                                // set name field to display name at top
                                String dFirst = register.getFirst();
                                String dLast = register.getLast();
                                Log.d("REGISTNAME",dFirst + ' ' + dLast);
                                TextView textView = (TextView)findViewById(R.id.profile_name);
                                textView.setText(dFirst + ' ' + dLast); //set text for text view
                                //mNameField.setText(dFirst + ' ' + dLast);

                                //TODO - for action bar
                                //setTitle(R.string.mNameField);

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


    /*
    private List<Register> loadWeights() {
        String order = mSharedPrefs.getString(SettingsFragment.PREFERENCE_SUBJECT_ORDER, "1");
        // TODO options will be how to order weights
        switch (Integer.parseInt(order)) {
            case 0: return mRegisterDb.weightDao().getWeights();
            case 1: return mRegisterDb.weightDao.getWeightsNewerFirst();
            default: return mRegisterDb.weightDao.getWeightsOlderFirst();
        }

    }
    */


/*
        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new DailyWeightFragment())
                .commit();

 */
    }



    public void addWeightClick(View view){
        newWeightScreen();
    }

    private void newWeightScreen() {
        Intent intent = new Intent();

        intent.putExtra(AddDailyWeight.EXTRA_PROFILE_ID, mRId);
        startActivity(new Intent(WeightsActivity.this, AddDailyWeight.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}