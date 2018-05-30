package com.example.lyx.starwars.Preferencias;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Cargar las preferencias desde el fichero
        //addPreferencesFromResource(R.xml.preferences);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(android.R.id.content, new PrefsFragment());
        ft.commit();
    }
}