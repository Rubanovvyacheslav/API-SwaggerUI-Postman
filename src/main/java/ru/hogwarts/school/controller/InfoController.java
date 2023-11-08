package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String getPort() {
        return port;
    }

    @GetMapping("/calculate")
    public void calculate() {
        Long start = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        Long finish = System.currentTimeMillis();
        logger.info("Result: {}, time: {}", sum, finish - start);
    }

    @GetMapping("/calculate-2")
    public void calculate2() {
        Long start = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
        Long finish = System.currentTimeMillis();
        logger.info("Result: {}, time: {}", sum, finish - start);
    }
}
