package ferramentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import modelo.Evento;

public class EventosDB extends SQLiteOpenHelper {
    private Context contexto;

    public EventosDB(Context cont){
        super(cont, "evento", null, 1);
        contexto = cont;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String criaTabela ="CREATE TABLE IF NOT EXISTS evento(id INTEGER PRIMARY KEY AUTOINCREMENT, NOME TEXT,"+
        "valor REAL, imagem TEXT, dataocorreu DATE, datacadastro DATE, datavalida DATE)";

        db.execSQL(criaTabela);
    }

    public void insereEvento(Evento novoEvento){

        try(SQLiteDatabase db = this.getWritableDatabase()){

            ContentValues valores = new ContentValues();

            valores.put("nome", novoEvento.getNome());
            valores.put("valor", novoEvento.getValor());
            valores.put("imagem", novoEvento.getCaminhoFoto());
            valores.put("dataocorreu", novoEvento.getOcorreu().getTime());
            valores.put("datacadastro", new Date().getTime());
            valores.put("dataValida", novoEvento.getValida().getTime());


            db.insert("evento", null, valores);

        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }
    public void atualizaEvento(){

    }
    public void buscaEvento(){

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //ficará parado até a ativação da activity de update (funcionalidade)
    }
}
