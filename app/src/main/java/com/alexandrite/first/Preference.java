package com.alexandrite.first;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Preference extends AppCompatActivity implements FragInputPreferences.KeyInputInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alexandrite.first.R.layout.activity_preference);

        // Check if frame layout exist or not
        if (findViewById(com.alexandrite.first.R.id.inputFragment) != null) {

            // However, if we're being restored from a previous state,
            // Then we don't need to do anything and should just return,
            // Otherwise we might end up with overlapped fragments
            if (savedInstanceState != null) {
                return;
            }

            FragInputPreferences inputPreferences = new FragInputPreferences();
            inputPreferences.setArguments(getIntent().getExtras());
            FragmentTransaction fragInput = getFragmentManager().beginTransaction();

            fragInput.add(com.alexandrite.first.R.id.inputFragment, inputPreferences);
            fragInput.commit();


        }


        // Check if frame layout exist or not
        if(findViewById(com.alexandrite.first.R.id.displayFragment) != null){

            // However, if we're being restored from a previous state,
            // Then we don't need to do anything and should just return,
            // Otherwise we might end up with overlapped fragments
            if(savedInstanceState != null){
                return;
            }

            FragDisplayPreferences displayPreferences = new FragDisplayPreferences();
            displayPreferences.setArguments(getIntent().getExtras());
            FragmentTransaction fragDisplay = getFragmentManager().beginTransaction();

            fragDisplay.add(com.alexandrite.first.R.id.displayFragment,displayPreferences);
            fragDisplay.commit();
        }
    }

    @Override
    public void saveSinglePreference(String value) {

        SharedPreferences singlePref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor singleEdit = singlePref.edit();
        singleEdit.putString(getString(com.alexandrite.first.R.string.value_1_id) , value);
        singleEdit.apply();

        FragDisplayPreferences displayPreferences =  (FragDisplayPreferences) getFragmentManager().findFragmentById(com.alexandrite.first.R.id.displayFragment);
        displayPreferences.displaySinglePreference(value);
    }

    @Override
    public void saveMultiplePreferences(String value1, String value2) {
        SharedPreferences multiplePref = getSharedPreferences(getString(com.alexandrite.first.R.string.multiple_preference_id) , Context.MODE_PRIVATE);
        SharedPreferences.Editor multipleEdit = multiplePref.edit();
        multipleEdit.putString(getString(com.alexandrite.first.R.string.value_2_id) , value1);
        multipleEdit.putString(getString(com.alexandrite.first.R.string.value_3_id) , value2);
        multipleEdit.apply();

        FragDisplayPreferences displayPreferences = (FragDisplayPreferences) getFragmentManager().findFragmentById(com.alexandrite.first.R.id.displayFragment);
        displayPreferences.displayMultiplePreference(value1 , value2);
    }

}
