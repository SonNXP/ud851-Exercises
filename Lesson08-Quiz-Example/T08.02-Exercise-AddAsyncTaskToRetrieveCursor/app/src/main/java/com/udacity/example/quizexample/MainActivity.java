/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.example.quizexample;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.udacity.example.droidtermsprovider.DroidTermsExampleContract;

/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */

public class MainActivity extends AppCompatActivity {

    // TODO (3) Create an instance variable storing a Cursor called mData
    private Cursor mData;
    private Button mButton;
    private TextView mWordTextView;
    private TextView mDefinitionTextView;

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private final int STATE_HIDDEN = 0;

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private final int STATE_SHOWN = 1;

    // The current state of the app
    private int mCurrentState = STATE_SHOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the views
        mButton = (Button) findViewById(R.id.button_next);

        mWordTextView = (TextView) findViewById(R.id.text_view_word);
        mDefinitionTextView = (TextView) findViewById(R.id.text_view_definition);

        // TODO (5) Create and execute your AsyncTask here
        new WordFetchTask().execute();
    }

    /**
     * This is called from the layout when the button is clicked and switches between the
     * two app states.
     * @param view The view that was clicked
     */
    public void onButtonClick(View view) {

        // Either show the definition of the current word, or if the definition is currently
        // showing, move to the next word.
        switch (mCurrentState) {
            case STATE_HIDDEN:
                showDefinition();
                break;
            case STATE_SHOWN:
                nextWord();
                break;
        }
    }

    public void nextWord() {
        // Change button text
        mButton.setText(getString(R.string.show_definition));
        mCurrentState = STATE_HIDDEN;
        int wordCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD);
//        int definitionCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
        if (mData.moveToNext()) {
            String word = mData.getString(wordCol);
//            String definition = mData.getString(definitionCol);
            mWordTextView.setText(word);
//            mDefinitionTextView.setText(definition);
        } else{
            mData.moveToFirst();
            String word = mData.getString(wordCol);
            mWordTextView.setText(word);
        }
    }

    public void showDefinition() {
        // Change button text
        mButton.setText(getString(R.string.next_word));
        mCurrentState = STATE_SHOWN;
        int definitionCol = mData.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
        String definition = mData.getString(definitionCol);
        mDefinitionTextView.setText(definition);
    }

    // TODO (1) Create AsyncTask with the following generic types <Void, Void, Cursor>
    // TODO (2) In the doInBackground method, write the code to access the DroidTermsExample
    // provider and return the Cursor object
    // TODO (4) In the onPostExecute method, store the Cursor object in mData
    private class WordFetchTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(DroidTermsExampleContract.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int wordCol = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD);
            int definitionCol = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION);
            while (cursor.moveToNext()) {
                String word = cursor.getString(wordCol);
                String definition = cursor.getString(definitionCol);
                Log.v("Cursor Example", word + " - " + definition);
            }
            mData = cursor;
            super.onPostExecute(cursor);
        }
    }
}
