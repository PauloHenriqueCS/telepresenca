package com.sinch.android.rtc.sample.video;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    private static final String TAG = "sql";
    // Nome do banco
    public static final String NOME_BANCO = "nurse.sqlite";
    private static final int VERSAO_BANCO = 1;
    private static final Line ID = null;

    public DB(Context context) {
        // context, nome do banco, factory, versão
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Criando a Tabela enfermeira...");
        db.execSQL("create table if not exists Enfermeira (_id integer primary key autoincrement,glicose text, pressao text, dia text, hora text );");
        Log.d(TAG, "Tabela Enfermeira criada com sucesso.");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Caso mude a versão do banco de dados, podemos executar um SQL aqui
    }
    // Insere um novo Line, ou atualiza se já existe
    public long save(Line line ) {

        long id = line.id;


        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("glicose", line.glicose);
            values.put("pressao", line.pressao);
            values.put("dia", line.dia);
            values.put("hora", line.hora);


            if (id != 0) {
                String _id = String.valueOf(id);
                String[] whereArgs = new String[]{_id};
                // update Line set values = ... where _id=?
                int count = db.update("enfermeira", values, "_id=?", whereArgs);
                return count;
            } else {
                // insert into Line values (...)
                id = db.insert("enfermeira", "", values);
                return id;
            }
        } finally {
            db.close();
        }
    }

    //
//
//    // Deleta o Line
//    public int delete(Line Line) {
//        SQLiteDatabase db = getWritableDatabase();
//        try {
//            // delete from Line where _id=?
//            int count = db.delete("Line", "_id=?", new String[]{String.valueOf(Line.id)});
//            Log.i(TAG, "Deletou [" + count + "] registro.");
//            return count;
//        } finally {
//            db.close();
//        }
//    }
//    // Deleta os Lines do tipo fornecido
//    public int deleteLinesByTipo(String tipo) {
//        SQLiteDatabase db = getWritableDatabase();
//        try {
//            // delete from Line where _id=?
//            int count = db.delete("Line", "tipo=?", new String[]{tipo});
//            Log.i(TAG, "Deletou [" + count + "] registros");
//            return count;
//        } finally {
//            db.close();
//        }
//    }
    // Consulta a lista com todos os Lines
    public List<Line> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // select * from Line
            Cursor c = db.query("enfermeira", null, null, null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }

    public Cursor findAllc() {
        SQLiteDatabase db = getWritableDatabase();
        try {

            Cursor c = db.query("enfermeira", null, null, null, null, null, "_id" + " DESC");
            return c;
        } finally {
           // db.close();
        }
    }





    // Consulta o Line pelo tipo
    public List<Line> findAllByTipo(String dia) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            // "select * from Line where tipo=?"
            Cursor c = db.query("Line", null, "tipo = '" + dia + "'", null, null, null, null);
            return toList(c);
        } finally {
            db.close();
        }
    }
    //    // Lê o cursor e cria a lista de Lines
    public ArrayList<Line> toList(Cursor c) {
        ArrayList<Line> line = new ArrayList<Line>();
        if (c.moveToFirst()) {
            do {
                Line Lines = new Line();
                line.add(Lines);
                // recupera os atributos de Line
                Lines.id = c.getLong(c.getColumnIndex("_id"));
                Lines.glicose = c.getString(c.getColumnIndex("glicose"));
                Lines.pressao = c.getString(c.getColumnIndex("pressao"));
                Lines.dia = c.getString(c.getColumnIndex("dia"));
                Lines.hora = c.getString(c.getColumnIndex("hora"));
            } while (c.moveToNext());
        }
        return line;
    }


    // Executa um SQL
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }
    // Executa um SQL
    public void execSQL(String sql, Object[] args) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql, args);
        } finally {
            db.close();
        }
    }
}
