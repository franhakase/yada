package eu.franhakase.yada;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import java.util.Locale;
import java.util.Objects;
import eu.franhakase.yada.kuromoji.KanaToRomaji;
import eu.franhakase.yada.kuromoji.Kuromoji;

public class NewDialogTranslationActivity extends Activity implements Button.OnClickListener, ClipboardManager.OnPrimaryClipChangedListener
{
    private KanaToRomaji kn;
    private String Original = "";
    boolean bResume = false;
    boolean bFirstFinish = false;
    private WebView wvMain;
    private ClipboardManager clipboard;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_translation_dialog);
        kn = new KanaToRomaji();
        LinearLayout ll = findViewById(R.id.dlMain);
        ll.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_rounded_rectangle));
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.gravity = Gravity.TOP;
        getWindow().setAttributes(a);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Button btTransliterate = findViewById(R.id.btTransliterate);
        btTransliterate.setOnClickListener(this);
        wvMain = findViewById(R.id.wvMain);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                if(!bFirstFinish)
                {
                    bFirstFinish = true;
                    if(url.toLowerCase().contains("deepl.com"))
                    {
                        wvMain.loadUrl(getResources().getString(R.string.app_deepl_webpage_cleanup));
                    }
                }
            }
        });

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus && !bResume)
        {
            bResume = true;
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.addPrimaryClipChangedListener(this);
            ClipData clip = clipboard.getPrimaryClip();
            if(   clip != null && clip.getItemCount() > 0 && clip.getItemAt(0).getText() != null)
            {
                Original = clip.getItemAt(0).getText().toString();
                String url = String.format(Locale.getDefault(), "https://www.deepl.com/translator#auto/en/%s", Uri.encode(clip.getItemAt(0).getText().toString()));
                wvMain.loadUrl(Objects.requireNonNull(url));
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private String Transliterate(int mode)
    {
        if(mode == 0)
        {
            return String.format("%s\r\n%s", Original, kn.convert(this, Kuromoji.Tokenize(Original)));
        }
        else if (mode == 1)
        {
            return Kuromoji.TokenizeKaraoke(this, Original);
        }
        return Original;
    }

    private void mostrarRomanizado()
    {
        LayoutInflater inflater= LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.romaji_dialog_layout, null);
        final TextView textview = view.findViewById(R.id.txtResult);
        String translit = "";
        textview.setText(translit);
        textview.setMovementMethod(new ScrollingMovementMethod());
        Spinner spnMode = view.findViewById(R.id.spnRomajiMode);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.lst_romaji_mode));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMode.setAdapter(spinnerArrayAdapter);
        spnMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                textview.setText(Transliterate(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        new AlertDialog.Builder(this, R.style.yada_dialog)
                .setTitle("Resultado da romanização:")
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_yada_notif)
                .show();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btTransliterate)
        {
            mostrarRomanizado();
        }
    }

    @Override
    protected void onDestroy()
    {
        clipboard.removePrimaryClipChangedListener(this);
        //Toast.makeText(this, "safe", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public void onPrimaryClipChanged()
    {
        ClipData clip = clipboard.getPrimaryClip();
        if(clip != null && clip.getItemCount() > 0 && clip.getItemAt(0).getText() != null)
        {
            Original = clip.getItemAt(0).getText().toString();
            Toast.makeText(this, "Novo conteúdo pronto para transliteração!", Toast.LENGTH_SHORT).show();
        }
    }
}