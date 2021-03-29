package eu.franhakase.yada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import eu.franhakase.yada.kuromoji.Kuromoji;

public class SplashScreen extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        Thread t = new Thread()
        {
            @Override
            public void run()
            {
                Kuromoji.setup();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        t.start();
    }
}