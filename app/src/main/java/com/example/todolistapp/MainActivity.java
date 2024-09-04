package com.example.todolistapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAddTask;
    private RecyclerView recyclerViewTodo, recyclerViewCompleted;
    private Spinner spinnerFilter;

    private List<String> todoList = new ArrayList<>();
    private List<String> completedList = new ArrayList<>();

    private TodoAdapter todoAdapter;
    private CompletedAdapter completedAdapter;

    private SharedPreferences sharedPreferences;
    private final String TODO_KEY = "TODO_LIST";
    private final String COMPLETED_KEY = "COMPLETED_LIST";
    public String filter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextTask = findViewById(R.id.editTextTask);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        recyclerViewTodo = findViewById(R.id.recyclerViewTodo);
        recyclerViewCompleted = findViewById(R.id.recyclerViewCompleted);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        sharedPreferences = getSharedPreferences("TodoApp", MODE_PRIVATE);

        loadTasks(filter);

        todoAdapter = new TodoAdapter(todoList, item -> {
            todoList.remove(item);
            completedList.add(item);
            saveTasks();
            todoAdapter.notifyDataSetChanged();
            completedAdapter.notifyDataSetChanged();
        });
        completedAdapter = new CompletedAdapter(completedList);

        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTodo.setAdapter(todoAdapter);

        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompleted.setAdapter(completedAdapter);

        buttonAddTask.setOnClickListener(v -> {
            String task = editTextTask.getText().toString().trim();
            if (!task.isEmpty()) {
                todoList.add(task);
                saveTasks();
                todoAdapter.notifyDataSetChanged();
                editTextTask.setText(task);
            }
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Implement filtering logic if needed
                // For now, just reset the adapter based on selection
                // Use case: Filtering tasks based on their status (all, completed, pending)
                String filter = parent.getItemAtPosition(position).toString();
                loadTasks(filter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadTasks(String filter) {
        // Load tasks from SharedPreferences
        Set<String> todoSet = sharedPreferences.getStringSet(TODO_KEY, new HashSet<>());
        Set<String> completedSet = sharedPreferences.getStringSet(COMPLETED_KEY, new HashSet<>());
        todoList.clear();
        completedList.clear();
        todoList.addAll(todoSet);
        completedList.addAll(completedSet);
    }

    private void saveTasks()
    {
        // Save tasks to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(TODO_KEY, new HashSet<>(todoList));
        editor.putStringSet(COMPLETED_KEY, new HashSet<>(completedList));
        editor.apply();
    }
}