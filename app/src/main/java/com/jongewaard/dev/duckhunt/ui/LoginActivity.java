package com.jongewaard.dev.duckhunt.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jongewaard.dev.duckhunt.Common.Constantes;
import com.jongewaard.dev.duckhunt.Models.User;
import com.jongewaard.dev.duckhunt.R;

public class LoginActivity extends AppCompatActivity {

    EditText etNick;
    Button btnStart;
    String nick;


    //Objeto que permite conectarse a Firestore
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciar la conexion a Firestore
        db = FirebaseFirestore.getInstance();

        etNick = (EditText)findViewById(R.id.editTextNick);
        btnStart = (Button)findViewById(R.id.buttonStart);

        // Cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);


        // Eventos: evento click
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etNick.getText().toString();

                if(nick.isEmpty()){
                    etNick.setError("El nombre de usuario es obligatorio");
                }else if(nick.length() < 3) {
                    etNick.setError("Debe tener al menos 3 caracteres");
                }else{

                    addNickAndStart();


                }
            }
        });
    }

    private void addNickAndStart() {

        //Introduzco en la base de datos de Firebase el nick de usuario
        db.collection("users").whereEqualTo("nick", nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0){
                            etNick.setError("El nick no está disponible");
                        }else{
                            addNickToFirestore();
                        }
                    }
                });


    }

    private void addNickToFirestore() {

        final User nuevoUsuario = new User(nick, 0);

        db.collection("users")
                .add(nuevoUsuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");
                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                        i.putExtra(Constantes.EXTRA_NICKS, nick);
                        i.putExtra(Constantes.EXTRA_ID, documentReference.getId());
                        startActivity(i);
                    }
                });
    }
}
