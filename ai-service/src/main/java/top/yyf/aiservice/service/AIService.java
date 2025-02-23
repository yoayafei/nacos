package top.yyf.aiservice.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AIService {

    @Value("sk-ea85d886e71b4f71b11262f0be52968c")
    private String apiKey;

//    @Value("${tongyi.api.secret}")
//    private String apiSecret;

    private final OkHttpClient client = new OkHttpClient();

    public String ask(String question) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"question\": \"" + question + "\"}");
        Request request = new Request.Builder()
                .url("https://api.tongyi.com/v1/models/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
