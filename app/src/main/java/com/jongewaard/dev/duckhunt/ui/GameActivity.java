package com.jongewaard.dev.duckhunt.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jongewaard.dev.duckhunt.Common.Constantes;
import com.jongewaard.dev.duckhunt.R;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvCounterDucks, tvTimer, tvNick;
    ImageView ivDuck;
    int counter = 0;
    int anchoPantalla, altoPantalla;
    int width, height;
    Random aleatorio;
    boolean gameOver = false;
    String nick, id;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponents();
        eventos();
        initPantalla();
        moveDuck();
        initCuentaAtras();


    }

    private void initCuentaAtras() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                mostrarDialogoGameOver();
                saveResultFirestore();
            }
        }.start();
    }

    private void saveResultFirestore() {

        db.collection("users")
                .document(id)
                .update(
                        "duck", counter
                );
    }

    private void mostrarDialogoGameOver() {
    // 1. Instantiate an AlertDialog.Builder with its constructor
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    // 2. Chain together various setter methods to set the dialog characteristics
    builder.setMessage("Has conseguido cazar " + counter + " patos")
        .setTitle("Game over");


        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                counter = 0;
                tvCounterDucks.setText("00000");
                gameOver = false;
                initCuentaAtras();
                moveDuck();

            }
        });
        builder.setNegativeButton("ver Ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });

        // 3. Get the AlertDialog from create()
    AlertDialog dialog = builder.create();

    //4. mostrar el dialogo
    dialog.show();


    }

    private void initPantalla() {
        //1. Obtener el tamaño de la pantalla del dispositivo
        // en el que estamos ejecutando la app
        Display display = getWindowManager().getDefaultDisplay(); //Obtengo la resolucion de la pantalla
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;

        //2. Inicializamos el objeto para generar numeros aleatorios
        aleatorio = new Random();

    }

    private void initViewComponents() {

        tvCounterDucks = (TextView)findViewById(R.id.textViewCounter);
        tvTimer = (TextView)findViewById(R.id.textViewTimer);
        tvNick = (TextView)findViewById(R.id.textViewNick);
        ivDuck = (ImageView)findViewById(R.id.imageViewDuck);

        // Cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        // Extras: obtener nick y setear en TextView
        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICKS);

        id = extras.getString(Constantes.EXTRA_ID);

        tvNick.setText(nick);
    }

    private void eventos() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gameOver) { //si No es  game over igual a TRUE
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));

                    //la imagen cuando el pato esta presionado
                    ivDuck.setImageResource(R.drawable.duck_clicked);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Restaura la imagen y la setea en otra posicion
                            ivDuck.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 500);
                }
            }
        });
    }

    private void moveDuck() {

        int min = 0;
        int maximoX = anchoPantalla - ivDuck.getWidth();
        int maximoY = altoPantalla - ivDuck.getHeight();

        //Toast.makeText(this, "El tamaó X es: "+ maximoX + " y el tamaó Y es: " + maximoY, Toast.LENGTH_SHORT).show();

        // Generamos 2 numeros aleatorios, uno para la coordenada
        // x y otro para la coordenada y.
        int randomX = aleatorio.nextInt(((maximoX - min) + 1) + min); //formula que permite calcular un numero
                                                                    // aleatorio entre un minimo y un maximo

        int randomY = aleatorio.nextInt(((maximoY - min) + 1) + min);

        //Utilizamos los numeros leatorios para mover el pato a esa posicion
        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
    }
}
