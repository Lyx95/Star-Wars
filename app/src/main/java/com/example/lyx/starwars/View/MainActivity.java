package com.example.lyx.starwars.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.lyx.starwars.Model.Detail;
import com.example.lyx.starwars.Model.Films;
import com.example.lyx.starwars.Model.Personaje;
import com.example.lyx.starwars.ViewModel.PersonajeNamesViewModel;
import com.example.lyx.starwars.Preferencias.PrefsActivity;
import com.example.lyx.starwars.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListItemOnClickInterface,PersonajeListFragment.Callbacks {

    private static final int SETTINGS_REQUEST_CODE = 4;
    public static int posicion = 0;
    String url="https://swapi.co/api/people/?page=1";
    private ProgressBar progressBar;
    private String next;
    private String filterFilm="";
    PersonajeListFragment fragment;
    private PersonajeNamesViewModel personajeNamesViewModel;
    ArrayList<String> añadidos=new ArrayList<>();
    private boolean mTwoPanes;
    private PersonajeDetailsFragment fragmentD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //Para el drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Para el navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;


        //ViewModel
        personajeNamesViewModel = ViewModelProviders.of(this).get(PersonajeNamesViewModel.class);

        // Existe el contenedor del fragmento?
        if (findViewById(R.id.personaje_list_frag) != null) {

            if (savedInstanceState != null) {
                //Si hay un filtro guardado lo recuperamos para mostrar solo los personajes que correspondan a ese filtro
                filterFilm= savedInstanceState.getString("peli");
            }
            else{
                if(isOnline()){
                    // Descargamos los datos cuando se inicia la actividad
                    progressBar.setVisibility(View.VISIBLE);
                    DownloadJsonTask a = new DownloadJsonTask();
                    a.execute(url);
                }
            }

            // Crear el fragmento pasándole el parámetro
            fragment = PersonajeListFragment.newInstance(filterFilm);

            // Añadir el fragmento al contenedor 'personaje_list_frag'
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.personaje_list_frag, fragment).commit();
        }

        //Si existe el conotenedor del fragmento "perosonaje_details_container" lo indicamos con una variable
        if (findViewById(R.id.perosonaje_details_container) != null)
            mTwoPanes = true;
        else
            mTwoPanes=false;

    }

    //Guarda los filtros  en un bundle al cerrar la actividad
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("peli",filterFilm);
    }

    //Cierra el drawer, si esta abierto, al pulsar el boton de back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, PrefsActivity.class);
            startActivityForResult(intent,SETTINGS_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Manejador de eventos cuando una acividad devuelve resultados
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Cuando la respuesta es "SETTINGS_REQUEST_CODE" recrea el fragmento para actualizar la vista
        if (requestCode == SETTINGS_REQUEST_CODE) {
            // Crear el fragmento pasándole el parámetro
            fragment.filtraPeli(filterFilm);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personajes) {
            filterFilm="";
        } else if (id == R.id.nav_peli1) {
            filterFilm="4";
        } else if (id == R.id.nav_peli2) {
            filterFilm="5";
        } else if (id == R.id.nav_peli3) {
            filterFilm="6";
        } else if (id == R.id.nav_peli4) {
            filterFilm="1";
        } else if (id == R.id.nav_peli5) {
            filterFilm="2";
        } else if (id == R.id.nav_peli6) {
            filterFilm="3";
        } else if (id == R.id.nav_peli7) {
            filterFilm="7";
        }

        // Crear el fragmento pasándole el parámetro
        fragment = PersonajeListFragment.newInstance(filterFilm);

        // Añadir el fragmento al contenedor 'fragment_container'
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.personaje_list_frag, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(String name) {

        if(mTwoPanes){
            if(fragmentD != null)
                getSupportFragmentManager().beginTransaction().remove(fragmentD).commit();
            // Crear el fragmento pasándole el parámetro
            fragmentD = PersonajeDetailsFragment.newInstance(name);
            // Añadir el fragmento al contenedor 'fragment_container'
            getSupportFragmentManager().beginTransaction().addToBackStack(null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.perosonaje_details_container, fragmentD).commit();
        }
        else{
            Intent intent = new Intent(this, PersonajeDetailsActivity.class);
            intent.putExtra(PersonajeDetailsActivity.DESCRIPTION, name);
            startActivity(intent);
        }
    }

    @Override
    public void onPersonajeSelected(String personaje) {
        /*if(mTwoPanes){
            if(fragmentD != null)
                getSupportFragmentManager().beginTransaction().remove(fragmentD).commit();
            else{
                getSupportFragmentManager().beginTransaction().remove(fragmentD).commit();

                // Crear el fragmento pasándole el parámetro
                fragmentD = PersonajeDetailsFragment.newInstance(personaje);
                // Añadir el fragmento al contenedor 'fragment_container'
                getSupportFragmentManager().beginTransaction().addToBackStack(null);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.perosonaje_details_container, fragmentD).commit();
            }

        }
        else{
            Intent intent = new Intent(this, PersonajeDetailsActivity.class);
            intent.putExtra(PersonajeDetailsActivity.DESCRIPTION, personaje);
            startActivity(intent);
        }*/
    }

    //Comprueba si hay conexion
    boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
        //return true;
    }

    private InputStream openHttpInputStream(String myUrl)
            throws MalformedURLException, IOException, ProtocolException {
        InputStream is;
        URL url = new URL(myUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Aquí se hace realmente la petición
        conn.connect();

        is = conn.getInputStream();
        return is;
    }


    public void populateList(ArrayList<Personaje> personajes){
        for(Personaje p:personajes)
            fragment.personajeNamesViewModel.insert(p);
    }

    private class DownloadJsonTask extends AsyncTask<String, Void, ArrayList<Personaje>> {

        private static final String NEXT_TAG = "next";
        private static final String RESULTS_TAG = "results";
        private static final String NAME_TAG = "name";
        private static final String HEIGT_TAG = "height";
        private static final String MASS_TAG = "mass";
        private static final String HAIR_TAG = "hair_color";
        private static final String SKIN_TAG = "skin_color";
        private static final String EYE_TAG = "eye_color";
        private static final String BIRTH_TAG = "birth_year";
        private static final String GENDER_TAG = "gender";
        private static final String HOMEWORLD_TAG = "homeworld";
        private static final String FILMS_TAG = "films";
        private static final String SPECIES_TAG = "species";
        private static final String VEHICLES_TAG = "vehicles";
        private static final String STARSHIPS_TAG = "starships";
        private static final String TITTLE_TAG = "tittle";
        private static final String EPISODES_TAG = "episode_id";


        private ArrayList<Personaje> parseJsonBusFile(String jsonBusInformation)
                throws JSONException, IOException {
            ArrayList<Personaje> result = new ArrayList<Personaje>();

            JSONObject root = new JSONObject(jsonBusInformation);
            next = root.getString(NEXT_TAG);//Guarga la siguiente url a descargar
            JSONArray personajesArray = root.getJSONArray(RESULTS_TAG);

            //para cada elemento del array crea un personaje
            for (int i = 0; i < personajesArray.length(); i++) {
                Personaje p = new Personaje();
                JSONObject anArrival = personajesArray.getJSONObject(i);

                //Añade datos basicos al personaje
                String name=anArrival.getString(NAME_TAG);
                p.setName(name);
                p.setBirth_year(anArrival.getString(BIRTH_TAG));
                p.setHeight(anArrival.getString(HEIGT_TAG));
                p.setMass(anArrival.getString(MASS_TAG));
                p.setHair_color(anArrival.getString(HAIR_TAG));
                p.setSkin_color(anArrival.getString(SKIN_TAG));
                p.setEye_color(anArrival.getString(EYE_TAG));
                p.setGender(anArrival.getString(GENDER_TAG));

                //añade url del homeworld
                String homeworld=anArrival.getString(HOMEWORLD_TAG);
                p.setHomeworld(homeworld);
                //si esta añadido lo descarga, si no no, de este modo solo descargamos una vez cada elemento
                if(!añadidos.contains(homeworld) && !homeworld.equals("")){
                    añadidos.add(homeworld);
                    downloadItem(homeworld);
                }

                //Añadir peliculas
                ArrayList<String> films=new ArrayList<String>();
                JSONArray filmsArray = anArrival.getJSONArray(FILMS_TAG);
                for (int j = 0; j < filmsArray.length(); j++) { //Para cada pelicula crea una relacion en la tabla personaje-pelicula
                    String anFilm = String.valueOf(filmsArray.getString(j).charAt(filmsArray.getString(j).length() - 2));
                    Films film=new Films();
                    film.setFilm(anFilm);
                    film.setPersonaje(name);
                    film.setKey(name+anFilm);
                    personajeNamesViewModel.insert(film);
                    films.add(anFilm);
                }
                p.setFilms(films);

                //añade url de especie
                ArrayList<String> sp=new ArrayList<String>();
                JSONArray spArray = anArrival.getJSONArray(SPECIES_TAG);
                String s;
                if(spArray.length()>0)
                    s= spArray.getString(0);
                else
                    s="";
                p.setSpecies(s);
                //si esta añadido lo descarga, si no no, de este modo solo descargamos una vez cada elemento
                if(!añadidos.contains(s) && !s.equals("")){
                    añadidos.add(s);
                    downloadItem(s);
                }


                //Añadir vehiculos
                ArrayList<String> vehicles=new ArrayList<String>();
                JSONArray vehiclesArray = anArrival.getJSONArray(VEHICLES_TAG);
                for (int j = 0; j < vehiclesArray.length(); j++) {
                    String anVehicle = vehiclesArray.getString(j);
                    vehicles.add(anVehicle);

                    //si esta añadido lo descarga, si no no, de este modo solo descargamos una vez cada elemento
                    if(!añadidos.contains(anVehicle)){
                        añadidos.add(anVehicle);
                        downloadItem(anVehicle);
                    }
                }

                p.setVehicles(vehicles);

                //Añadir starships
                ArrayList<String> starships=new ArrayList<String>();
                JSONArray starshipsArray = anArrival.getJSONArray(STARSHIPS_TAG);
                for (int j = 0; j < starshipsArray.length(); j++) {
                    String anStarship = starshipsArray.getString(j);
                    starships.add(anStarship);

                    //si esta añadido lo descarga, si no no, de este modo solo descargamos una vez cada elemento
                    if(!añadidos.contains(anStarship)){
                        añadidos.add(anStarship);
                        downloadItem(anStarship);
                    }
                }
                p.setStarships(starships);

                result.add(p);
            }

            return result;
        }


        @Override
        protected ArrayList<Personaje> doInBackground(String... urls) {

            // urls vienen de la llamada a execute(): urls[0] es la url
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                // TODO: las cadenas deberían ser recursos
                return null;
            }
        }

        private ArrayList<Personaje> downloadUrl(String myUrl) throws IOException {
            InputStream is = null;

            try {
                is = openHttpInputStream(myUrl);
                return parseJsonBusFile(streamToString(is));
            } catch (JSONException e) {
                return null;
            } finally {
                // Asegurarse de que el InputStream se cierra
                if (is != null) {
                    is.close();
                }

            }
        }

        /*
        Metodo para descargar los detalles que vienen dados por una url,
        por ejemplo el nombre de los vehiculos
        Una vez descargado los añade a la tabla relacionando url y nombre
         */
        private void downloadItem(String myUrl) throws IOException {
            InputStream is = null;

            try {
                is = openHttpInputStream(myUrl);
                String item=streamToString(is);
                JSONObject root;
                root = new JSONObject(item);
                String name = root.getString(NAME_TAG);
                Detail d=new Detail();
                d.setUrl(myUrl);
                d.setName(name);
                personajeNamesViewModel.insert(d);
            } catch (JSONException e) {
                return;
            } finally {
                // Asegurarse de que el InputStream se cierra
                if (is != null) {
                    is.close();
                }

            }
        }

        // Pasa un InputStream a un String
        public String streamToString(InputStream stream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 5000;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            do {
                length = stream.read(buffer);
                if (length != -1) {
                    baos.write(buffer, 0, length);
                }
            } while (length != -1);

            return baos.toString("UTF-8");
        }

        // Muestra el resultado en un text_view
        @Override
        protected void onPostExecute(ArrayList<Personaje> result) {
            //Añade resultados a la lista
            populateList(result);

            //si queda por descargar descarga la siguiente lista de personajes
            if(next!="null"){
                progressBar.setVisibility(View.VISIBLE);
                DownloadJsonTask a = new DownloadJsonTask();
                a.execute(next);
            }
            else
                progressBar.setVisibility(View.INVISIBLE);

        }
    }


}
