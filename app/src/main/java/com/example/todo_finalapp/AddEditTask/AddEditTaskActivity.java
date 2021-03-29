package com.example.todo_finalapp.AddEditTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.todo_finalapp.Fragments.EditTaskFragment;
import com.example.todo_finalapp.R;

public class AddEditTaskActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "ExtraTaskID";
    Fragment mFragment;
    FragmentManager mFragmentManager;
    AddEditTaskViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        mFragment=new EditTaskFragment();
        mFragmentManager=getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.activityEdit,mFragment)
                .commit();

    }
}
