package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ferramentas.EventosDB;

public class MainActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView entrada;
    private TextView saida;
    private TextView saldo;
    private ImageButton entraBtn;
    private ImageButton saidaBtn;
    private Button anteriorBtn;
    private Button proxBtn;
    private Button novoBtn;
    private Calendar hoje;
    private Calendar dataApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // criando link entre os elementos Java x Xml
        titulo = (TextView) findViewById(R.id.tituloMain);
        entrada = (TextView) findViewById(R.id.entradaTxt);
        saida = (TextView) findViewById(R.id.saidaTxt);
        saldo = (TextView) findViewById(R.id.saldoTxt);

        entraBtn = (ImageButton) findViewById(R.id.entradaBtn);
        saidaBtn = (ImageButton) findViewById(R.id.saidaBtn);

        anteriorBtn = (Button) findViewById(R.id.anteriorBtn);
        proxBtn = (Button) findViewById(R.id.proximoBtn);
        novoBtn = (Button) findViewById(R.id.novoBtn);
        //responsável por emplementar todos os eventos de botões
        cadastroEventos();


        //recupera data e hora atual
        dataApp = Calendar.getInstance();
        hoje = Calendar.getInstance();

        mostraDataApp();

    }
    private void cadastroEventos(){
        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(-1);
            }
        });

        proxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaMes(1);
            }
        });

        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventosDB db = new EventosDB(MainActivity.this);
                //db.insereEvento();


                //Toast.makeText(MainActivity.this, db.getDatabaseName(), Toast.LENGTH_LONG).show();
            }
        });

        entraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MainActivity.this, VisualizarEventos.class);

                trocaAct.putExtra("acao", 0);

                startActivity(trocaAct);
            }
        });

        saidaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MainActivity.this, VisualizarEventos.class);

                trocaAct.putExtra("acao", 1);
                //pedimos para iniciar a activity passada como parametro
                startActivity(trocaAct);
            }
        });

    }


    private void  mostraDataApp(){
        String nomeMes[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
                "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

        int mes = dataApp.get(Calendar.MONTH);
        int ano = dataApp.get(Calendar.YEAR);

        titulo.setText(nomeMes[mes] + "/" + ano);

    }
    private void atualizaMes(int ajuste){
        dataApp.add(Calendar.MONTH, ajuste);
        //próximo ês não pode passar do mês atual
        if(ajuste > 0){
            if(dataApp.after(hoje)){
                dataApp.add(Calendar.MONTH,-1);
            }
        }else{
            //busca no banco de dados para avaliar se há dados dos messes anteriores
        }


        mostraDataApp();
    }

}