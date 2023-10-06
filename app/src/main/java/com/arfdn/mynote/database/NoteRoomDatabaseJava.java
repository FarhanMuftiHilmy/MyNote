package com.arfdn.mynote.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteJava.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDatabaseJava extends RoomDatabase {

    public abstract NoteDaoJava noteDao();

    private static volatile NoteRoomDatabaseJava INSTANCE;

    public static NoteRoomDatabaseJava getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (NoteRoomDatabaseJava.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteRoomDatabaseJava.class, "note_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}