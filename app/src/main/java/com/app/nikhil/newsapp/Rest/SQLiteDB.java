package com.app.nikhil.newsapp.Rest;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDB extends SQLiteOpenHelper {

        private Context context;
        public static String TITLE="TITLE";
        public static String DESCRIPTION="DESCRIPTION";
        public static String URLTOIMAGE="URLTOIMAGE";
        public static String CONTENT="CONTENT";

        public SQLiteDB(Context context) {
            super(context, "ARTICLEDETAILS", null, 6);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try {
                sqLiteDatabase.execSQL("CREATE TABLE ARTICLEDETAILS (_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+TITLE+ " VARCHAR(255),"+DESCRIPTION +" VARCHAR(255),"+URLTOIMAGE +" VARCHAR(255),"+CONTENT+" VARCHAR(255));");

            }
            catch(SQLException e)
            {

            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ARTICLEDETAILS");
            onCreate(sqLiteDatabase);
        }


}
