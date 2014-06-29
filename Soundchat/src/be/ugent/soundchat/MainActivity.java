package be.ugent.soundchat;

import be.ugent.soundchat.model.Message;
import be.ugent.soundchat.model.MessageDBHandle;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity implements IMessageReceiver {

	private ISCPlayer player = new SCPlayer(this);
	private ISCRecorder recorder = new SCRecorder(this);

	private Button btnSend;
	private EditText txtMessage;
	private ProgressBar progressBarPlaying;

	private ListView lvChat;

	private CursorAdapter adapter;
	private MessageDBHandle handle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handle = new MessageDBHandle(this);

		setContentView(R.layout.activity_main);

		btnSend = (Button) findViewById(R.id.btnSend);
		txtMessage = (EditText) findViewById(R.id.txtMessage);
		progressBarPlaying = (ProgressBar) findViewById(R.id.progress);
		lvChat = (ListView) findViewById(R.id.lvChat);

		adapter = new MessageCursorAdapter(this, handle.getAllMessages());
		lvChat.setAdapter(adapter);
		lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); // auto
																			// scroll
																			// to
																			// last
																			// message
		lvChat.setStackFromBottom(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // don't
																							// automatically
																							// display
																							// keyboard
																							// on
																							// startup
		// set clicklistener on sendbutton
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (!txtMessage.getText().toString().isEmpty()) {
					player.playSound(txtMessage.getText().toString());						
					btnSend.setEnabled(false);
					
					handle.addMessage(new Message(txtMessage.getText().toString(), true));
					refreshMessages();
					txtMessage.setText("");

					// hide the keyboard
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				}				
			}
		});


		// when user presses volume buttons, change music audio
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Set audiovolume to percentage of maximum audiovolume (otherwise it is possible that the phone is on silent mode and playing won't work)
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		float percent = 0.30f;
		int volumePercent = (int)(maxVolume*percent);	
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumePercent, 0);
		recorder.startRecording();		

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//recorder.startRecording(); 
	}

	@Override
	protected void onPause() {
		super.onPause();
		//recorder.stopRecording();
	}
	

	private void refreshMessages() {
		adapter = new MessageCursorAdapter(this, handle.getAllMessages());
		// adapter.notifyDataSetChanged();
		lvChat.setAdapter(adapter);
	}

	@Override
	public void onMessageReceived(Message m) {
		// do something with the message, e.g. add to the database handle
		handle.addMessage(m);
		refreshMessages();
	}


	@Override
	public void onMessagePlayCompleted() {
		// re-enable the send button because the previous message has completed playing			
		btnSend.setEnabled(true);
	}

	@Override
	public void MessagePlayProgress(int percentageCompleted) {
		// update the progress bar in UI
		progressBarPlaying.setProgress(percentageCompleted);                
		
	}
	
	
}
