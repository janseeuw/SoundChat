package be.ugent.soundchat.model;

import java.util.Date;

public class Message {
	private String text;
	private boolean isMine;
	private Date date;
	private int id;
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public Message(String text, boolean isMine){
		this(text, isMine, new Date());
	}
	
	public Message(String text,boolean isMine,Date date){		
		setText(text);
		setMine(isMine);
		setDate(date);		
	}
	
	
	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public boolean isMine() {
		return isMine;
	}


	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [message_id=");
		builder.append(this.getID());
		builder.append(", text=");
		builder.append(this.getText());
		builder.append(", date=");
		builder.append(this.getDate());
		builder.append(", isMine=");
		builder.append(this.isMine());
		builder.append("]");
		return builder.toString();
	}
	

}