package com.geekbrains.weather.ui.plan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.geekbrains.weather.R;
import com.geekbrains.weather.model.note.Note;
import com.geekbrains.weather.model.note.NoteDataReader;
import com.geekbrains.weather.model.note.NoteDataSourse;
import com.geekbrains.weather.ui.base.BaseFragment;
import com.geekbrains.weather.ui.base.BaseView;

public class PlanFragment extends BaseFragment {

    private NoteDataSourse noteDataSourse;
    private NoteAdapter adapter;
    private NoteDataReader reader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.plan_layout, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.plan_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_add:
                addElement();
                return true;
            case R.id.menu_clear:
                clearList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearList() {

    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
       initDataSource();

        RecyclerView recyclerView = view.findViewById(R.id.rv_plan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(reader);
        adapter.setItemClickListener(new NoteAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemEditClick(Note note) {
                editElement(note);
            }

            @Override
            public void onItemDelete(Note note) {
                deleteElement(note);
            }
        });
    }

    private void addElement(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View alterView = inflater.inflate(R.layout.add_recycler, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(alterView);
        builder.setTitle(R.string.title_add);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.menu_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editTextNote = alterView.findViewById(R.id.et_note_text);
                EditText editTextTitle = alterView.findViewById(R.id.et_note_title);
                EditText editTextDate = alterView.findViewById(R.id.et_note_date);
                EditText editTextEvent = alterView.findViewById(R.id.et_note_event);

                noteDataSourse.addNote(editTextTitle.getText().toString(), editTextNote.getText().toString(), editTextDate.getText().toString(), editTextEvent.getText().toString());
                dataUpdate();
            }
        });
        builder.show();
    }

    private void deleteElement(Note note) {
        noteDataSourse.delete(note);
        dataUpdate();
    }

    private void editElement(Note note) {
        noteDataSourse.editNote(note, "Edited", "Edited title");
        dataUpdate();
    }

    private void dataUpdate() {
        reader.refresh();
        adapter.notifyDataSetChanged();
    }

    private void initDataSource(){
        noteDataSourse = new NoteDataSourse(getActivity().getApplicationContext());
        noteDataSourse.open();
        reader = noteDataSourse.getDataReader();
    }
}
