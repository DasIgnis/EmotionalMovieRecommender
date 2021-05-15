package com.example.sentimentalrecommender.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.sentimentalrecommender.R;
import com.example.sentimentalrecommender.entities.Message;
import com.example.sentimentalrecommender.entities.MessageRepository;
import com.example.sentimentalrecommender.modules.AppModule;
import com.example.sentimentalrecommender.modules.DaggerAppComponent;
import com.example.sentimentalrecommender.modules.RoomModule;
import com.example.sentimentalrecommender.presenters.VoiceActorPresenter;
import com.example.sentimentalrecommender.views.VoiceActorView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class VoiceActorActivity extends AppCompatActivity implements RecognitionListener, VoiceActorView {

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private TextToSpeech textToSpeech;
    private boolean ttsEnabled;

    private final int AUDIO_RECORD_REQUEST_CODE = 1;
    private boolean isListening = false;

    @BindView(R.id.recognized_text_container)
    EditText recognizedTextContainer;

    @BindView(R.id.stop_listening)
    ImageButton stopListeningButton;

    @Inject
    public MessageRepository messageRepository;

    public VoiceActorPresenter voiceActorPresenter = new VoiceActorPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_actor);

        ButterKnife.bind(this);

        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);

        voiceActorPresenter.attachView(this);

        if(ContextCompat.checkSelfPermission(
                VoiceActorActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        setupTTS();
        setupSpeechRecognizer();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_RECORD_REQUEST_CODE);
        }
    }

    private void setupTTS() {
        this.textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                if (textToSpeech.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                        == TextToSpeech.LANG_AVAILABLE) {
                    textToSpeech.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                } else {
                    textToSpeech.setLanguage(Locale.US);
                }
                textToSpeech.setPitch(1.0f);
                textToSpeech.setSpeechRate(1f);
                ttsEnabled = true;
            } else if (status == TextToSpeech.ERROR) {
                Toast.makeText(
                        VoiceActorActivity.this,
                        getResources().getString(R.string.tts_initialization_error),
                        Toast.LENGTH_LONG).show();
                ttsEnabled = false;
            }
        });
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(this);
    }

    @OnClick(R.id.stop_listening)
    public void onStopListeningClicked() {
        if (isListening) {
            speechRecognizer.cancel();
            stopListeningButton.setImageResource(R.drawable.ic_baseline_mic_48);
        } else {
            speechRecognizer.startListening(speechRecognizerIntent);
            stopListeningButton.setImageResource(R.drawable.ic_baseline_mic_off_48);
        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        recognizedTextContainer.setText(data.get(0));
        textToSpeech.speak(data.get(0), TextToSpeech.QUEUE_ADD, null, Math.random() + "");
        Toast.makeText(VoiceActorActivity.this, "Finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        recognizedTextContainer.setText(data.get(0));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("LISTENER", "onEvent " + eventType);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("LISTENER", "onReady " + params);
    }

    @Override
    public void onBeginningOfSpeech() {
        recognizedTextContainer.setText("Listening...");
        Toast.makeText(VoiceActorActivity.this, "Listening", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.d("LISTENER", "onEnd ");
    }

    @Override
    public void onError(int error) {
        Log.e("LISTENER", error + "");
    }

    @Override
    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    @Override
    public void addMessageToRecycler(Message message) {

    }
}