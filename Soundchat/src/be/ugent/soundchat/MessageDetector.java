package be.ugent.soundchat;

import java.util.ArrayList;

import be.ugent.soundchat.model.Message;

public class MessageDetector {
	/*
	 * Class that get's the binary values corresponding to the detected
	 * frequency by the recorder. This class is responsible for detecting the
	 * begin and end-barker, extract the message and push it to the GUI.
	 */

	private int numFalseInARow = 0;
	private int numTrueInARow = 0;

	private boolean beginBarkerDetected = false;
	private boolean endBarkerDetected = false;

	private boolean previous = true;

	private ArrayList<Boolean> bits;

	private IMessageReceiver receiver;

	public MessageDetector(IMessageReceiver receiver) {
		bits = new ArrayList<Boolean>();
		this.receiver = receiver;
	}

	public void addBits(boolean[] detectedBits) {

		if (!beginBarkerDetected) {
			// wait for begin barker = 8 x false in a row
			for (boolean b : detectedBits) {
				if (b == false)
					numFalseInARow++;
				else
					numFalseInARow = 0;
			}

			if (numFalseInARow == Configuration.beginBarker.length) {
				beginBarkerDetected = true;
				System.out.println("BEGIN BARKER DETECTED");
			}
		} else { // begin barker detected

			for (boolean b : detectedBits) {
				bits.add(b);
				if (b == true)
					numTrueInARow++;
				else
					numTrueInARow = 0;

				// System.out.printf("Teller true:%d\n",numTrueInARow);
			}

			if (numTrueInARow >= Configuration.endBarker.length) {
				endBarkerDetected = true;
				System.out.println("END BARKER DETECTED");
				// copy message from begin and remove end-barker and give this
				// to the UI
				boolean[] messageBits = new boolean[bits.size()
						- Configuration.endBarker.length];
				for (int i = 0; i < bits.size()
						- Configuration.endBarker.length; i++) {
					messageBits[i] = bits.get(i).booleanValue();
				}

				String message = Helpers.Binary2Data(messageBits,
						Configuration.charsetSize);

				MainActivity main = (MainActivity) receiver;
				final Message m = new Message(message, false);
				main.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Message parameter = m;
						receiver.onMessageReceived(parameter);
					}
				});

				beginBarkerDetected = false;
				bits.clear();
			}

		}

	}

}
