package com.example.lyx.starwars.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by lyx on 5/2/18.
 */

@Entity(tableName = "detail_table")
public class Detail {

    @PrimaryKey
    @NonNull
    private String url;
    String name;

    public Detail(){

    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
