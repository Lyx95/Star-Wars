package com.example.lyx.starwars.DataAcces;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lyx.starwars.Model.Films;
import com.example.lyx.starwars.Model.Personaje;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by lyx on 4/25/18.
 */

@Dao
public interface PersonajeDAO {
    @Insert(onConflict = REPLACE)
    public void insertPersonaje(Personaje personaje);

    @Insert(onConflict = REPLACE)
    public void insertFilm(Films films);

    @Query("DELETE from personaje_table")
    public void deleteAll();

    @Query("SELECT * FROM personaje_table WHERE name LIKE :name")
    public LiveData<Personaje> getPersonajeByName(String name);

    @Query("SELECT name FROM personaje_table")
    public LiveData<List<String>> getNames();

    @Query("SELECT personaje FROM  film_table" +
            " WHERE film LIKE :film")
    public LiveData<List<String>> getNamesByFilm(String film);
}
