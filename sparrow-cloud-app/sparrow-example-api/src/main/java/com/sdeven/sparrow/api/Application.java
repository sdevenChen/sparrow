package com.sdeven.sparrow.api;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.net.InetAddress;


@Slf4j
@SpringBootApplication
public class Application implements ApplicationListener<WebServerInitializedEvent> {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "Local: \t\thttp://{}:{}\n\t" +
                        "----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                event.getWebServer().getPort());
    }
}
