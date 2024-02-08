package app.API;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import lombok.Getter;
import lombok.Setter;

public class ex1 {
    public static void main(String[] args) {
        getMovie("tt6166392");
        IMidiaController movieController = new MovieController();
        System.out.println(Arrays.toString(movieController.getByRatingOrHeighere(2)));
    }

    private static void getMovie(String imdb_id) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/find/{imdb_id}?external_source=imdb_id"
            .replace("{imdb_id}", imdb_id);

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NDMwZmEzYzRkOWU5YzI3MDZiZDg2YTNkY2IxODhjZSIsInN1YiI6IjY1YzBhODc5MTJjNjA0MDE3YzA0MzQ5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hRbItPdxtn4GYN4hCWHkuvQnA8ZAyIVeAoCOUVR48hY")
            .build();

        try {
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
            // objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            
            String json = response.body().string();
            MediaDTO movieDTO = objectMapper.readValue(json, MediaDTO.class);
            System.out.println(movieDTO.movie_results[0].overview);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Getter
    @Setter
    public static class MediaDTO{

        MovieResultsDTO movie_results[];
        MovieResultsDTO tv_results[];
        MovieResultsDTO person_results[];
        MovieResultsDTO tv_episode_results[];
        MovieResultsDTO tv_season_results[];
    }
}
