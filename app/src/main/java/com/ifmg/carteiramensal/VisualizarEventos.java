package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VisualizarEventos extends AppCompatActivity {

    private TextView tituloTxt;
    private ListView listaEventos;
    private TextView totalTxt;
    private Button novoBtn;
    private Button cancelarBtn;

    //0peração = 0 indica entrada e operação = 1indica saída
    private int operacao = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_eventos);

        tituloTxt = (TextView) findViewById(R.id.tituloTxt);
        listaEventos = (ListView) findViewById(R.id.listaEventos);
        totalTxt = (TextView) findViewById(R.id.valorTotalTxt);
        novoBtn = (Button) findViewById(R.id.novoBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        Intent intencao = getIntent();
        operacao = intencao.getIntExtra("acao", -1);
        //0 - entrada e 1 - saída

        ajustaOperacao();
        cadastrarEventos();
        cadastrarEventos();

    }

    private void cadastrarEventos(){
        novoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(operacao != -1) {

                    Intent trocaAct = new Intent(VisualizarEventos.this, CadastroEdicaoEvento.class);
                    if(operacao == 0){
                        trocaAct.putExtra("acao",0);
                    }else{
                        trocaAct.putExtra("acao", 1);
                    }

                    startActivity(trocaAct);
                }

            }
        });

    }


    private void ajustaOperacao(){
        //precisa realizar uma busca no banco a respeito dos eventos a serem apresentados na lista

        if(operacao == 0){
            tituloTxt.setText("Entradas");
        }else{
            if(operacao == 1){
                tituloTxt.setText("Saidas");
            }else{
                //erro na cofg. da intent 
                Toast.makeText(VisualizarEventos.this, "erro no parametro ação", Toast.LENGTH_LONG).show();
            }
        }

    }




}