package be.ugent.soundchat;

import be.ugent.soundchat.model.MessageDBHandle;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageCursorAdapter extends CursorAdapter {

	public MessageCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}


	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.chat_bubble, parent, false);
        return retView;
	}

	@Override
	public void bindView(View row, Context context, Cursor cursor) {
		LinearLayout wrapper = (LinearLayout)row.findViewById(R.id.wrapper);
		TextView txtMessage = (TextView) row.findViewById(R.id.message);
		
		int text_column = cursor.getColumnIndex(MessageDBHandle.TABLE_TEXT_NAME),
				mine_column = cursor.getColumnIndex(MessageDBHandle.TABLE_MINE_NAME);
		boolean mine = cursor.getInt(mine_column) > 0;
		
		txtMessage.setText(cursor.getString(text_column));
		txtMessage.setBackgroundResource(mine? R.drawable.bubble_green : R.drawable.bubble_yellow);
		wrapper.setGravity(mine? Gravity.RIGHT : Gravity.LEFT);
	}

}