package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

import java.util.List;

@Component
public class JpaChatService implements ChatService {
    @Autowired
    private JpaChatRepository chatRepository;

    @Override
    public void add(long chatId) {
        chatRepository.save(new Chat(chatId));
    }

    @Override
    public void remove(long chatId) {
        chatRepository.deleteById(chatId);
    }

    @Override
    public List<Chat> getAll() {
        return chatRepository.findAll();
    }
}
