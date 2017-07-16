package com.diamond.alexandrite;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragInputPreferences extends Fragment{

    KeyInputInterface holder;
    private EditText value1Text;
    private EditText value2Text;
    private EditText value3Text;

    public interface KeyInputInterface{
        public void saveSinglePreference(String value);
        public void saveMultiplePreferences(String value1 , String value2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            holder = (KeyInputInterface) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.diamond.alexandrite.R.layout.frag_input_preferences , container , false);

            value1Text = (EditText) view.findViewById(com.diamond.alexandrite.R.id.value1Text);
            value2Text = (EditText) view.findViewById(com.diamond.alexandrite.R.id.value2Text);
            value3Text = (EditText) view.findViewById(com.diamond.alexandrite.R.id.value3Text);

        Button saveSingleButton = (Button) view.findViewById(com.diamond.alexandrite.R.id.saveSingleButton);
        Button saveMultipleButton = (Button) view.findViewById(com.diamond.alexandrite.R.id.saveMultipleButton);

        saveSingleButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.saveSinglePreference(value1Text.getText().toString());
                    }
                }
        );

        saveMultipleButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.saveMultiplePreferences(value2Text.getText().toString() , value3Text.getText().toString());
                    }
                }
        );

        return view;
    }
}
