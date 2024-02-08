package app.API;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class MovieController implements IMidiaController {
    public MovieResultsDTO[] getByRatingOrHeighere(int rating){
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_average.asc&vote_average.gte={rating}&vote_count.gte=200"
            .replace("{rating}", Integer.toString(rating));
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NDMwZmEzYzRkOWU5YzI3MDZiZDg2YTNkY2IxODhjZSIsInN1YiI6IjY1YzBhODc5MTJjNjA0MDE3YzA0MzQ5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hRbItPdxtn4GYN4hCWHkuvQnA8ZAyIVeAoCOUVR48hY")
            .build();

        try {
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();

            ResultDTO result = objectMapper.readValue(response.body().string(), ResultDTO.class);
            return result.getResults();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public MovieResultsDTO[] getSortedByReleaseDate(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=primary_release_date.desc")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NDMwZmEzYzRkOWU5YzI3MDZiZDg2YTNkY2IxODhjZSIsInN1YiI6IjY1YzBhODc5MTJjNjA0MDE3YzA0MzQ5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hRbItPdxtn4GYN4hCWHkuvQnA8ZAyIVeAoCOUVR48hY")
            .build();

        try {
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();

            ResultDTO result = objectMapper.readValue(response.body().string(), ResultDTO.class);
            return result.getResults();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public MovieResultsDTO[] getMathingTitle(String title){
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/search/movie?query={title}&include_adult=false&language=en-US&page=1"
            .replace("{title}", title);
        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NDMwZmEzYzRkOWU5YzI3MDZiZDg2YTNkY2IxODhjZSIsInN1YiI6IjY1YzBhODc5MTJjNjA0MDE3YzA0MzQ5MyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hRbItPdxtn4GYN4hCWHkuvQnA8ZAyIVeAoCOUVR48hY")
            .build();

        try {
            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            // String tmp = response.body().string();
            // System.out.println(tmp);
            
            // objectMapper.enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);
            ResultDTO result = objectMapper.readValue(response.body().string().trim(), ResultDTO.class);
            return result.getResults();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Getter
    @Setter
    @ToString
    public static class ResultDTO {
        int page;
        MovieResultsDTO[] results;
        int total_pages;
        int total_results;
        
    }
}
