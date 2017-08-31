package com.sinch.android.rtc.sample.video;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class NurseVoice extends ListActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech myTTS;
    private static final String TAG = "livroandroid";
    private int MY_DATA_CHECK_CODE = 0;
    private Button btn;
    private SpeechRecognizer stt;
    private SpeechRecognizer stt2;
    private int control = 0;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tasks = new ArrayList<String>();
    private TextToSpeech tts;
    private String wordGlicose = "0";
    private String wordPressao = "0";
    private int valida = 0;
    private Boolean pressao = false;
    private String frase = " ";
    private Button Voice;
    private Button Text;
    private Button Cancel;
    private Button Adc;
    private LinearLayout Add;
    ArrayList<String> words = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listvoice);
        Text = (Button) findViewById(R.id.text);
        Voice = (Button) findViewById(R.id.voice);
        Add = (LinearLayout) findViewById(R.id.add);
        Cancel = (Button) findViewById(R.id.cancel);
        Adc = (Button) findViewById(R.id.adc);


        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        tts = new TextToSpeech(this, this);
        Consultar();
        text();
        voice();

        adc();
        cancel();

    }

    public void voice(){


        Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Text.setEnabled(false);
                Voice.setEnabled(false);
                controlP();
            }
        });

    }

    public void text(){


        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.setVisibility(View.VISIBLE);
                Text.setEnabled(false);
                Voice.setEnabled(false);

            }
        });

    }

    public void cancel(){


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.setVisibility(View.GONE);
                Text.setEnabled(true);
                Voice.setEnabled(true);
            }
        });

    }

    public void adc(){

        Adc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add.setVisibility(View.GONE);
                Text.setEnabled(true);
                Voice.setEnabled(true);
            }
        });

    }


    void controlP() {
        frase = "Por favor, fale sua pressão";
        FalarTexto();
        pressao = true;
        reconhecer();

    }

    void controlG() {
        frase = "Por favor, fale  sua Glicose";
        // falar que não entendeu a ação;
        FalarTexto();
        pressao = false;
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                reconhecer();
            }


        }.start();
    }


    //================================================= Metodo que inicia o reconhecimento de voz  =================================================//

    public void reconhecer(){
        stt2 = SpeechRecognizer.createSpeechRecognizer(this);
        stt2.setRecognitionListener(new BaseRecognitionListener() {

            public void onResults(Bundle results) {

                // Recupera as possíveis palavras que foram pronunciadas
                words = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                analiseNumeros(words);
                valida ++;
                if (valida == 2){
                    SalvarBD(wordPressao ,wordGlicose);
                }


            }
        });
        // Inicia o Listener do reconhecimento de voz
        Intent intent = getRecognizerIntent2();
        stt2.startListening(intent);
    }


    //================================================= Analise de numeros  =================================================//
    private void analiseNumeros (ArrayList<String> words) {


        for (String integer : words) {

            control++;
            String temp = integer;
            if (isInter(temp)) {

                if (pressao) {
                    wordPressao = temp;
                    control = -5;
                } else {
                    wordGlicose = temp;
                    control = -5;
                }
            } else if (isFloat(temp)) {
                if (pressao) {
                    wordPressao = temp;
                    control = -5;
                } else {
                    wordGlicose = temp;
                    control = -5;
                }
            } else {
                if (control == 4) {

                    // falar que não entendeu a ação;
                    frase = "Desculpa, mas não consegui entender o número ";
                    FalarTexto();

                }

            }
        }
        if (pressao) {
            controlG();
        }


    }

    private void Consultar()
    {
        //  Cursor cursor = getContentResolver().query(People.CONTENT_URI, new String[] {People._ID, People.NAME, People.NUMBER}, null, null, null);
        DB banco = new DB(this);
        Cursor cursor = banco.findAllc();
        startManagingCursor(cursor);

        // THE DESIRED COLUMNS TO BE BOUND
        Line newLine = new Line();
        newLine.setID(0);
        newLine.setGlicose("glicose");
        newLine.setPressao("pressao");
        newLine.setDia("dia");
        newLine.setHora("hora");



        String[] columns = new String[] { newLine.glicose, newLine.pressao ,newLine.dia , newLine.hora };
        // THE XML DEFINED VIEWS WHICH THE DATA WILL BE BOUND TO
        int[] to = new int[] { R.id.pressao, R.id.glicose , R.id.data ,R.id.hora };

        // CREATE THE ADAPTER USING THE CURSOR POINTING TO THE DESIRED DATA AS WELL AS THE LAYOUT INFORMATION
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.list_entry, cursor, columns, to);

        // SET THIS ADAPTER AS YOUR LISTACTIVITY'S ADAPTER
        this.setListAdapter(mAdapter);
    }


    //================================================= Metodo para Salvar no banco  =================================================//
    private void SalvarBD(String Glicose, String Pressao) {

        Line newLine = new Line();
        newLine.setID(0);
        newLine.setGlicose(Glicose);
        newLine.setPressao(Pressao);
        newLine.setDia(getData());
        newLine.setHora(getHora());
        DB banco = new DB(this);
        banco.save(newLine);
        Consultar();

    }



    //================================================= Identifica se é numeros  =================================================//
    private boolean isInter(String word) {
        boolean isNumber = false;
        try {
            Integer.parseInt(word);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        return isNumber;
    }

    private boolean isFloat(String word) {
        boolean isNumber = false;
        try {
            Double.parseDouble(word);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        return isNumber;
    }




    //================================================= Configuração do reconhecimento de voz  =================================================//
    protected Intent getRecognizerIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        return intent;
    }

    protected Intent getRecognizerIntent2() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale algo");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        return intent;
    }






    //================================================= Get data e hora  =================================================//
    private String getData() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);

        return data_completa;
    }

    private String getHora() {


        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String hora_atual = dateFormat_hora.format(data_atual);

        return hora_atual;
    }


//================================================= Text to Speak  =================================================//


    public void FalarTexto() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(frase, TextToSpeech.QUEUE_FLUSH, null);

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn.performClick();
            }
        }, 50);
    }


    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            Log.d(TAG, "Engine TTS inicializada com sucesso: " + Locale.getDefault());

            // Deixa inglês por padrão
            Locale PT = new Locale("pt", "BR");
            tts.setLanguage(PT);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }


    //====================================================================================================================//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libera os recursos e finaliza o STT e TTS
        stt2.stopListening();
        stt2.destroy();
        tts.shutdown();
    }
}
