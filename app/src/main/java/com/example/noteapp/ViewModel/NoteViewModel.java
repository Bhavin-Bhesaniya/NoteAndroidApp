package com.example.noteapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.noteapp.Entity.Note;
import com.example.noteapp.Repository.NoteRepo;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    public NoteRepo repo;
    public LiveData<List<Note>> getAllNote;
    public LiveData<List<Note>> getHighToLow;
    public LiveData<List<Note>> getLowToHigh;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repo = new NoteRepo(application);
        getAllNote = repo.getAllNotes;
        getHighToLow = repo.getHighToLow;
        getLowToHigh = repo.getLowToHigh;
    }

    public void InsertNote(Note note) {
        repo.InsertNote(note);
    }

    public void UpdateNote(Note note) {
        repo.UpdateNote(note);
    }

    public void DeleteNote(int id) {
        repo.DeleteNote(id);
    }
}
