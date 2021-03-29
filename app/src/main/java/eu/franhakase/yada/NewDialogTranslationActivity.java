package eu.franhakase.yada;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Locale;
import java.util.Objects;

import eu.franhakase.yada.kuromoji.KanaToRomaji;
import eu.franhakase.yada.kuromoji.Kuromoji;

public class NewDialogTranslationActivity extends Activity implements Button.OnClickListener
{
    boolean bResume = false;
    KanaToRomaji kn;
    String Translit = "";
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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus)
        {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = clipboard.getPrimaryClip();
            if(   clip != null && clip.getItemCount() > 0 && clip.getItemAt(0).getText() != null)
            {
                String url = String.format(Locale.getDefault(), "https://www.deepl.com/translator#ja/en/%s", Uri.encode(clip.getItemAt(0).getText().toString()));
                Translit = clip.getItemAt(0).getText().toString() + "\r\n\r\n-\r\n\r\n" + kn.convert(Kuromoji.Tokenize(clip.getItemAt(0).getText().toString()));
                WebView wvMain = findViewById(R.id.wvMain);
                wvMain.getSettings().setJavaScriptEnabled(true);
                wvMain.loadUrl(Objects.requireNonNull(url));
            }

        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void mostrarRomanizado()
    {
        LayoutInflater inflater= LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.romaji_dialog_layout, null);
        TextView textview = view.findViewById(R.id.txtResult);
        textview.setText(Translit);
        new AlertDialog.Builder(this)
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
}