package be.ugent.soundchat.model;

import java.text.*;
import java.util.*;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

/*
 * Class that acts as our database handle. It's responsible for persisting and retrieving the chatmessages.
 * 
 */

public class MessageDBHandle extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "SOUNDCHAT_MESSAGES";
	private static final String TABLE_NAME = "MESSAGES";
	
	public static final String TABLE_ID_NAME = "MESSAGE_ID";
	public static final String TABLE_TEXT_NAME = "MESSAGE_TEXT";
	public static final String TABLE_MINE_NAME = "MESSAGE_MINE";
	public static final String TABLE_DATE_NAME = "MESSAGE_DATE";
	
	private java.text.DateFormat dateFormatter;
	
	
	public MessageDBHandle(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_stat = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s BOOLEAN, %s TIMESTAMP)",
				TABLE_NAME, TABLE_ID_NAME, TABLE_TEXT_NAME, TABLE_MINE_NAME, TABLE_DATE_NAME);
		db.execSQL(create_stat);
		Log.d("info","Table Created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
	}
	
	public void addMessage(Message m) {
		//this.onUpgrade(getWritableDatabase(), 0, 1);
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    values.put(TABLE_TEXT_NAME, m.getText());
	    values.put(TABLE_MINE_NAME, m.isMine());
	    values.put(TABLE_DATE_NAME, dateFormatter.format(m.getDate()));
	 
	    db.insert(TABLE_NAME, TABLE_ID_NAME, values);
	    db.close();	
	    Log.d("info", "Message '" + m.getText() + "' added");
	}
	
	public void deleteMessage(Message m) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_NAME, TABLE_ID_NAME + " = ?",
	            new String[] { String.valueOf(m.getID()) });
	    db.close();	
	}

	
	public Cursor getAllMessages() {
		String query = String.format("SELECT %s AS _id, %s, %s, %s FROM %s ORDER BY %s ASC",
				TABLE_ID_NAME, TABLE_TEXT_NAME, TABLE_DATE_NAME, TABLE_MINE_NAME, TABLE_NAME, TABLE_DATE_NAME);
		SQLiteDatabase db = this.getWritableDatabase();
	    
		Cursor result = db.rawQuery(query, null);    
	    return result;
	}
	
	/**
	 * Debugging method that gets an arraylist of the messages instead of the CursorAdapter
	 * @deprecated Use 'GetAllMessages' instead
	 */
	public List<Message> getMessageList() throws ParseException {
		List<Message> result = new ArrayList<Message>();
		Message tmpMessage;
		
		String query = String.format("SELECT * FROM %s ORDER BY %s ASC", TABLE_NAME, TABLE_DATE_NAME);
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(query, null);
		
	    int id_col = cursor.getColumnIndex(TABLE_ID_NAME), text_col = cursor.getColumnIndex(TABLE_TEXT_NAME),
	    		date_col = cursor.getColumnIndex(TABLE_DATE_NAME), mine_col = cursor.getColumnIndex(TABLE_MINE_NAME);
	    
	    boolean doorgaan = cursor.moveToFirst();
	    while (doorgaan) {
	    	tmpMessage = new Message(cursor.getString(text_col), cursor.getInt(mine_col) > 0, dateFormatter.parse(cursor.getString(date_col)));
	    	tmpMessage.setID(id_col);
	    	result.add(tmpMessage);
	    	
	    	doorgaan = cursor.moveToNext();
	    }
		return result;
	}
}