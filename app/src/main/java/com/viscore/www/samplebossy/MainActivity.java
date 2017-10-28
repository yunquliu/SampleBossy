package com.viscore.www.samplebossy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView greeting;
    ImageButton btAsk;
    TextToSpeech hintSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greeting = (TextView)findViewById(R.id.greeting);
        btAsk = (ImageButton) findViewById(R.id.btAsk);

        hintSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //set up the language Chinese
                if(status != TextToSpeech.ERROR){
                    hintSpeech.setLanguage(Locale.CHINA);
                    String toSpeak = "老板准备好了,请随便问问";
                    hintSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    public void OnButtonClick(View v)
    {
        if(v.getId() == R.id.btAsk)
        {
//            hintSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
            promptSpeechInput();
        }
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"@String/emptystr");
        //Hint the user to speak

        try {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "抱歉，您的设备不支持语音识别", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int request_code, int result_code, Intent i)
    {
        super.onActivityResult(request_code, result_code,i);

        switch(request_code)
        {
            case 100: if(result_code == RESULT_OK && i != null)
            {
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                greeting.setText(result.get(0));
            }
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (hintSpeech != null) { // 关闭TTS引擎
            hintSpeech.shutdown();
        }
    }
}