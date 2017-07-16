package com.diamond.alexandrite;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragDisplayPreferences extends Fragment{

    private TextView value1View;
    private TextView value2View;
    private TextView value3View;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.diamond.alexandrite.R.layout.frag_display_preferences , container , false);

        value1View = (TextView) view.findViewById(com.diamond.alexandrite.R.id.value1View);
        value2View = (TextView) view.findViewById(com.diamond.alexandrite.R.id.value2View);
        value3View = (TextView) view.findViewById(com.diamond.alexandrite.R.id.value3View);

        // This is to read shared preferences

        // For single
        SharedPreferences singlePref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String value1 = singlePref.getString(getString(com.diamond.alexandrite.R.string.value_1_id) , getString(com.diamond.alexandrite.R.string.no_data));

        // For multiple
        SharedPreferences multiplePref = getActivity().getSharedPreferences(getString(com.diamond.alexandrite.R.string.multiple_preference_id) , Context.MODE_PRIVATE);
        String value2 = multiplePref.getString(getString(com.diamond.alexandrite.R.string.value_2_id) , getString(com.diamond.alexandrite.R.string.no_data));
        String value3 = multiplePref.getString(getString(com.diamond.alexandrite.R.string.value_3_id) , getString(com.diamond.alexandrite.R.string.no_data));

        value1View.setText(value1);
        value2View.setText(value2);
        value3View.setText(value3);

        return view;
    }

    public void displaySinglePreference(String value){
            value1View.setText(value);
    }

    public void displayMultiplePreference(String value1 , String value2){
            value2View.setText(value1);
            value3View.setText(value2);
    }

}
