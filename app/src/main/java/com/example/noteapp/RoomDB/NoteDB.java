package com.example.noteapp.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDB extends RoomDatabase {

    public static NoteDB INSTANCE;

    public static NoteDB getDatabaseInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDB.class,
                    "note_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract NoteDao noteDao();

}
