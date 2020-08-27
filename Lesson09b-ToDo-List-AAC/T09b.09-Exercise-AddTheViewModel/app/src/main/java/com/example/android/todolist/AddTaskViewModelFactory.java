package com.example.android.todolist;

import android.arch.lifecycle.ViewModelProvider;

import com.example.android.todolist.database.AppDatabase;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mTaskId;

    public AddTaskViewModelFactory (AppDatabase database, int taskId) {
        mDb = database;
        mTaskId = taskId;
    }
}
