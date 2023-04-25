package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;

import java.util.List;

// Mark this class annotated as repository or define in configuration

public interface JpaLinkRepository extends ListCrudRepository<Link, Long> {
    // TODO: writer queries to get concrete objects

    @Query(value = "select link from links link where TYPE(link) = StackoverflowLink")
    List<StackoverflowLink> findGithubLinks();

    void deleteByUrlAndChatId(String url, long chatId);
}