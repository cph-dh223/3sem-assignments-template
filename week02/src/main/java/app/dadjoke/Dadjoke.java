package app.dadjoke;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Dadjoke {
    public static void main(String[] args) {
        System.out.println(getDadJoke());
    }
    private static String getDadJoke(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://icanhazdadjoke.com/")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), DadJokeDTO.class).joke;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Getter
    @Setter
    @ToString
    private static class DadJokeDTO{
        private String id;
        private String joke;
        private int status;
    }
}
