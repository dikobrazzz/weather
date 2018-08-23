package com.geekbrains.weather.ui.plan;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.geekbrains.weather.R;
import com.geekbrains.weather.model.note.Note;
import com.geekbrains.weather.model.note.NoteDataReader;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private NoteDataReader noteDataReader;
    private OnMenuItemClickListener itemClickListener;

    public NoteAdapter(NoteDataReader noteDataReader) {
        this.noteDataReader = noteDataReader;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        holder.bind(noteDataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return noteDataReader.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private Note note;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_plan_rv);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener != null){
                        showPopupMenu(textView);
                    }
                }
            });
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.menu_edit:
                            itemClickListener.onItemEditClick(note);
                            return true;
                        case R.id.menu_delete:
                            itemClickListener.onItemDelete(note);
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

        public void bind(Note note) {
            this.note = note;
            textView.setText(note.getTitle());
        }
    }

    public void setItemClickListener(OnMenuItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnMenuItemClickListener{
        void onItemEditClick(Note note);
        void onItemDelete(Note note);
    }
}
