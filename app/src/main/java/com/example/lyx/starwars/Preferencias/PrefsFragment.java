package com.example.lyx.starwars.Preferencias;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.lyx.starwars.R;

/**
 * Created by lyx on 5/23/18.
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Cargar las preferencias desde el fichero
        addPreferencesFromResource(R.xml.preferences);
    }
}