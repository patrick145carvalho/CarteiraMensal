package com.ifmg.carteiramensal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ferramentas.EventosDB;
import modelo.Evento;

public class CadastroEdicaoEvento extends AppCompatActivity {

    private DatePickerDialog calendarioUsuario;
    private TextView tituloTxt;
    private EditText nomeTxt;
    private EditText valorTxt;
    private TextView dataTxt;
    private CheckBox repeteBtn;
    private ImageView foto;
    private Button fotoBtn;
    private Button salvarBtn;
    private Button cancelarBtn;
    private Calendar calendarioTemp;
    private Spinner mesesRepeteSpi;

    //0 - cadastro de entrada, 1- cadastro de saída, 2- edição de entrada, 3- edição de saída
    private int acao = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_edicao_evento);

        tituloTxt = (TextView) findViewById(R.id.tituloCadastroTxt);
        nomeTxt = (EditText) findViewById(R.id.nomeCadastroTxt);
        valorTxt = (EditText) findViewById(R.id.valorCadastroTxt);
        dataTxt = (TextView) findViewById(R.id.dataCadastroTxt);
        repeteBtn = (CheckBox) findViewById(R.id.repeteBtn);
        foto = (ImageView) findViewById(R.id.fotoCadastro);
        fotoBtn = (Button) findViewById(R.id.fotoBtn);
        salvarBtn = (Button) findViewById(R.id.salvarCadastroBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarCadastroBtn);
        mesesRepeteSpi = (Spinner) findViewById(R.id.mesesSpiner);

        Intent intencao = getIntent();
        acao = intencao.getIntExtra("acao", -1);

        ajustaPorAcao();
        cadastraEventos();
        confSpinners();

    }
    private void confSpinners(){
        List<String> meses = new ArrayList<>();
        //vamos permitir nessa versão a repetição de apenas 24 meses de um evento
        for(int i = 1; i<= 24; i++){
            meses.add(i+"");
        }
        ArrayAdapter<String> listaAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, meses);
        mesesRepeteSpi.setAdapter(listaAdapter);
        mesesRepeteSpi.setEnabled(false);
    }

    private void cadastraEventos(){
        //Configurando o DatePicher
        calendarioTemp = Calendar.getInstance();
        calendarioUsuario = new DatePickerDialog(CadastroEdicaoEvento.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                calendarioTemp.set(ano, mes, dia);
                dataTxt.setText(dia+"/"+(mes+1)+"/"+ano);
            }
        }, calendarioTemp.get(Calendar.YEAR), calendarioTemp.get(Calendar.MONTH), calendarioTemp.get(Calendar.DAY_OF_MONTH));

        dataTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarioUsuario.show();
            }
        });

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarNovoEvento();
            }
        });
        //Tratando a repetição do evento

        repeteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeteBtn.isChecked()){
                    mesesRepeteSpi.setEnabled(true);
                }else{
                    mesesRepeteSpi.setEnabled(false);
                }
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Termina a execuçao de uma Activity e retorna a anterior
                finish();
            }
        });
    }


    // método que auxilia na reutilização da activity, altera valores dos componentes reutilizáveis
    private void ajustaPorAcao(){
        // recuperando a data de hj

        Calendar hoje = Calendar.getInstance();

        SimpleDateFormat formatador = new SimpleDateFormat("dd,MM,yyyy");
        dataTxt.setText(formatador.format(hoje.getTime()));


        switch (acao){
            case 0:{
                tituloTxt.setText("Cadast. Entrada");
            }break;
            case 1:{
                tituloTxt.setText("Cadast. Saída");
            }break;
            case 2:{
                //edição de entradas
                tituloTxt.setText("Edição Entrada");
            }break;
            case 3:{//edição de saídas
                tituloTxt.setText("Edição Saída");
            }break;
            default:{

            }

        }

    }
    private void cadastrarNovoEvento(){

        String nome = nomeTxt.getText().toString();
        Double valor = Double.parseDouble(valorTxt.getText().toString());

        if(acao == 1 || acao == 3){
            valor *= -1;
        }

        //SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        //String dataStr = dataTxt.getText().toString();

        //try {

            Date diaEvento = calendarioTemp.getTime();
            //um novo calendario para calcular a data limite (repetição)
            Calendar dataLimite = Calendar.getInstance();
            dataLimite.setTime(calendarioTemp.getTime());

            //verificando se esse evento irá repetir por alguns meses
            if(repeteBtn.isChecked()){
            //por enquanto estamos considerando apenas um mes
                String mesStr = (String)mesesRepeteSpi.getSelectedItem();
                dataLimite.add(Calendar.MONTH, Integer.parseInt(mesStr));
            }
            //setando para o ultimo dia do mes limite
            dataLimite.set(Calendar.DAY_OF_MONTH, dataLimite.getActualMaximum(Calendar.DAY_OF_MONTH));

            Evento novoEvento = new Evento(nome, valor, new Date(), dataLimite.getTime(), diaEvento, null);

            //inserir esse evento no banco de dados
            EventosDB bd = new EventosDB(CadastroEdicaoEvento.this);
            bd.insereEvento(novoEvento);

            Toast.makeText(CadastroEdicaoEvento.this, "Cadastro feito com sucesso", Toast.LENGTH_LONG).show();

            finish();

        /*}catch (ParseException ex){
            System.err.println("erro no formato da data....");
        }*/

    }

}