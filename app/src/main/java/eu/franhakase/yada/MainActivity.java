package eu.franhakase.yada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cielyang.android.clearableedittext.ClearableEditText;
import com.google.gson.Gson;

import java.util.Locale;
import java.util.Objects;

import eu.franhakase.yada.kuromoji.KanaToRomaji;
import eu.franhakase.yada.kuromoji.Kuromoji;
import eu.franhakase.yada.translation.Beam;
import eu.franhakase.yada.translation.DeepLResultTranslation;
import eu.franhakase.yada.translation.Translation;
import eu.franhakase.yada.translation.TranslationResponseCallback;
import eu.franhakase.yada.util.WebService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TranslationResponseCallback
{

    private EditText txtTokenizedInput, txtOutput;
    private ClearableEditText txtInput;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private Spinner spnInputLanguage, spnOutputLanguage;
    private KanaToRomaji kj;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kj = new KanaToRomaji();
        txtInput = findViewById(R.id.txtInput);
        txtOutput = findViewById(R.id.txtOutput);
        txtTokenizedInput = findViewById(R.id.txtTokenizedInput);
        Button btTranslate = findViewById(R.id.btTranslate);
        spnInputLanguage = findViewById(R.id.spnInputLanguage);
        spnOutputLanguage = findViewById(R.id.spnOutputLanguage);

        spnOutputLanguage.setSelection(1);
        btTranslate.setOnClickListener(this);

        txtInput.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {

            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after)
            {

            }

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                String TokenizedInput = Kuromoji.Tokenize(s.toString());
                txtTokenizedInput.setText(kj.convert(TokenizedInput));
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager .getDefaultSharedPreferences(this);
        boolean serviceEnabled = sharedPref.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        if(serviceEnabled)
        {
            startClipToTranslate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.btSettings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btTranslate)
        {
            Translate();
        }
    }

    @Override
    public void onTranslationCallBack(String s)
    {
        StringBuilder sFinalResult = new StringBuilder();

        try
        {
            int count = 1;
            DeepLResultTranslation drt = new Gson().fromJson(s, DeepLResultTranslation.class);
            for(Translation t:  drt.getResult().translations)
            {
                for(Beam b: t.getBeams())
                {
                    sFinalResult.append(String.format(Locale.getDefault(), "%d. %s\n", count, b.postprocessed_sentence));
                    count++;
                }
            }
            s = sFinalResult.toString();
        }
        catch(Exception ex)
        {
            Log.e("proc", Objects.requireNonNull(ex.getMessage()));
        }
        txtOutput.setText(s);
    }

    private void Translate()
    {
        String Input = Objects.requireNonNull(txtInput.getText()).toString();
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        Input = Input.replaceAll(characterFilter, "");
        Input = WebService.escape(Input);
        if(Input.length() > 0)
        {

            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences sharedPref = PreferenceManager .getDefaultSharedPreferences(this);
            long _id = Long.parseLong(Objects.requireNonNull(sharedPref.getString(SettingsActivity.KEY_PREF_CURRENT_ID, "48600231")));
            SharedPreferences.Editor ed = sharedPref.edit();
            ed.putString(SettingsActivity.KEY_PREF_CURRENT_ID, String.valueOf(_id+1));
            ed.apply();

            int input = spnInputLanguage.getSelectedItemPosition();
            int output = spnOutputLanguage.getSelectedItemPosition();
            String[] languages = getResources().getStringArray(R.array.languagues_value);
            WebService.GetTranslation(this, Input, languages[input], languages[output], _id);
        }
    }

    private void askForSystemOverlayPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (!Settings.canDrawOverlays(this))
                {
                    errorToast();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void errorToast()
    {
        Toast.makeText(this, "Impossível iniciar a aplicação com a permissão 'Desenhar sobre outros aplicativos' desabilitada.", Toast.LENGTH_LONG).show();
    }


    private void startClipToTranslate()
    {
        askForSystemOverlayPermission();
        if (Settings.canDrawOverlays(MainActivity.this))
        {
            Intent i = new Intent(MainActivity.this, ClipToTranslateService.class);
            i.setAction(ServiceFlags.ACTION_START_FOREGROUND_SERVICE);
            startService(i);
        }
        else
        {
            errorToast();
        }
    }
}