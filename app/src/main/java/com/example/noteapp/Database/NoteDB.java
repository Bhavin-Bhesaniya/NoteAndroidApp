package com.example.noteapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteapp.Dao.NoteDao;
import com.example.noteapp.Entity.Note;


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
