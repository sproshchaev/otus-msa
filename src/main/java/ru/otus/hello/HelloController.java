package ru.otus.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${hello.greeting}")
    private String greeting;

    @Value("${hello.data-dir}")
    private String dataDir;

    /**
     * Точка входа для проверки, что сервис поднялся.
     */
    @GetMapping("/")
    public String root() {
        log.info("GET / — запрос к корневому эндпоинту");
        return "hello-service is running";
    }

    /**
     * Приветствие. Текст приветствия задаётся переменной окружения GREETING (инструкция ENV).
     */
    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam(defaultValue = "World") String name) {
        log.info("GET /hello — name={}", name);
        Map<String, String> body = new LinkedHashMap<>();
        body.put("message", greeting + ", " + name + "!");
        body.put("host", hostname());
        return body;
    }

    /**
     * Информация об окружении: то, что видит процесс внутри контейнера.
     * Демонстрирует namespaces (hostname, pid) и cgroups (память, ядра).
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        log.info("GET /info — запрос информации об окружении");
        Runtime runtime = Runtime.getRuntime();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("hostname", hostname());
        body.put("pid", ProcessHandle.current().pid());
        body.put("availableProcessors", runtime.availableProcessors());
        body.put("maxMemoryMb", runtime.maxMemory() / 1024 / 1024);
        body.put("totalMemoryMb", runtime.totalMemory() / 1024 / 1024);
        body.put("javaVersion", System.getProperty("java.version"));
        body.put("greeting", greeting);
        body.put("dataDir", dataDir);
        return body;
    }

    /**
     * Запись строки в файл. Демонстрирует эфемерность слоя контейнера и работу volumes.
     */
    @PostMapping("/data")
    public Map<String, Object> write(@RequestParam String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        try {
            Path file = dataFile();
            Files.createDirectories(file.getParent());
            String line = LocalDateTime.now().format(TS) + " [" + hostname() + "] " + message
                    + System.lineSeparator();
            Files.writeString(file, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            log.info("POST /data — записано: {}", message);
            body.put("status", "saved");
            body.put("file", file.toString());
        } catch (IOException e) {
            log.error("POST /data — ошибка записи", e);
            body.put("status", "error");
            body.put("reason", e.getMessage());
        }
        return body;
    }

    /**
     * Чтение всех ранее записанных строк.
     */
    @GetMapping("/data")
    public Map<String, Object> read() {
        Map<String, Object> body = new LinkedHashMap<>();
        Path file = dataFile();
        body.put("file", file.toString());
        try {
            if (Files.notExists(file)) {
                body.put("lines", List.of());
                body.put("count", 0);
                return body;
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            body.put("lines", lines);
            body.put("count", lines.size());
        } catch (IOException e) {
            log.error("GET /data — ошибка чтения", e);
            body.put("status", "error");
            body.put("reason", e.getMessage());
        }
        return body;
    }

    private Path dataFile() {
        return Path.of(dataDir, "messages.log");
    }

    private String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

}
