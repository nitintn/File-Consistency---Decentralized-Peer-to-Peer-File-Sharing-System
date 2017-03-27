package message;

import java.util.Random;

public class Message {

	private int messageId;
	private String fileName;
	private int ttl;
	private int messageType;
	
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public Message(int mId, int ttl, String fName, int mType){
		
		this.messageType = mType;
		this.messageId = mId;
		this.ttl = ttl;
		this.fileName = fName;
		
	}
	
	public static int createMessageId(int peerId){
		int messageId = 0;
		Random rand = new Random();
		int random = rand.nextInt(100);
		messageId = random*(int)Math.pow(10, 3) + peerId;
		return messageId;
	}
	
	
	
}
