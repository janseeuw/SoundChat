package be.ugent.soundchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import be.ugent.soundchat.model.Message;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import android.app.Activity;

/**
 * @author jonasanseeuw Test with Goertzel Algorihm
 * 
 *         Code should be clear, comments in code
 * 
 */
public class SCRecorder implements ISCRecorder {

	private AudioRecord record = null;
	private int bufferSize;
	
	private List<Goertzel> goertzelList;
	private int[] counters;

	final IMessageReceiver receiver; // class that receives the recorded messages	

	private Thread recordingThread;
	private SCRecorderTask recordingTask;
	
	private MessageDetector messageDetector;
	private Object[] bitPatterns = Configuration.getFrequencyMap().keySet().toArray();
	
	int  numSamplesEachTone= (int)((double)Configuration.sampleRate*Configuration.duration);
	int  numSamplesEachBin= numSamplesEachTone/10; // we want 10 measurements for each tone

	/**
	 * @param receiver
	 *            class that receives the recorded messages
	 * 
	 */
	public SCRecorder(IMessageReceiver receiver) {
		this.receiver = receiver;

	    //minBufferSize = AudioRecord.getMinBufferSize(Configuration.sampleRate,
		//				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		//minBufferSize = (int)(Configuration.sampleRate*2*Configuration.duration); // TODO:  calculate this using Configuration.duration?
		//minBufferSize = (int)(Configuration.sampleRate*Configuration.duration)*2;
		bufferSize = 44100*2;

		

		// minBuffersize has to be at least 7680
		
		record = new AudioRecord(MediaRecorder.AudioSource.MIC, Configuration.sampleRate,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);		

		System.out.println("**min buffersize:" + bufferSize);
		
		initGoertzel();

		recordingTask = new SCRecorderTask();		
		messageDetector = new MessageDetector(receiver);
	}

	private void initGoertzel() {
		goertzelList = new ArrayList<Goertzel>();
		int numSamples = bufferSize;
		int threshold = 50;
		
		counters = new int[Configuration.getFrequencyMap().size()];
		
		for (Entry<String, Integer> entry : Configuration.getFrequencyMap().entrySet())
		{
			//goertzelList.add(new Goertzel(Configuration.sampleRate, entry.getValue(), numSamples, threshold));
			goertzelList.add(new Goertzel(Configuration.sampleRate, entry.getValue(), numSamplesEachBin, threshold));
		}
		
	}

	@Override
	public void startRecording() {
		recordingThread = new Thread(recordingTask);
		recordingThread.start();
	}

	@Override
	public void stopRecording() {
		record.stop();
		try {
			recordingTask.stopRunning();
			recordingThread.join();
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	private class SCRecorderTask implements Runnable {
		private boolean continueRunning = true;

		public void stopRunning() {
			continueRunning = false;
		}

		@Override
		public void run() {    
	        // Moves the current Thread into the background
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND); //Why?
			
			short[] lin = new short[bufferSize];	
			int num1;
		
			record.startRecording();
			while (continueRunning) {
					num1 = record.read(lin, 0, bufferSize); 
					//System.out.println("Number shorts read from microphone:"+num1);
													
					for(int b=0; b<Configuration.sampleRate;b+=numSamplesEachBin){ // process each bin with Goertzel						
						short[] bin = Arrays.copyOfRange(lin,b, b+numSamplesEachBin);
						
						//System.out.printf("%d %d length subarray:%d\n",numSamplesEachTone,numSamplesEachBin,bin.length);		
					
						for(int i=0; i<goertzelList.size(); i++){
							goertzelList.get(i).processSamples(bin);
							if(goertzelList.get(i).frequencyDetected()){
									//System.out.printf("%f - mag:%f \n",goertzelList.get(i).getTargetFrequency(), goertzelList.get(i).getMagnitude());
							    	counters[i]++;		
							    	
							}
						}
						
						for(int i=0; i<counters.length; i++){					
							if(counters[i] >=8){						
									System.out.printf("******** %f >=8x gezien *****\n",goertzelList.get(i).getTargetFrequency());
									messageDetector.addBits(Helpers.StringToBooleanArray(bitPatterns[i].toString()));
									//counters[i] = 0;
									for(int j=0;j<counters.length;j++)
										counters[j]=0;
							}
						}					
					}				
			}
	   }		
	}	
}
