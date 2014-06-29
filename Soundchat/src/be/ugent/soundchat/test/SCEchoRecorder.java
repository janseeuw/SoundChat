package be.ugent.soundchat.test;

import be.ugent.soundchat.ISCRecorder;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * @author jonasanseeuw
 * Records with microphone as audiosource and echoes 
 */

/* class NO LONGER USED */

public class SCEchoRecorder implements ISCRecorder {

	private int sampleRate = 8000; // Hz
	private AudioRecord record = null;
	private AudioTrack track = null;
	
	public SCEchoRecorder(){
		int min = AudioRecord.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		record = new AudioRecord(MediaRecorder.AudioSource.MIC,
				sampleRate, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, min);
		
		int maxJitter = AudioTrack.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				maxJitter, AudioTrack.MODE_STREAM);
	}
	
	@Override
	public void startRecording() {
		(new Thread() {
			@Override
			public void run() {
				recordAndPlay();
			}
		}).start();
	}

	@Override
	public void stopRecording() {
		record.stop();
	}
	
	private void recordAndPlay() {
		short[] lin = new short[1024];
		int num = 0;
		record.startRecording();
		track.play();
		while (true) {
			num = record.read(lin, 0, 1024);
			track.write(lin, 0, num);
		}
	}

}
