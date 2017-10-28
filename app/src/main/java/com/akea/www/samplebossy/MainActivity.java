package com.akea.www.samplebossy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.DefaultDatabaseErrorHandler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView greeting, textPlayback;
    ImageButton btAsk;
    TextToSpeech hintSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greeting = (TextView)findViewById(R.id.greeting);
        textPlayback = (TextView) findViewById((R.id.textPlayback));
        btAsk = (ImageButton) findViewById(R.id.btAsk);

        hintSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //set up the language Chinese
                if(status != TextToSpeech.ERROR){
                    hintSpeech.setLanguage(Locale.CHINA);
                    hintSpeech.speak("老板，请问吧！",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    public void OnButtonClick(View v)
    {
        if(v.getId() == R.id.btAsk)
        {
//            hintSpeech.speak("Say something",TextToSpeech.QUEUE_FLUSH,null);
            promptSpeechInput();
        }
    }
    public void readBackBasicHints(String hint) {
        hintSpeech.speak(hint,TextToSpeech.QUEUE_FLUSH,null);
    }

    public String mapQuestionToHint(String q) {
        String hint;

        int index1 = q.indexOf("销售");
            int index11 = q.indexOf("订单");
        int index2 = q.indexOf("机会");
        int index3 = q.indexOf("市场");
        int index4 = q.indexOf("社交");
        int index5 = q.indexOf("分析");
        int index6 = q.indexOf("生产");
 //       Log.i("bossylog", Integer.toString(index1)+Integer.toString(index2)+
 //               Integer.toString(index3)+Integer.toString(index4)+Integer.toString(index5));

        if(index1 != -1 || index11 != -1) {
            hint= "收到新订单，祝贺！";
        } else if (index2 != -1) {
            hint="发现新业务机会，注意！";
        } else if (index3 != -1) {
            hint="市场推广效果不错";
        } else if (index4 != -1) {
            hint="社交网站，没什么新鲜的";
        } else if (index5 != -1) {
            hint="业务数据分析有异动！";
        } else if (index6 != -1) {
            hint = "生产正常，缺电解铝";
        } else {
            hint = "潜在需求上涨，注意客户服务！";
        }
        return hint;
    }

    public void showVisualPresentation(String q, String hint) {
        Intent clip = new Intent("com.akea.www.samplebossy.visual");

        clip.putExtra("qstn",q);
        clip.putExtra("hint",hint);

        startActivity(clip);
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
            case 100:
                if(result_code == RESULT_OK && i != null) {
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String q = result. get(0);
                    String hint = mapQuestionToHint(q);

                    greeting.setText(q);
                    textPlayback.setText(hint);

                    readBackBasicHints(hint);
                    showVisualPresentation(q,hint);
                    //Log.i("bossylog", "question is: "+q+"hint is: "+hint);
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
