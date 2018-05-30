package com.example.lyx.starwars.DataAcces;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.lyx.starwars.Model.Detail;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by lyx on 5/2/18.
 */

@Dao
public interface DetailDAO {
    @Insert(onConflict = REPLACE)
    public void insertDetail(Detail detail);

    @Query("SELECT * FROM  detail_table" +
            " WHERE url LIKE :url")
    public LiveData<Detail> getDetailByUrl(String url);
}
