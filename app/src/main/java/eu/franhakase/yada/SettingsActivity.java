package eu.franhakase.yada;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    public static final String KEY_PREF_EXAMPLE_SWITCH = "swp_ativar_servico";
    public static final String KEY_PREF_CURRENT_ID = "swp_id_api";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }}
