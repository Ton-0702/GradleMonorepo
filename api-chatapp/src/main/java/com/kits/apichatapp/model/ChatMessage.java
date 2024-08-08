package com.kits.apichatapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // create parameters easy
public class ChatMessage {
    private String content;
    private String sender; // tên ng gửi
    private MessageType type;
}
