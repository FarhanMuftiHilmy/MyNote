package com.arfdn.mynote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.arfdn.mynote.R;
import com.arfdn.mynote.database.NoteDaoJava;
import com.arfdn.mynote.database.NoteJava;
import com.arfdn.mynote.database.NoteRoomDatabaseJava;
import com.arfdn.mynote.databinding.ActivityMainJavaBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivityJava extends AppCompatActivity {
    private NoteDaoJava mNotesDao;
    private ExecutorService executorService;
    private int updateId = 0;

    private ActivityMainJavaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDatabaseJava db = NoteRoomDatabaseJava.getDatabase(this);
        mNotesDao = db.noteDao();

        binding = ActivityMainJavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAdd.setOnClickListener(view -> {
            insert(new NoteJava(
                    binding.edtTitle.getText().toString(),
                    binding.edtDesc.getText().toString(),
                    binding.edtDate.getText().toString()
            ));
            setEmptyField();
        });

        binding.btnUpdate.setOnClickListener(view -> {
            update(new NoteJava(
                    updateId,
                    binding.edtTitle.getText().toString(),
                    binding.edtDesc.getText().toString(),
                    binding.edtDate.getText().toString()
            ));
            updateId = 0;
            setEmptyField();
        });

        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            NoteJava item = (NoteJava) adapterView.getAdapter().getItem(i);
            updateId = item.getId();
            binding.edtTitle.setText(item.getTitle());
            binding.edtDesc.setText(item.getDescription());
            binding.edtDate.setText(item.getDate());
        });

        binding.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            NoteJava item = (NoteJava) adapterView.getAdapter().getItem(i);
            delete(item);
            return true;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllNotes();
    }

    private void setEmptyField() {
        binding.edtTitle.setText("");
        binding.edtDesc.setText("");
        binding.edtDate.setText("");
    }



    private void getAllNotes() {
        mNotesDao.getAllNotes().observe(this, notes -> {
            ArrayAdapter<NoteJava> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1, notes
            );
            binding.listView.setAdapter(adapter);
        });
    }

    private void insert(NoteJava note) {
        executorService.execute(() -> mNotesDao.insert(note));
    }

    private void delete(NoteJava note) {
        executorService.execute(() -> mNotesDao.delete(note));
    }

    private void update(NoteJava note) {
        executorService.execute(() -> mNotesDao.update(note));
    }

}