package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zybooks.weighttracker.data.DAO.RegisterDao;
import com.zybooks.weighttracker.data.InitDb;
import com.zybooks.weighttracker.data.model.Register;

import java.util.List;

public class WeightsActivity extends AppCompatActivity {

    // TODO - cannot get the register ID to pass from the registration page
    public static final String EXTRA_PROFILE_ID = "com.zybooks.weighttracker.register_id";
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

        // Initialize the RegisterDao
        registerDao = InitDb.appDatabase.registerDao();

        //final Register register = registerDao.getProfile(mRId);
        //mNameField = findViewById(R.id.profile_name);
        //mRegisterDb = WeightAndSeeDatabase.getInstance(getApplicationContext());



        if (mRId == -1) {
            // TODO - setting the register ID to an existing ID for testing.
            //mRegister = new Register();
            mRId = 5;
            //mNameField.setText("Name not Found");
            //TODO - set title in action bar
            //setTitle(R.string.add_question);
        } else {

            mRegister = registerDao.getProfile(mRId);
            String dFirst = mRegister.getFirst();
            String dLast = mRegister.getLast();
            mNameField.setText(dFirst + ' ' + dLast);
            //TODO - for action bar
            //setTitle(R.string.mNameField);

        }



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
        startActivity(new Intent(WeightsActivity.this, AddDailyWeight.class));
        intent.putExtra(AddDailyWeight.EXTRA_PROFILE_ID, mRId);
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