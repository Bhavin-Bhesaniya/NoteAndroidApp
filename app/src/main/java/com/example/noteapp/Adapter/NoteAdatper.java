package com.example.noteapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Entity.Note;
import com.example.noteapp.MainActivity;
import com.example.noteapp.R;
import com.example.noteapp.UpdateNoteActivity;

import java.util.List;

public class NoteAdatper extends RecyclerView.Adapter<NoteAdatper.NoteViewHolder> {

    MainActivity mainActivity;
    List<Note> notes;
    List<Note> allNote;

    public NoteAdatper(MainActivity mainActivity, List<Note> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        allNote = notes;
    }

    public void searchNote(List<Note> filterName) {
        this.notes = filterName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.note_title);
        holder.noteDetail.setText(note.notes);
        holder.noteTime.setText(note.note_time);

        if (note.note_priority.equals("3")) {
            holder.notePriority.setBackgroundResource(R.drawable.red_shape);
        } else if (note.note_priority.equals("2")) {
            holder.notePriority.setBackgroundResource(R.drawable.yellow_shape);
        } else {
            holder.notePriority.setBackgroundResource(R.drawable.green_shape);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UpdateNoteActivity.class);
            intent.putExtra("id", note.id);
            intent.putExtra("title", note.note_title);
            intent.putExtra("note", note.notes);
            intent.putExtra("priority", note.note_priority);
            intent.putExtra("time", note.note_time);
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, noteDetail, noteTime;
        View notePriority;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.showNoteTitle);
            noteDetail = itemView.findViewById(R.id.showDetail);
            notePriority = itemView.findViewById(R.id.showNotePriority);
            noteTime = itemView.findViewById(R.id.showNoteTime);
        }
    }
}
