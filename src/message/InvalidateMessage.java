/**
 * This class is for the Invalidate Message. This is sent out by the originating
 * server when a master copy is modified.
 * Variables: 
 * - peerId: sending the message
 * - originatorId: This is the id of the origin Server for the file.
 * - fName: file Name
 * - versionNumber: version Number of the file present with the peer.
 * 
 * @author dmehta
 * @date   Mar 05 2017
 * 
 */

package message;

public class InvalidateMessage extends ConsistencyMessage {

	public InvalidateMessage(int peerId, int originatorId, String fName, int versionNumber) {
		super(ConsistencyMessage.createMessageId(peerId), originatorId, fName, versionNumber,
				MessageConst.Push_Message_Type);
	}

}
