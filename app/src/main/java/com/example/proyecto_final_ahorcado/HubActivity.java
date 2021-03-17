package com.example.proyecto_final_ahorcado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HubActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        mAuth = FirebaseAuth.getInstance();

    }

    public void Jugar(View view) {
        Intent intent=new Intent(this,JuegoActivity.class);
        startActivity(intent);
    }

    public void Perfil(View view) {
       Intent intent=new Intent(this,PerfilActivity.class);
        startActivity(intent);
    }

    public void Estadisticas(View view) {
        Intent intent=new Intent(this, EstadisticasActivity.class);
        startActivity(intent);
    }

    public void Salir(View view) {
        mAuth.signOut();
        startActivity(new Intent(HubActivity.this, LoginActivity.class));
        finish();
    }
}