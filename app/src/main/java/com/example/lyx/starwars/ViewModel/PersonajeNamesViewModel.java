package com.example.lyx.starwars.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.lyx.starwars.Model.Detail;
import com.example.lyx.starwars.Model.Films;
import com.example.lyx.starwars.Model.Personaje;
import com.example.lyx.starwars.DataAcces.PersonajeRepository;

import java.util.List;

/**
 * Created by lyx on 4/25/18.
 */


public class PersonajeNamesViewModel extends AndroidViewModel {
    private PersonajeRepository mRepository;
    private LiveData<List<String>> mNames; // Miembro de la clase


    public PersonajeNamesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PersonajeRepository(application);
        mNames = mRepository.getPersonajeNames(); // Inicialización en el constructor
    }

    public LiveData<List<String>> getPersonajeNames(){
        return mNames;
    }

    public LiveData<List<String>> getPersonajeNames(String film){
        if (film.equals(""))
            return getPersonajeNames();
        else
            return mRepository.getNamesByFilm(film);
    }

    public LiveData<Personaje> getPersonajeByName(String name){
        return mRepository.getPersonajeByName(name);
    }


    public LiveData<Detail> getDetailByUrl(String url){
        return mRepository.getDetailByUrl(url);
    }

    public void insert(Personaje personaje){
        mRepository.insertPersonaje(personaje);
    }

    public void insert(Films films){
        mRepository.insterFilm(films);
    }

    public void insert(Detail detail){
        mRepository.insertDetail(detail);
    }
}