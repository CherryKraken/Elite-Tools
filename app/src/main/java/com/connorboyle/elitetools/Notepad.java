package com.connorboyle.elitetools;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.connorboyle.elitetools.models.NoteEntry;

/**
 * Created by Connor Boyle on 2017-06-18.
 */

public class Notepad extends AppCompatActivity implements TextWatcher {

    EditText etTitle, etText;
    NoteEntry note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_layout);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etText = (EditText) findViewById(R.id.etNotepad);

        note = new NoteEntry("", "", System.currentTimeMillis());

        if (!getIntent().getBooleanExtra("newNote", true)) {
            note = (NoteEntry) getIntent().getSerializableExtra("noteToEdit");
            etText.setText(note.text);
            etTitle.setText(note.title);
        }

        etText.setLinksClickable(true);
        etText.setAutoLinkMask(Linkify.WEB_URLS);
        etText.setMovementMethod(ExtendedLinkMovementMethod.getInstance());
        Linkify.addLinks(etText, Linkify.WEB_URLS);
        etText.addTextChangedListener(this);
    }

    private static class ExtendedLinkMovementMethod extends ArrowKeyMovementMethod {
        static ExtendedLinkMovementMethod sInstance;
        public static ExtendedLinkMovementMethod getInstance() {
            if (sInstance == null) {
                sInstance = new ExtendedLinkMovementMethod();
            }
            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int offset = layout.getOffsetForHorizontal(line, x);
                ClickableSpan[] link = buffer.getSpans(offset, offset, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (etTitle.getText().toString().trim().equals("") && etText.getText().toString().trim().equals("")) {
            setResult(RESULT_CANCELED);
        } else if (etTitle.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter a title for this note", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            note.title = etTitle.getText().toString();
            note.text = etText.getText().toString();
            note.modified = System.currentTimeMillis();
            setResult(RESULT_OK, new Intent().putExtra("savedNote", note));
        }
        finish();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override
    public void afterTextChanged(Editable s) {
        Linkify.addLinks(s, Linkify.WEB_URLS);
    }
}
