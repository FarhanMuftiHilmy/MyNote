package com.arfdn.mynote.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDaoJava {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NoteJava note);

    @Update
    void update(NoteJava note);

    @Delete
    void delete(NoteJava note);

    @Query("SELECT * from note_table ORDER BY id ASC")
    LiveData<List<NoteJava>> getAllNotes();
}