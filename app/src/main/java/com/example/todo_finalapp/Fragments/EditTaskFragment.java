package com.example.todo_finalapp.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.todo_finalapp.AddEditTask.AddEditTaskActivity;
import com.example.todo_finalapp.AddEditTask.AddEditTaskViewModel;
import com.example.todo_finalapp.AddEditTask.AddEditTaskViewModelFactory;
import com.example.todo_finalapp.Database.TaskEntry;
import com.example.todo_finalapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EditTaskFragment extends Fragment {
    private int user_id;
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    public static final String EXTRA_TASK_ID = "ExtraTaskID";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    private final int REQ_CODE = 100;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddEditTaskActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    EditText AddNote;
    RadioGroup mRadioGroup;
    Button mButton;
    Button btnDelete;
    private int del_Clicked;
    View rootView;
    private int mTaskId = DEFAULT_TASK_ID;
    AddEditTaskViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_try_frag, container, false);
        rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        user_id=Integer.parseInt(getActivity().getIntent().getStringExtra("userId"));
        initViews();

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            btnDelete.setVisibility(View.VISIBLE);

            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI

                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                AddEditTaskViewModelFactory factory = new AddEditTaskViewModelFactory(getActivity().getApplication(), mTaskId);
                viewModel = ViewModelProviders.of(this, factory).get(AddEditTaskViewModel.class);

                viewModel.getTask().observe(getActivity(), new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(TaskEntry taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);
                    }
                });

            }
        }else {

            AddEditTaskViewModelFactory factory = new AddEditTaskViewModelFactory(getActivity().getApplication(), mTaskId);
            viewModel = ViewModelProviders.of(this, factory).get(AddEditTaskViewModel.class);
        }


        mEditText = rootView.findViewById(R.id.edit_task);
        ImageView speak = rootView.findViewById(R.id.edit_speak);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    void initViews() {
        mEditText = rootView.findViewById(R.id.edit_task);
        AddNote = rootView.findViewById(R.id.edit_desc);
        mRadioGroup = rootView.findViewById(R.id.radioGroup);
        btnDelete=rootView.findViewById(R.id.deleteButton);
        mButton = rootView.findViewById(R.id.save_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonClicked();
            }
        });
    }
    private void onDeleteButtonClicked() {
        String description = mEditText.getText().toString();
        String note=AddNote.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();
        del_Clicked=1;
        TaskEntry todo = new TaskEntry(description,note, priority, date, user_id);
        if(del_Clicked==1) {
            todo.setId(mTaskId);
            viewModel.deleteTask(todo);
        }
        getActivity().finish();
        Toast toast=Toast.makeText(getActivity().getApplicationContext(),"Task Deleted",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 80);
        toast.show();
    }
    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(TaskEntry task) {
        if(task == null){
            return;
        }
        mEditText.setText(task.getDescription());
        AddNote.setText(task.getNote());
        setPriorityInViews(task.getPriority());

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        // Not yet implemented
        String description = mEditText.getText().toString();
        String note = AddNote.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        TaskEntry todo = new TaskEntry(description, note, priority, date, user_id);
        if(mTaskId == DEFAULT_TASK_ID) {
            viewModel.insertTask(todo);
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task added", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 80);
            toast.show();

        }
        else{
            todo.setId(mTaskId);
            viewModel.updateTask(todo);
            Toast toast1=Toast.makeText(getActivity().getApplicationContext(),"Task updated",Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.BOTTOM, 0, 80);
            toast1.show();

        }
        getActivity().finish();

    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) rootView.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditText.setText(result.get(0));
                }
                break;
            }
        }
    }
}