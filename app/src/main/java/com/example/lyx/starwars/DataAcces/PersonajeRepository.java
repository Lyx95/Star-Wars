package com.example.lyx.starwars.DataAcces;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.lyx.starwars.DataAcces.DetailDAO;
import com.example.lyx.starwars.DataAcces.FilmsDAO;
import com.example.lyx.starwars.DataAcces.PersonajeDAO;
import com.example.lyx.starwars.DataAcces.PersonajeDatabase;
import com.example.lyx.starwars.Model.Detail;
import com.example.lyx.starwars.Model.Films;
import com.example.lyx.starwars.Model.Personaje;

import java.util.List;

/**
 * Created by lyx on 4/25/18.
 */

public class PersonajeRepository {
    private final PersonajeDAO personajeDAO;
    private final FilmsDAO filmsDAO;
    private final DetailDAO detailDAO;
    private final LiveData<List<String>> mNames;

    public PersonajeRepository(Application application) {
        this.personajeDAO = PersonajeDatabase.getDatabase(application).personajeDAO();
        this.filmsDAO = PersonajeDatabase.getDatabase(application).filmsDAO();
        this.detailDAO = PersonajeDatabase.getDatabase(application).detailDAO();
        this.mNames = this.personajeDAO.getNames();
    }

    public LiveData<Personaje> getPersonajeByName(String name) {
        return personajeDAO.getPersonajeByName(name);
    }

    public LiveData<List<String>> getNamesByFilm(String name) {
        return personajeDAO.getNamesByFilm(name);
    }

    public LiveData<Detail> getDetailByUrl(String url) {
        return detailDAO.getDetailByUrl(url);
    }

    public LiveData<List<String>> getPersonajeNames() {
        return mNames;
    }

    public void insertPersonaje(Personaje personaje) {
        new insertAsyncTask(personajeDAO).execute(personaje);
    }


    public void insertDetail(Detail detail) {
        new insertDetail(detailDAO).execute(detail);
    }

    public void insterFilm(Films films) {
        new insertFilm(filmsDAO).execute(films);
    }

    public void deleteAll() {
        new deleteAsyncTask(personajeDAO).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Personaje, Void, Void> {

        private PersonajeDAO myAsyncTaskDAO;

        public insertAsyncTask(PersonajeDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(Personaje... personajes) {
            myAsyncTaskDAO.insertPersonaje(personajes[0]);
            return null;
        }
    }

    private static class insertFilm extends AsyncTask<Films, Void, Void> {

        private FilmsDAO myAsyncTaskDAO;

        public insertFilm(FilmsDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(Films... films) {
            myAsyncTaskDAO.insertFilm(films[0]);
            return null;
        }
    }

    private static class insertDetail extends AsyncTask<Detail, Void, Void> {

        private DetailDAO myAsyncTaskDAO;

        public insertDetail(DetailDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(Detail... details) {
            myAsyncTaskDAO.insertDetail(details[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Personaje, Void, Void> {

        private PersonajeDAO myAsyncTaskDAO;

        public deleteAsyncTask(PersonajeDAO myAsyncTaskDAO) {
            this.myAsyncTaskDAO = myAsyncTaskDAO;
        }

        @Override
        protected Void doInBackground(Personaje... personajes) {
            myAsyncTaskDAO.deleteAll();
            return null;
        }
    }
}