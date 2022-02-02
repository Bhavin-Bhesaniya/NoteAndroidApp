package com.example.noteapp.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.noteapp.Dao.NoteDao;
import com.example.noteapp.Database.NoteDB;
import com.example.noteapp.Entity.Note;

import java.util.List;

public class NoteRepo {
    public NoteDao noteDao;
    public LiveData<List<Note>> getAllNotes;
    public LiveData<List<Note>> getHighToLow;
    public LiveData<List<Note>> getLowToHigh;

    public NoteRepo(Application application) {
        NoteDB noteDB = NoteDB.getDatabaseInstance(application);
        noteDao = noteDB.noteDao();
        getAllNotes = noteDao.getAllNotes();
        getHighToLow = noteDao.getHighToLow();
        getLowToHigh = noteDao.getLowToHigh();
    }

    public void InsertNote(Note note) {
        noteDao.insertNote(note);
    }

    public void UpdateNote(Note note) {
        noteDao.updateNote(note);
    }

    public void DeleteNote(int id) {
        noteDao.deleteNote(id);
    }
}
