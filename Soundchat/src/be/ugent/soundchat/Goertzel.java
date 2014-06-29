package be.ugent.soundchat;

import java.lang.Math;
import java.lang.Byte;

/**
 */
public class Goertzel {

	private float sampleRate;
	private float targetFrequency;
	private float numSamples;
	float threshold;
  
	private float coeff, sine, cosine, scalingFactor; // can be precomputed once
	private float q1, q2;  // different after each processed block

	private Goertzel gless;
	private Goertzel gmore;

	/**
	 * Constructor
	 * 
	 * @param sampleRate
	 *            is the sampling rate of the signal to be analyzed
	 * @param targetFreq
	 *            is the frequency that Goertzel will look for.
	 * @param numSample is the number of samples to process
	 * 
	 */
	public Goertzel(float sampleRate, float targetFreq, float numSamples, float threshold) {
		this(sampleRate, targetFreq, numSamples, threshold, 0);
	}
	

	/* Private constructor to prevent recursion */
	private Goertzel(float sampleRate, float targetFreq, float numSamples, float threshold, int i) {
		this.sampleRate = sampleRate;
		this.setTargetFrequency(targetFreq);
		this.numSamples = numSamples;
		this.threshold = threshold;
		
		initGoertzel();
		resetGoertzel();

		if (i < 1) {
			float diff = 100;
			gless = new Goertzel(sampleRate, targetFreq - diff, numSamples, threshold, ++i);
			gmore = new Goertzel(sampleRate, targetFreq + diff, numSamples, threshold, ++i);
		}
	}
	
	public boolean frequencyDetected(){
		float mag = this.getMagnitude();
		float magless = gless.getMagnitude();
		float magmore = gmore.getMagnitude();
		
		return (mag > threshold && (mag > ((magless + magmore)/2)*2));
	}

	/**
	 * Call this method after every block of N samples has been processed.
	 * 
	 * @return void
	 */
	public void resetGoertzel() {
		q1 = 0;
		q2 = 0;
	}

	/**
	 * Call this once, to precompute the constants.
	 * 
	 * @return void
	 */
	public void initGoertzel() {
		int k;
		double omega;

		k = (int) (0.5 + ((numSamples * getTargetFrequency()) / sampleRate));
		omega = (2.0 * Math.PI * k) / numSamples;
		
		// precomputed values
		sine = (float) Math.sin(omega);
		cosine = (float) Math.cos(omega);
		coeff = (float) (2.0 * cosine);
		scalingFactor = (float) (numSamples / 2.0);
	}

	/**
	 * Call this routine for every block samples.
	 * 
	 * @param sample
	 *            is a short
	 * @return void
	 */
	public void processSamples(short[] sample) {
		resetGoertzel();
		float q0;
		for (int i = 0; i < numSamples; i++) {
			q0 = (float) (coeff * q1 - q2 + sample[i]);
			q2 = q1;
			q1 = q0;
		}
		if(gmore != null)  // limit recursion depth
			gmore.processSamples(sample);
		if(gless != null)
			gless.processSamples(sample);
	}

	/**
	 * 
	 * @return double is the value of the mag.
	 */
	public float getMagnitude() {
		float magnitude, real, imag;
		
		real = (q1 - q2 * cosine) / scalingFactor;
	    imag = (q2 * sine) / scalingFactor;

	    magnitude = (float) Math.sqrt(real*real + imag*imag);
	    return magnitude;
	}


	public float getTargetFrequency() {
		return targetFrequency;
	}


	public void setTargetFrequency(float targetFrequency) {
		this.targetFrequency = targetFrequency;
	}
	
	
}