package eu.franhakase.yada;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

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

public class TranslationDialogActivity extends Activity implements View.OnClickListener, TranslationResponseCallback
{
    private EditText txtInput, txtTokenizedInput, txtOutput;
    private Spinner spnInputLanguague, spnOutputLanguage;
    private KanaToRomaji kj;
    private boolean bResume = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_main);
        LinearLayout ll = findViewById(R.id.main_view);
        ll.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_rounded_rectangle));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.gravity = Gravity.TOP;
        getWindow().setAttributes(a);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        txtInput = findViewById(R.id.txtInput);
        txtOutput = findViewById(R.id.txtOutput);
        txtTokenizedInput = findViewById(R.id.txtTokenizedInput);
        spnInputLanguague = findViewById(R.id.spnInputLanguage);
        spnOutputLanguage = findViewById(R.id.spnOutputLanguage);
        spnOutputLanguage.setSelection(1);

        Button btTranslate = findViewById(R.id.btTranslate);
        btTranslate.setOnClickListener(this);

        kj = new KanaToRomaji();

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
    protected void onStart()
    {

        super.onStart();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {

        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus && !bResume)
        {
            bResume = true;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = clipboard.getPrimaryClip();
            if(   clip != null && clip.getItemCount() > 0 && clip.getItemAt(0).getText() != null)
            {
                txtInput.setText(clip.getItemAt(0).getText().toString());
                Translate();
            }

        }
        super.onWindowFocusChanged(hasFocus);
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
        String Input = txtInput.getText().toString();
        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        Input = Input.replaceAll(characterFilter, "");
        Input = WebService.escape(Input);
        if(Input.length() > 0)
        {
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences sharedPref = PreferenceManager .getDefaultSharedPreferences(this);
            long _id = Long.parseLong(Objects.requireNonNull(sharedPref.getString(SettingsActivity.KEY_PREF_CURRENT_ID, "48600231")));
            SharedPreferences.Editor ed = sharedPref.edit();
            ed.putString(SettingsActivity.KEY_PREF_CURRENT_ID, String.valueOf(_id++));
            ed.apply();
            int input = spnInputLanguague.getSelectedItemPosition();
            int output = spnOutputLanguage.getSelectedItemPosition();
            String[] languages = getResources().getStringArray(R.array.languagues_value);
            WebService.GetTranslation(this, Input, languages[input], languages[output], _id);
        }
    }
}
