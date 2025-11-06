package io.github.myeongseokkang.harusaegim;

import io.github.myeongseokkang.harusaegim.service.OpenAIClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class HarusaegimApplication {
    public static void main(String[] args) {
        SpringApplication.run(HarusaegimApplication.class, args);
    }

    @Bean
    CommandLineRunner testGpt(OpenAIClient openAIClient) {
        return args -> {
            String text = openAIClient.respond(
                    "일기 스타일로 5문장 내외 한국어로 써줘.",
                    "오늘의 키워드: 커피, 비, 친구와 통화, 야근 2시간"
            );
            System.out.println("GPT 응답 >>> " + text);
        };
    }
}
