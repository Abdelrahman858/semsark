package project.semsark.service.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.semsark.jwt.JwtUtil;
import project.semsark.model.entity.Chat;
import project.semsark.model.entity.User;
import project.semsark.model.request_body.ChatRequest;
import project.semsark.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    JwtUtil jwtUtil;


    public Chat sendMessage(ChatRequest chatRequest) {
        simpMessagingTemplate.convertAndSend("/private/"+chatRequest.getReceiverEmail(),chatRequest.getMessage());
        User user = jwtUtil.getUserDataFromToken();
        Chat chat = Chat.builder()
                .message(chatRequest.getMessage())
                .date(new Date(System.currentTimeMillis()))
                .status(chatRequest.isStatus())
                .receiverEmail(chatRequest.getReceiverEmail())
                .senderEmail(user.getEmail())
                .build();
        return chatRepository.save(chat);
    }

    public List<Chat> getAllChats(String email) {
        User user = jwtUtil.getUserDataFromToken();

        List<Chat> chats = new ArrayList<>();

        for (Chat chat : chatRepository.findAll()) {
            if (chat.getSenderEmail().equals(user.getEmail()) && chat.getReceiverEmail().equals(email) ||
                    chat.getSenderEmail().equals(email) && chat.getReceiverEmail().equals(user.getEmail()))
                chats.add(chat);
        }
        return chats;
    }
}
