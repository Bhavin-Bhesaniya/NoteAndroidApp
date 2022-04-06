package com.example.noteapp.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_db")
public class Note {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "note_title")
    public String note_title;

    @ColumnInfo(name = "note_details")
    public String notes;

    @ColumnInfo(name = "note_priority")
    public String note_priority;

    @ColumnInfo(name = "note_time")
    public String note_time;
}
