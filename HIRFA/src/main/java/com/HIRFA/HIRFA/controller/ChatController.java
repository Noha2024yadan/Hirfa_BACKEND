package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ChatMessageDTO;
import com.HIRFA.HIRFA.entity.Message;
import com.HIRFA.HIRFA.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        Message msg = Message.builder()
                .contenu(chatMessage.getContenu())
                .dateEnvoi(LocalDateTime.now())
                .statut("ENVOYE")
                .build();
        messageRepository.save(msg);

        messagingTemplate.convertAndSend("/topic/messages/" + chatMessage.getReceiverId(), chatMessage);
    }

    @MessageMapping("/chat.sendPrivate")
    public void sendPrivate(@Payload ChatMessageDTO chatMessage) {
        Message msg = Message.builder()
                .contenu(chatMessage.getContenu())
                .dateEnvoi(LocalDateTime.now())
                .statut("ENVOYE")
                .build();
        messageRepository.save(msg);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverId(),
                "/queue/messages",
                chatMessage);
    }
}
