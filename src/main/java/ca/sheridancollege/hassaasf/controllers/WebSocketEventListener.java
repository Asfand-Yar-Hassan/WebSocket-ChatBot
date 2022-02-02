package ca.sheridancollege.hassaasf.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import ca.sheridancollege.hassaasf.model.ChatMessage;
import ch.qos.logback.classic.Logger;

@Controller
public class WebSocketEventListener {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent even) {
		logger.info("Received a new web socket connection");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (username != null) {
			logger.info("User Disconnected: {0}", username);
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(ChatMessage.MessageType.LEAVE);
			chatMessage.setSender(username);
			
			messagingTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}
}
