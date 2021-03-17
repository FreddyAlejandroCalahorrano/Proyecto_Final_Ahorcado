package com.example.proyecto_final_ahorcado;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proyecto_final_ahorcado.model.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.Random;

public class JuegoActivity extends AppCompatActivity {
    //Creamos las variables
    private FirebaseAuth mAuth;
    EditText nomJ;
    String ID, nomJBD, ScoreR;
    String email;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
//////////////////////////////////////////

    private String[] words;
    private Random random;
    private String currWord;
    private TextView[] charViews;
    private LinearLayout wordLayout;
    private LetterAdapter adapter;
    private GridView gridView;
    private int numCorr;
    private int numChars;
    private ImageView[] parts;
    private int sizeParts=6;
    private int currPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        words=getResources().getStringArray(R.array.words);
        wordLayout=findViewById(R.id.words);
        gridView=findViewById(R.id.letters);
        random=new Random();
        parts=new ImageView[sizeParts];
        parts[0] = findViewById(R.id.head);
        parts[1] = findViewById(R.id.body);
        parts[2] = findViewById(R.id.armLeft);
        parts[3] = findViewById(R.id.armRight);
        parts[4] = findViewById(R.id.legLeft);
        parts[5] = findViewById(R.id.legRight);

        jugarJuego();
        //////////////////////////////////////////////////////////////////

/////////////////////////////
    }
    private void jugarJuego(){
        cargarDatos();
        String newWord=words[random.nextInt(words.length)];

        while(newWord.equals(currWord))newWord=words[random.nextInt(words.length)];
        currWord=newWord;

        charViews=new  TextView[currWord.length()];

        wordLayout.removeAllViews();
        for(int i=0; i<currWord.length(); i++){
            charViews[i]=new TextView(this);
            charViews[i].setText(""+currWord.charAt(i));
            charViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ));
            charViews[i].setGravity(Gravity.CENTER);
            charViews[i].setTextColor(Color.WHITE);
            charViews[i].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[i]);
        }
        adapter=new LetterAdapter(this);
        gridView.setAdapter(adapter);
        numCorr=0;
        currPart=0;
        numChars=currWord.length();

        for(int i=0; i<sizeParts; i++){
            parts[i].setVisibility(View.INVISIBLE);
        }
    }
    public void letterPressed(View view){
        String letter=((TextView)view).getText().toString();
        char letterChar=letter.charAt(0);

        view.setEnabled(false);

        boolean correct=false;
        for(int i=0; i<currWord.length(); i++){
            if(currWord.charAt(i)==letterChar){
                correct=true;
                numCorr++;
                charViews[i].setTextColor(Color.BLACK);
            }
        }
        if(correct){
            if(numCorr==numChars){
                inicializarFirebase();
                SumPuntaje();
                disableButtons();
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Ganaste");
                builder.setMessage("Felicidades! \n\n La respuesta era \n\n"+currWord);
                builder.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JuegoActivity.this.jugarJuego();
                    }
                });

                builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JuegoActivity.this.finish();
                    }
                });
                builder.show();
            }
        }
        else if(currPart<sizeParts){
            parts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        }
        else{
            disableButtons();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Perdiste");
            builder.setMessage("Tu perdiste! \n\n La respuesta era \n\n"+currWord);
            builder.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JuegoActivity.this.jugarJuego();
                }
            });

            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JuegoActivity.this.finish();
                }
            });
            builder.show();
        }
    }
    public void disableButtons(){
        for (int i=0; i<gridView.getChildCount();i++){
            gridView.getChildAt(i).setEnabled(false);
        }
    }

    public void EndGame(View view) {
        Intent intent=new Intent(this,HubActivity.class);
        startActivity(intent);
    }
    public void cargarDatos(){
        ///Accedemos a los datos del Jugador
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email = user.getEmail();
        ID = email.replaceAll(".com", "");
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Jugador/"+ ID);
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<Jugador> t = new GenericTypeIndicator<Jugador>() {};
                Jugador player = dataSnapshot.getValue(t);
                nomJBD = player.getNombre();
                Log.d("nombre",nomJBD);
                ScoreR = player.getPuntaje();
                Log.d("Puntaje",ScoreR);

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }
    public void SumPuntaje(){
        int valor = Integer.parseInt(ScoreR);
        valor = valor+100;
        String ScoreE = String.valueOf(valor);
        Jugador j = new Jugador();
        j.setUid(ID);
        j.setNombre(nomJBD);
        j.setPuntaje(ScoreE);
        databaseReference.child("Jugador").child(j.getUid()).setValue(j);
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}