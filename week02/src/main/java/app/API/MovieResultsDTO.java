package app.API;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResultsDTO{
    boolean adult;
    String backdrop_path;
    int id;
    String title;
    String original_language;
    String original_title;
    String overview;
    String poster_path;
    String media_type;
    int[] genre_ids;
    String homepage;
    float popularity;
    LocalDate release_date;
    boolean video;
    float vote_average;
    int vote_count;

    @JsonProperty("release_date")
    private void releaseDate(String date){
        if(date.length() == 10){
            release_date = LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
    @Override
    public String toString() {
        return title;
    }
}