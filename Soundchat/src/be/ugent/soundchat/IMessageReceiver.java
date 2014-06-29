package be.ugent.soundchat;

import be.ugent.soundchat.model.Message;

/**
 * @author jonasanseeuw
 *	Callback interface
 */
public interface IMessageReceiver {
	public void onMessageReceived(Message m);
	public void onMessagePlayCompleted();
	public void MessagePlayProgress(int percentageCompleted);
}
