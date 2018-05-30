package com.example.lyx.starwars.DataAcces;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.lyx.starwars.Model.Detail;
import com.example.lyx.starwars.Model.Films;
import com.example.lyx.starwars.Model.Personaje;

/**
 * Created by lyx on 4/11/18.
 */

@Database(entities = {Personaje.class,Films.class,Detail.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class PersonajeDatabase extends RoomDatabase {
    public abstract PersonajeDAO personajeDAO();
    public abstract FilmsDAO filmsDAO();
    public abstract DetailDAO detailDAO();
    private static PersonajeDatabase INSTANCE;

    public static PersonajeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PersonajeDatabase.class) {
                if (INSTANCE == null) {

                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PersonajeDatabase.class, "personaje_database")
                            .addCallback(sRoomDatabaseCallbackCreate)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallbackCreate = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDBAsync(INSTANCE).execute();
        }
    };

    private static class populateDBAsync extends AsyncTask<Void, Void, Void> {

        private final PersonajeDAO mDAO;

        populateDBAsync(PersonajeDatabase db) {
            this.mDAO = db.personajeDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}