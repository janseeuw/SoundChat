package be.ugent.soundchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author jonasanseeuw
 * Utility methods
 */
public class Helpers {
	
	/**
	 * Converts String to boolean array
	 * @param data
	 * @param charsetSize
	 * @return
	 */
	public static boolean[] data2Binary(String data, int charsetSize) {
		boolean[] bits = new boolean[data.length() * charsetSize];

		int index = 0;
		for (char c : data.toCharArray()) {
			int asciiVal = (int) c;
			System.out.print(c+":"+asciiVal+" ");

			
			asciiVal <<= 1; // throw away most left bit. (is always a 0, cfr. ASCII-table)
			
			for (int i = 0; i < charsetSize; i++) {
				bits[index] = (asciiVal & (int)Math.pow(2,charsetSize) ) == 0 ? false : true;
				System.out.print(bits[index] + " ");
				asciiVal <<= 1; // throw away left most bit
				index++;
			}
		    System.out.println();
		}

		return bits;
	}
	

	/**
	 * Converts String to String containing 0's and 1's. (for debugging purposes)
	 * @param data
	 * @param charsetSize
	 * @return
	 */	
	public static String data2BinaryString(String data, int charsetSize){
		boolean[] array = data2Binary(data, charsetSize);
		return BooleanArrayToString(array);
	}
	
	/**
	 * Converts array of booleans to String  (inverse operation of data2Binary() )
	 * @param binary
	 * @param charsetSize
	 * @return
	 */
	public static String Binary2Data(boolean[] binary, int charsetSize){
		String result="";				
		String bitSequence="";
		int counter=0;
		
		for(boolean b:binary){
			if(b)
				bitSequence+="1";
			else
				bitSequence+="0";
			
			counter++;			
			if(counter==charsetSize){ // convert temporary bitSequence to corresponding ascii-character
				int asciiVal = Integer.parseInt(bitSequence, 2);
				System.out.println("asciival:"+asciiVal);
				result+= (char)asciiVal;
				System.out.println("Current result:"+result);
				counter=0;
				bitSequence="";
			}
		}
				
		return result;
	}
	
	public static String BooleanArrayToString(boolean[] array){
		String result="";			
		for(boolean b:array){
			if(b)
				result+="1";
			else
				result+="0";		
		}
		
		return result;
	}
	
	public static boolean[] StringToBooleanArray(String bitPattern){
		boolean[] array = new boolean[bitPattern.length()];
		int index=0;
		for(char c:bitPattern.toCharArray()){
			if(c=='0')
				array[index]=false;
			else
				array[index]=true;
			index++;
		}
		return array;		
	}
	
	

}