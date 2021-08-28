package com.its.app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ResultActivity extends AppCompatActivity {


    private ImageView playBtn;
    private String text = "";
    private TextView textView;
    private TextToSpeech textToSpeechSystem;
    private ProgressBar progressBar;
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        playBtn = findViewById(R.id.playBtn);
        textView = findViewById(R.id.text);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        Bundle extras = getIntent().getExtras();

        if (!extras.containsKey("text")) {
            finish();
        } else {
            text = extras.getString("text");
        }
        textView.setText(text);

        playBtn.setOnClickListener(v -> {
            if (textToSpeechSystem != null) {
                if (textToSpeechSystem.isSpeaking()) {
                    textToSpeechSystem.stop();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    playText();
                }
            } else {
                playText();
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText();
            }
        });
    }

    private void saveText() {
        ArrayList list = StoreUtil.getArrayList(getApplicationContext());
        if (list == null) {
            list = new ArrayList();
        }
        if (!list.contains(text)) {
            list.add(text);
        }

        StoreUtil.saveArrayList(getApplicationContext(), list);
        Snackbar.make(textView.getRootView(), "Text saved", Snackbar.LENGTH_SHORT).show();
    }

    private void playText() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");

        textToSpeechSystem = new TextToSpeech(this, ttsInitResult -> {
            if (TextToSpeech.SUCCESS == ttsInitResult) {
                textToSpeechSystem.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SLKD");
                textToSpeechSystem.setOnUtteranceProgressListener(listener);
            }
        });


    }

    UtteranceProgressListener listener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);

            });
        }

        @Override
        public void onDone(String utteranceId) {
            Log.i(TAG, "onDone: ");
            runOnUiThread(() -> {
                progressBar.setVisibility(View.INVISIBLE);

            });
        }

        @Override
        public void onError(String utteranceId) {
            Log.i(TAG, "onError: ");
            runOnUiThread(() -> {
                progressBar.setVisibility(View.INVISIBLE);

            });
        }
    };


    @Override
    protected void onDestroy() {
        if (textToSpeechSystem != null && textToSpeechSystem.isSpeaking()) {
            textToSpeechSystem.stop();
            textToSpeechSystem.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (textToSpeechSystem != null && textToSpeechSystem.isSpeaking()) {
            textToSpeechSystem.shutdown();
            textToSpeechSystem.stop();
        }
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
