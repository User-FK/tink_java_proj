package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.client.bot.HttpBotClient;
import ru.tinkoff.edu.java.scrapper.client.bot.RabbitBotClient;
import ru.tinkoff.edu.java.scrapper.repository.pojo.PullRequest;
import ru.tinkoff.edu.java.scrapper.response.AnswersResponse;
import ru.tinkoff.edu.java.scrapper.response.GithubRepository;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;

@Slf4j
@Component
public class ClientConfig {
    public static final String PARAM_SITE = "site";
    public static final String SOF = "stackoverflow";

    @Bean
    public GithubClient getGithubClient(@Value("${secrets.github_api_key}") String githubApiKey) {
        final var webClient = WebClient.builder()
                .defaultHeader("authorization", "Bearer " + githubApiKey)
                .baseUrl("https://api.github.com/repos/")
                .build();

        return (user, repositoryName) ->  {
            PullRequest[] pulls = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(user)
                            .pathSegment(repositoryName)
                            .pathSegment("pulls")
                            .queryParam("state", "all")
                            .build())
                    .retrieve()
                    .bodyToMono(PullRequest[].class)
                    .block();

            var githubRepo = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(user)
                            .pathSegment(repositoryName)
                            .build())
                    .retrieve()
                    .bodyToMono(GithubRepository.class)
                    .block();

            githubRepo.setPulls(pulls);
            return githubRepo;
        };

    }

    @Bean
    public StackOverflowClient getStackOverflowclient() {
        final var httpClient = HttpClient.create()
                .baseUrl("https://api.stackexchange.com/2.3/questions")
                .compress(true);

        final var webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        var client = new StackOverflowClient() {
            @Override
            public StackOverflowResponse getQuestions(long... ids) {
                return webClient.get()
                        .uri(uriBuilder -> {
                            for (long id: ids)
                                uriBuilder.pathSegment(String.valueOf(id));

                            uriBuilder.queryParam(PARAM_SITE, SOF);
                            return uriBuilder.build();
                        })
                        .retrieve()
                        .bodyToMono(StackOverflowResponse.class)
                        .block();
            }

            @Override
            public AnswersResponse getAnswers(long questionId) {
                return webClient.get()
                        .uri(uriBuilder -> uriBuilder.pathSegment(String.valueOf(questionId))
                                .pathSegment("answers")
                                .queryParam(PARAM_SITE, SOF)
                                .build()
                        )
                        .retrieve()
                        .bodyToMono(AnswersResponse.class)
                        .block();
            }
        };

        return client;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", value = "bot-client", havingValue = "http")
    public BotClient httpBotClient(@Value("${secrets.bot-base-url}") String baseUrl) {
        // Is there a better way to call a post-construct method?
        log.info("Using http bot client.");

        var client = new HttpBotClient(baseUrl);
        client.init();
        return client;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", value = "bot-client", havingValue = "rabbit")
    public BotClient rabbitmqBotClient(RabbitTemplate rabbit) {
        log.info("Using rabbit bot client.");

        return new RabbitBotClient(rabbit);
    }

    @Bean("linkParser")
    public Parser getLinkParser() {
        var parser = new GithubParser();
        parser.setNext(new StackOverflowParser());
        return parser;
    }
}
