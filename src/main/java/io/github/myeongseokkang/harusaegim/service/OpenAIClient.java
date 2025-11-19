package io.github.myeongseokkang.harusaegim.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAIClient {

    private final String apiKey;
    private final String model;
    private final String baseUrl;
    private final HttpClient http;
    private final ObjectMapper om = new ObjectMapper();

    public OpenAIClient(
            @Value("${openai.api-key}") String apiKey,
            @Value("${openai.model}") String model,
            @Value("${openai.base-url}") String baseUrl
    ) {
        this.apiKey = apiKey;
        this.model = model;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * system / user 텍스트를 함께 보내는 헬퍼
     */
    public String respond(String systemText, String userText) {
        String merged = "System:\n" + systemText + "\n\nUser:\n" + userText;
        return respond(merged);
    }

    /**
     * OpenAI Responses API 호출
     * - 에러/타임아웃/파싱 실패 시 예외를 던지지 않고 빈 문자열("")을 반환한다.
     *   (상위 서비스에서 fallback 처리 용도)
     */
    public String respond(String inputText) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("input", inputText);

            byte[] json = om.writeValueAsBytes(body);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/responses"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(json))
                    .build();

            HttpResponse<byte[]> res = http.send(req, HttpResponse.BodyHandlers.ofByteArray());
            String raw = new String(res.body(), StandardCharsets.UTF_8);

            if (res.statusCode() / 100 != 2) {
                System.err.println("OpenAI error " + res.statusCode() + ": " + raw);
                return "";
            }

            JsonNode root = om.readTree(raw);

            if (root.has("output_text") && root.get("output_text").isTextual()) {
                return root.get("output_text").asText();
            }

            JsonNode output = root.path("output");
            if (output.isArray()) {
                for (JsonNode item : output) {
                    JsonNode content = item.path("content");
                    if (content.isArray()) {
                        for (JsonNode part : content) {
                            String type = part.path("type").asText("");
                            if ("output_text".equals(type) && part.has("text")) {
                                return part.get("text").asText();
                            }
                            if ("refusal".equals(type) && part.has("reason")) {
                                return "[refusal] " + part.get("reason").asText();
                            }
                        }
                    }
                }
            }

            System.err.println("OpenAI response parsed but no text found >>> " + raw);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
