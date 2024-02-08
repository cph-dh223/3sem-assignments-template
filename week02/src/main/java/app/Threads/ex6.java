package app.Threads;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ex6 {
    static String[] urls = new String[]{
        "https://icanhazdadjoke.com/api",
        "https://api.chucknorris.io/jokes/random",
        "https://api.kanye.rest",
        "https://api.whatdoestrumpthink.com/api/v1/quotes/random",
        "https://api.spacexdata.com/v5/launches/latest"
    };

    public static void main(String[] args) {
        List<Future<IDTO>> dotFutures = new ArrayList<>();
        ExecutorService es = Executors.newCachedThreadPool();
        
        dotFutures.add(
            es.submit(() -> {
                return getDadJoke();
            })
        );
        dotFutures.add(
            es.submit(() -> {
                return getChukJoke();
            })
        );
        dotFutures.add(
            es.submit(() -> {
                return getKanyeDTO();
            })
        );
        dotFutures.add(
            es.submit(() -> {
                return getTrumpThinkDTO();
            })
        );
        dotFutures.add(
            es.submit(() -> {
                return getSpaceXDTO();
            })
        );
        dotFutures.stream().parallel().forEach(e->{
            try {
                System.out.println(e.get() + "\n");
            } catch (InterruptedException | ExecutionException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
        System.out.println("main thread done");
        System.out.println();
        es.shutdown();

    }

    private static ChukJokeDTO getChukJoke(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(urls[1])
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), ChukJokeDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static DadJokeDTO getDadJoke(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://icanhazdadjoke.com/")
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), DadJokeDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static KanyeDTO getKanyeDTO(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(urls[2])
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), KanyeDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static TrumpThinkDTO getTrumpThinkDTO(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(urls[3])
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), TrumpThinkDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private static SpaceXDTO getSpaceXDTO(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(urls[4])
            .addHeader("Accept", "application/json")
            .method("GET", null)
            .build();
        try{
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), SpaceXDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Getter
    @Setter
    private static class DadJokeDTO implements IDTO {
        private String id;
        private String joke;
        private int status;
        @Override
        public String toString() {
            return "Dad joke: " + joke;
        }
    }

    @Getter
    @Setter
    @ToString
    private static class ChukJokeDTO implements IDTO {
        String[] categories;
        LocalDate created_at;
        String icon_url;
        String id;
        LocalDate updated_at;
        String url;
        String value;

        @JsonProperty("created_at")
        private void createdAt(String date){
            date = date.split("\\.")[0];
            this.created_at = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        @JsonProperty("updated_at")
        private void updatedAt(String date){
            date = date.split("\\.")[0];
            this.created_at = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Getter
    @Setter
    @ToString
    private static class KanyeDTO implements IDTO {
        String quote;
    }

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class TrumpThinkDTO implements IDTO {
        String message;
        // attributes nlp_attributes;
        
        @Getter
        @Setter
        @ToString
        private static class attributes{
            String [][] quote_structure;
        }
    }
    
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SpaceXDTO implements IDTO {
        String id;
        int flight_number;
        boolean success;
    }

    private static interface IDTO {
        @Override
        String toString();        
    }
}
