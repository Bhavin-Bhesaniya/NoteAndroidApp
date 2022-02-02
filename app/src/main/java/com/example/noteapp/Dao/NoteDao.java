package com.example.noteapp.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.noteapp.Entity.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note_db")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_db ORDER BY note_priority DESC")
    LiveData<List<Note>> getHighToLow();

    @Query("SELECT * FROM note_db ORDER BY note_priority ASC")
    LiveData<List<Note>> getLowToHigh();

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("DELETE FROM note_db WHERE id= :id")
    void deleteNote(int id);


}
