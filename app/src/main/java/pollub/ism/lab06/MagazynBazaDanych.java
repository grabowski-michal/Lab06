package pollub.ism.lab06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MagazynBazaDanych extends SQLiteOpenHelper {
    private static final String NAZWA_BAZY = "Stoisko z warzywami";
    static final int WERSJA = 1;
    public static final String NAZWA_TABELI = "Warzywniak";
    public static final String NAZWA_KOLUMNY_1 = "NAME";
    public static final String NAZWA_KOLUMNY_2 = "QUANTITY";

    private  final Context kontekst;

    MagazynBazaDanych(Context context){
        super(context,NAZWA_BAZY,null,WERSJA);
        kontekst = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlString = "CREATE TABLE " + NAZWA_TABELI + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + NAZWA_KOLUMNY_1 + " TEXT, " + NAZWA_KOLUMNY_2 +" INTEGER)";
        sqLiteDatabase.execSQL(sqlString);

        ContentValues krotka = new ContentValues();

        String[] asortyment = kontekst.getResources().getStringArray(R.array.Asortyment);

        for(String pozycja : asortyment){
            krotka.clear();
            krotka.put(NAZWA_KOLUMNY_1,pozycja);
            krotka.put(NAZWA_KOLUMNY_2,0);
            sqLiteDatabase.insert(NAZWA_TABELI,null,krotka);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Nie będziemy implementować, ale musi być
    }

    public Integer podajIlosc(String wybraneWarzywo){

        Integer ilosc = null;
        SQLiteDatabase bazaDoOdczytu = null;
        Cursor kursor = null;

        try {

            bazaDoOdczytu = getReadableDatabase();

            kursor = bazaDoOdczytu.query(
                    MagazynBazaDanych.NAZWA_TABELI,
                    new String[]{MagazynBazaDanych.NAZWA_KOLUMNY_2},
                    MagazynBazaDanych.NAZWA_KOLUMNY_1 + "=?", new String[]{wybraneWarzywo},
                    null,null, null);

            kursor.moveToFirst();
            ilosc = kursor.getInt(0);

        } catch(SQLException ex){
            // Należałoby to jakoś obsłużyć...
        } finally {
            if(kursor!=null) kursor.close();
            if(bazaDoOdczytu!=null) bazaDoOdczytu.close();
        }

        return ilosc;
    }

    public void zmienStanMagazynu(String wybraneWarzywo, int nowaIlosc){

        SQLiteDatabase bazaDoZapisu = null;

        try{
            bazaDoZapisu = getWritableDatabase();

            ContentValues krotka = new ContentValues();
            krotka.put(NAZWA_KOLUMNY_2, Integer.toString(nowaIlosc));

            bazaDoZapisu.update(NAZWA_TABELI, krotka, NAZWA_KOLUMNY_1 + "=?", new String[]{wybraneWarzywo});

        }catch (SQLException ex){
            // Należałoby to jakoś obsłużyć...
        }finally {
            if(bazaDoZapisu != null) bazaDoZapisu.close();
        }
    }
}
