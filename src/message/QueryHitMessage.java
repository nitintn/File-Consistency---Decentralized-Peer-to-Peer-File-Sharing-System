package message;

public class QueryHitMessage extends Message {

	public QueryHitMessage(int peerId, int ttl, String fName) {
		
		super(Message.createMessageId(peerId), ttl, fName, MessageConst.Query_Hit_Message_Type);
		
	}

}
