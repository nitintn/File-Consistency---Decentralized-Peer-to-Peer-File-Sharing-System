package message;

public class QueryMessage extends Message {

	public QueryMessage(int peerId, int ttl, String fName) {

		super(Message.createMessageId(peerId),ttl,fName, MessageConst.Query_Message_Type);
	}

}
