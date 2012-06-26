package com.ecafechat.mobilecircle.model;

import java.io.*;
import java.util.LinkedList;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ecafechat.mobilecircle.R;

public class MobileCircleDatabase extends SQLiteOpenHelper {
	
	/** The name of the database file on the file system */
    /** Keep track of context so that we can load SQL from string resources */
    private final Context mContext;
	
    private static final String DATABASE_NAME = "mobile_circle.db";
    private static String DATABASE_PATH = "";
    /** The version of the database that this class understands. */
    private static final int DATABASE_VERSION = 1;
    
    private SQLiteDatabase sqlDatabase;
    
	public MobileCircleDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        
        //this.sqlDatabase = this.getWritableDatabase();
        //this.open();
        boolean dbexist = checkdatabase();
        if (dbexist) {
        } else {
            System.out.println("Database doesn't exist");
            try {
                createdatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.sqlDatabase = this.getWritableDatabase();
	}
	
	public void open() {
        // Open the database
        String mypath = DATABASE_PATH + DATABASE_NAME;
        this.sqlDatabase = SQLiteDatabase.openDatabase(mypath, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

	public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (dbexist) {
        	System.out.println("database exists");
        } else {
        	System.out.println("database does not exist");
            this.getReadableDatabase();
            try {
            	System.out.println("copy database");
                copydatabase();
                System.out.println("database copied");
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


	private boolean checkdatabase() {
        boolean checkdb = false;
        String myPath = DATABASE_PATH + DATABASE_NAME;
        try {
            
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            System.out.println("checkdatabase -> Database doesn't exist : " + myPath);
        }

        return checkdb;
    }
	
	private void copydatabase() throws IOException {

        // Open your local db as the input stream
        InputStream myinput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        @SuppressWarnings("unused")
        String outfilename = DATABASE_PATH + DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(
                "/data/data/com.ecafechat.mobilecircle/databases/" + DATABASE_NAME);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        // Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();

    }


	@Override
	public void onCreate(SQLiteDatabase db) {
	/*	String[] sql = mContext.getString(R.string.MCDatabase_onCreate).split("\n");
        db.beginTransaction();
        try {
            // Create tables & test data
            execMultipleSQL(db, sql);
            execMultipleSQL(db, new String[] {mContext.getString(R.string.MCDatabase_onCreateData)});
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("Error creating tables and debug data", e.toString());
        } finally {
            db.endTransaction();
        }*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	* Execute all of the SQL statements in the String[] array
	* @param db The database on which to execute the statements
	* @param sql An array of SQL statements to execute
	*/
    private void execMultipleSQL(SQLiteDatabase db, String[] sql){
        for( String s : sql ) {
            if (s.trim().length()>0) {
                db.execSQL(s);
            }
        }
    }
    
    public Vector<String> getLocation(String number) {
    	Vector<String> result = new Vector<String>();
    	Cursor cursor = null;
    	try {
    		String sql = "SELECT c.desc as location, o.name as operator, o.company " +
    				"FROM circles c, operators o, circle_operator co " +
    				"WHERE co.number = ? AND co.operator=o.code AND co.circle=c.code";
    		
			cursor = sqlDatabase.rawQuery(sql, new String[] {number});
			if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
				result.add(cursor.getString(cursor.getColumnIndex("location")));
				result.add(cursor.getString(cursor.getColumnIndex("operator")));
				result.add(cursor.getString(cursor.getColumnIndex("company")));
			}
		} catch (Exception e) {
			Log.e(DATABASE_NAME, "An error occured while getting location for " + number + " : " + e.toString());
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
    	return result;
    }
    
    public String getFromDb() {
    	String result = "";
    	Cursor cursor = null;
    	try {
    		String sql = "SELECT count(*) FROM circles";
    		
			cursor = sqlDatabase.rawQuery(sql, new String[] {});
			if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
				result = "" + cursor.getString(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
    	return result;
    }
}
