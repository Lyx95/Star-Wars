package com.example.lyx.starwars.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.lyx.starwars.Model.Personaje;
import com.example.lyx.starwars.R;

/**
 * Created by lyx on 3/20/18.
 */

public class PersonajeDetailsActivity extends AppCompatActivity {


    public static final String DESCRIPTION = "es.uniovi.imovil.user.courses.DESCRIPTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personaje_details_activity);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbarDetails);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.fragment_details) != null) {

            Intent intent = getIntent();
            String nombre = intent.getStringExtra(DESCRIPTION);
            Log.d("Nombre: ",nombre);

            // Crear el fragmento pasándole el parámetro
            PersonajeDetailsFragment fragment =
                    PersonajeDetailsFragment.newInstance(nombre);

            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, fragment).commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
