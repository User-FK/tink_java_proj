package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;

// TODO: add url correctness check

@Service
public class JdbcLinkService implements LinkService {
    @Autowired
    private JdbcScrapperRepository repository;

    public void add(String url, int chatId) {
        repository.addLink(url, chatId);
    }

    public void delete(String url, int chatId) {
        repository.deleteLink(url, chatId);
    }

    public List<LinkRecord> linksForChat(int chatId) {
        return repository.getLinksForChat(chatId);
    }

    public List<LinkRecord> getAll() {
        return repository.getAllLinks();
    }
}
