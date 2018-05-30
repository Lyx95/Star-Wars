package com.example.lyx.starwars.DataAcces;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lyx.starwars.Model.Films;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by lyx on 5/1/18.
 */

@Dao
public interface FilmsDAO {
    @Insert(onConflict = REPLACE)
    public void insertFilm(Films films);

    @Query("SELECT personaje FROM  film_table" +
            " WHERE film LIKE :film")
    public LiveData<List<String>> getNamesByFilm(String film);
}
