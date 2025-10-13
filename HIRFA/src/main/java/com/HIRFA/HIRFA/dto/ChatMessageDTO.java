package com.HIRFA.HIRFA.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String senderId;
    private String receiverId;
    private String contenu;
}
