package be.ugent.soundchat;

import java.util.HashMap;


public class Configuration {

	/* 
	 * Contains the shared variables between the Player (modulation) and Recorder (demodulation)
	 *
	 */
	
	public static final int sampleRate = 44100;   // Hz
	
	public static final double duration = 0.5; // duration in seconds for 4 bit
	public static final int charsetSize = 8; 	
	
	public static final boolean[] beginBarker = new boolean[]{false,false,false,false,false,false,false,false}; 
	public static final boolean[] endBarker =   new boolean[]{true,true,true,true,true,true,true,true};
	
 
	private static HashMap<String,Integer> frequencyMap; // e.g. to encode/decode 0000 use 400Hz as frequency

	public static HashMap<String,Integer> getFrequencyMap() {
		if(frequencyMap==null){
			frequencyMap = new HashMap<String, Integer>();
			frequencyMap.put("0000", 400);
			frequencyMap.put("0001", 600);
			frequencyMap.put("0010", 800);
			frequencyMap.put("0011", 1000);
			frequencyMap.put("0100", 1200);
			frequencyMap.put("0101", 1400);
			frequencyMap.put("0110", 1800);
			frequencyMap.put("0111", 2000);
			frequencyMap.put("1000", 2200);
			frequencyMap.put("1001", 2400);
			frequencyMap.put("1010", 2600);
			frequencyMap.put("1011", 2800);
			frequencyMap.put("1100", 3000);
			frequencyMap.put("1101", 3200);
			frequencyMap.put("1110", 3400);
			frequencyMap.put("1111", 1600);
		}
		
		return frequencyMap;
	}


	
	
}
