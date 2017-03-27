/**
 * This is the super class for poll and invalidate message.
 * It is different from Message.java as these two types of messages got different
 * parameters and requirements.
 * 
 * Variable: 
 * - peerId: sending the message
 * - originatorId: This is the id of the origin Server for the file.
 * - fName: file Name
 * - versionNumber: version Number of the file present with the peer.
 * 
 * @author dmehta
 * @date   Mar 05 2017
 */

package message;

import java.util.Random;

public class ConsistencyMessage {

	int messageId;
	int originServerId;
	String fileName;
	int versionNumber;
	int messageType;

	public ConsistencyMessage(int mId, int originServerId, String fileName, int versionNumber, int mType) {
		this.messageId = mId;
		this.originServerId = originServerId;
		this.fileName = fileName;
		this.versionNumber = versionNumber;
		this.messageType = mType;
	}

	public static int createMessageId(int peerId) {
		int messageId = 0;
		Random rand = new Random();
		int random = rand.nextInt(100);
		messageId = random * (int) Math.pow(10, 3) + peerId;
		return messageId;
	}

}
