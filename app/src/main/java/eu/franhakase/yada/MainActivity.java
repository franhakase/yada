package eu.franhakase.yada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btAtvar = findViewById(R.id.btAtivar);
        btAtvar.setOnClickListener(this);
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
            return false;
            //Intent intent = new Intent(this, SettingsActivity.class);
            //startActivity(intent);
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btAtivar)
        {
            startClipToTranslate();
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
            finish();
        }
        else
        {
            errorToast();
        }
    }
}