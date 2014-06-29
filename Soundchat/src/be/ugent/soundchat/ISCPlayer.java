package be.ugent.soundchat;

/**
 * @author jonasanseeuw
 * Interface for playing sounds
 */
public interface ISCPlayer {
	/**
	 * Starts playing (modulating) the message
	 */
	public void playSound(String message);
}
