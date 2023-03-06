package project.semsark.controller.user_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.semsark.model.entity.Chat;
import project.semsark.model.request_body.ChatRequest;
import project.semsark.service.user_service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @PostMapping("/sendMessage/{email}")
    void sendMessage(@PathVariable String email , @RequestBody ChatRequest chatRequest){
        chatService.sendMessage(chatRequest);
    }
    @GetMapping("/list/{email}")
    List<Chat> getMessages(@PathVariable String email){
        return chatService.getAllChats(email);
    }
}
