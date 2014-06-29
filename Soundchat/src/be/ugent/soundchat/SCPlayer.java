package be.ugent.soundchat;

import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioTrack.OnPlaybackPositionUpdateListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.webkit.WebView.FindListener;

/**
 * @author jonasanseeuw Modulates a message using Frequency Modulation and plays
 *         the sound
 */
public class SCPlayer implements ISCPlayer {	

	final IMessageReceiver receiver; // class to notify that playing the message completed (to re-enable the send button)

	public SCPlayer(IMessageReceiver receiver) {
		this.receiver = receiver;
	}

	public void playSound(String message) {		
		byte generatedSound[] = generateTone(message);
		AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				Configuration.sampleRate, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, generatedSound.length,
				AudioTrack.MODE_STATIC);
			
		audioTrack.write(generatedSound, 0, generatedSound.length);		
		audioTrack.setNotificationMarkerPosition((int)(calculateNumSamples((message.length()*Configuration.charsetSize)+Configuration.beginBarker.length+Configuration.endBarker.length)*0.98));	
		audioTrack.setPositionNotificationPeriod((int)(0.1*Configuration.sampleRate)); // update progressbar every 0.1 seconds
			
		audioTrack.setPlaybackPositionUpdateListener(new OnPlaybackPositionUpdateListener() {			
			@Override
			public void onPeriodicNotification(AudioTrack at) {
				// Update play-progress to UI
				receiver.MessagePlayProgress((int)(   ((double)at.getPlaybackHeadPosition()/at.getNotificationMarkerPosition())*100)        );
			}
			
			@Override
			public void onMarkerReached(AudioTrack at) {
				System.out.println("Done playing sound...");
				receiver.onMessagePlayCompleted();
			}
		});
		
		audioTrack.play();		
	}


	/**
	 * @param message
	 * @return
	 */
	private byte[] generateTone(String message) {
		
		boolean[] messagebits = Helpers.data2Binary(message, Configuration.charsetSize); //convert message to bitpattern
		messagebits = addBarkerCodes(messagebits); // add the barkercodes			

		int numSamples = calculateNumSamples(messagebits.length);
		double[] samples = fillSampleArray(messagebits,numSamples, (int) (numSamples / (messagebits.length/4)));
		
		byte[] generatedSnd = new byte[samples.length * 2];
		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int idx = 0;
		for (final double dVal : samples) {
			// scale to maximum amplitude
			final short val = (short) ((dVal * 32767));
			// in 16 bit wav PCM, first byte is the low order byte
			generatedSnd[idx++] = (byte) (val & 0x00ff);
			generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
		}

		return generatedSnd;
	}
	
	
	private int calculateNumSamples(int bitPatternLength){		
		int numSamples= (int)(bitPatternLength/4 * Configuration.duration * Configuration.sampleRate);
		System.out.println("----------numSamples:"+numSamples);
		return numSamples;
	}
	
	private boolean[] addBarkerCodes(boolean[] message){ // add the pattern that informs the start / end of a message
		
		System.out.println("Adding barker codes");
		
		boolean[] messageWithBarker = new boolean[message.length+Configuration.beginBarker.length+Configuration.endBarker.length];
		
		int index=0;
		for (int i = 0; i < Configuration.beginBarker.length; i++,index++) { //add beginbarker
			messageWithBarker[index] = Configuration.beginBarker[i];
		}
		for (int i = 0; i < message.length; i++,index++) { // copy message
			messageWithBarker[index] = message[i];
		}
		for (int i = 0; i < Configuration.endBarker.length; i++,index++) { // add endbarker
			messageWithBarker[index] = Configuration.endBarker[i];
		}
		
		System.out.println("Message with barker:\n"+Helpers.BooleanArrayToString(messageWithBarker));
			
		return messageWithBarker;
	}
	
	
	/**
	 * Frequency Modulation part
	 * 
	 * @param bits
	 *            Binary Encoded data
	 * @param numSamples = total number of samples for the message
	 * 		numSamplesPerBit = how many samples per bit
	 * @return samples to play
	 */
	private double[] fillSampleArray(boolean[] bits, int numSamples,	int numSamplesPerBit) {
		double[] samples = new double[numSamples];

		System.out.println("Aantal bits:" + bits.length + " aantal samples:"+ numSamples + " aantal samples / bit:" + numSamplesPerBit);	
		
		String temp="";
		for (int i = 0; i < bits.length; i++) {			
			temp+= (bits[i]==true? "1" : "0");
			if(temp.length()==4){	
				System.out.printf("Temp:%s using frequency:%d\n",temp, Configuration.getFrequencyMap().get(temp).intValue());
				for (int j = 0; j < numSamplesPerBit; j++) {					
				    samples[(int) (numSamplesPerBit * Math.floor(i/4) + j)] = Math.sin(2 * Math.PI* j / (Configuration.sampleRate / Configuration.getFrequencyMap().get(temp).intValue()));			
				}				
				temp="";
			}			
			
		}

		return samples;
	}
}
