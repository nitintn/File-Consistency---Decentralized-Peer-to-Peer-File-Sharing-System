/**
 * This class is for the Poll Message
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

public class PollMessage extends ConsistencyMessage {

	public PollMessage(int peerId, int originatorId, String fName, int versionNumber) {
		super(ConsistencyMessage.createMessageId(peerId), originatorId, fName, versionNumber,
				MessageConst.Push_Message_Type);
	}

}
