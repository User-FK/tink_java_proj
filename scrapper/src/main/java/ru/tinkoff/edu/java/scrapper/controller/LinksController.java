package ru.tinkoff.edu.java.scrapper.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@Slf4j
@RestController
@RequestMapping(value = "/",
    produces = "application/json",
    consumes = "application/json")
public class LinksController {
    @Autowired
    private LinkService linkService;

    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(value = "/links")
    LinkResponse addLink(@RequestHeader long tgChatId,
                         @RequestBody AddLinkRequest request) {
        log.info("POST request to /links");
        log.info("Link to add: " + request);
        log.info("Url: " + request.url());

        linkService.add(request.url().toString(), tgChatId);

        return new LinkResponse(tgChatId, request.url());
    }

    @GetMapping(value = "/links")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    List<LinkResponse> getLink(@RequestHeader long tgChatId) {
        log.info("GET request to /links");

        return linkService.linksForChat(tgChatId)
                .stream()
                .map(linkRecord -> new LinkResponse(linkRecord.getId(), linkRecord.toURL()))
                .collect(Collectors.toList());
    }

    @Transactional
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping(value = "/links")
    LinkResponse deleteLink(@RequestHeader long tgChatId,
                            @RequestBody RemoveLinkRequest request) {
        log.info("DELETE request for /links");

        try {
            linkService.delete(request.url().toString(), tgChatId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e);
        }

        return new LinkResponse(tgChatId, request.url());
    }
}
