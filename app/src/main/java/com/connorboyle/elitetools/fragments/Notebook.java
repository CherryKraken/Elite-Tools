package com.connorboyle.elitetools.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.connorboyle.elitetools.Notepad;
import com.connorboyle.elitetools.R;
import com.connorboyle.elitetools.adapters.NoteEntryAdapter;
import com.connorboyle.elitetools.models.NoteDBHelper;
import com.connorboyle.elitetools.models.NoteEntry;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Connor Boyle on 2017-06-06.
 */

public class Notebook extends Fragment implements View.OnClickListener {

    public interface OnItemTouchListener {
        void onNoteEntryClick(Cursor c);
    }

    View view;
    RecyclerView rvNotebook;
    ItemTouchHelper mItemTouchHelper;
    OnItemTouchListener mTouchListener;
    Button btnNewNote;

    static final int ADD_NEW_NOTE = 10;
    static final int UPDATE_NOTE = 20;

    NoteDBHelper db;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.notebook_layout, container, false);
            rvNotebook = (RecyclerView) view.findViewById(R.id.rvNotebook);
            mTouchListener = new OnItemTouchListener() {
                @Override
                public void onNoteEntryClick(Cursor c) {
                    NoteEntry note = new NoteEntry(
                            c.getLong(0), c.getString(1), c.getString(2), System.currentTimeMillis());
                    Intent i = new Intent(getActivity(), Notepad.class)
                            .putExtra("newNote", false)
                            .putExtra("noteToEdit", note);
                    startActivityForResult(i, UPDATE_NOTE);
                }
            };
            db = new NoteDBHelper(getContext());
            setUpRecyclerView();
            btnNewNote = (Button) view.findViewById(R.id.btnNewNote);
            btnNewNote.setOnClickListener(this);
        }
        db = new NoteDBHelper(getContext());
        //refreshList();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewNote) {
            Intent i = new Intent(this.getActivity(), Notepad.class)
                    .putExtra("newNote", true);
            startActivityForResult(i, ADD_NEW_NOTE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        NoteEntry note;
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case ADD_NEW_NOTE:
                    db.open();
                    note = (NoteEntry) data.getSerializableExtra("savedNote");
                    db.createNote(note);
                    db.close();
                    rvNotebook.getAdapter().notifyDataSetChanged();
                    break;
                case UPDATE_NOTE:
                    db.open();
                    note = (NoteEntry) data.getSerializableExtra("savedNote");
                    db.updateNote(note);
                    db.close();
                    rvNotebook.getAdapter().notifyDataSetChanged();
                    break;
            }
        }
        refreshList();
    }

    private void refreshList() {
        db.open();
        cursor = db.getNotes();
        cursor.moveToFirst();
        ((NoteEntryAdapter)rvNotebook.getAdapter()).changeCursor(cursor);
        db.close();
    }

    private void setUpRecyclerView() {
        rvNotebook.setLayoutManager(new LinearLayoutManager(getContext()));
        db.open();
        cursor = db.getNotes();
        cursor.moveToFirst();
        rvNotebook.setAdapter(new NoteEntryAdapter(getContext(), cursor, mTouchListener));
        db.close();
        rvNotebook.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimation();
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback mSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = 16; // dp
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                NoteEntryAdapter adapter = (NoteEntryAdapter) rvNotebook.getAdapter();
                if (adapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NoteEntryAdapter adapter = (NoteEntryAdapter) rvNotebook.getAdapter();
                cursor = adapter.getCursor();
                cursor.moveToPosition(position);
                adapter.pendingRemoval(position);
                refreshList();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() < 0) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        mItemTouchHelper = new ItemTouchHelper(mSimpleCallback);
        mItemTouchHelper.attachToRecyclerView(rvNotebook);
    }

    private void setUpAnimation() {
        rvNotebook.addItemDecoration(new RecyclerView.ItemDecoration() {
            Drawable bg;
            boolean initiated;

            private void init() {
                bg = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    bg.setBounds(left, top, right, bottom);
                    bg.draw(c);

                }

                super.onDraw(c, parent, state);
            }
        });
    }
}
