package ru.tinkoff.edu.java.scrapper.client;

import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.response.AnswersResponse;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;

public interface StackOverflowClient {
    StackOverflowResponse getQuestions(long... ids);
    AnswersResponse getAnswers(long questionId);
}
