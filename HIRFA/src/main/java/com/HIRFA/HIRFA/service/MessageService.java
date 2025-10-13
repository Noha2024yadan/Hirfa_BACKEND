package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.entity.Message;
import com.HIRFA.HIRFA.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message envoyerMessage(Message message) {
        message.setStatut("ENVOYE");
        message.setDateEnvoi(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getConversation(UUID coopId, UUID designerId) {
        List<Message> conv1 = messageRepository
                .findByCooperativeSender_CooperativeIdAndDesignerReceiver_DesignerId(coopId, designerId);
        List<Message> conv2 = messageRepository
                .findByDesignerSender_DesignerIdAndCooperativeReceiver_CooperativeId(designerId, coopId);
        conv1.addAll(conv2);

        conv1.sort(Comparator.comparing(Message::getDateEnvoi));

        return conv1;
    }
}
