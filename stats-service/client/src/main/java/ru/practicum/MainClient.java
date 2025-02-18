package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainClient extends BaseClient {
    public MainClient(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public void addHit(CreateHitDto hitDto) {
        post("/hit", hitDto);
    }

    public List<StatDto> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters;
        String path;

        if (uris != null) {
            path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique
            );
        } else {
            path = "/stats?start={start}&end={end}&unique={unique}";
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique
            );
        }

        ResponseEntity<Object> response = get(path, parameters);

        List<Map<String, Object>> body = (List<Map<String, Object>>) response.getBody();
        List<StatDto> statsList = new ArrayList<>();

        if (body != null && !body.isEmpty()) {
            for (Map<String, Object> s : body) {
                StatDto stat = StatDto.builder()
                        .uri(s.get("uri").toString())
                        .hits(((Number) s.get("hits")).intValue())
                        .app(s.get("app").toString())
                        .build();
                statsList.add(stat);
            }
        }

        return statsList;
    }
}