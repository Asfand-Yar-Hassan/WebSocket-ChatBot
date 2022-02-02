package ca.sheridancollege.hassaasf.model;

import lombok.Data;

@Data
public class ChatMessage {

	
	private MessageType type;
	private String content;
	private String sender;

	public enum MessageType {
		JOIN, 
		CHAT, 
		LEAVE;
	}

}
