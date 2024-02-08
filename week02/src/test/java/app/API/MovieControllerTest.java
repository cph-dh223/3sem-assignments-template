package app.API;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MovieControllerTest {

    MovieController movieController;

    @BeforeEach
    public void setup(){
        movieController = new MovieController();
    }
    @AfterEach
    public void tearDown(){
        movieController = null;
    }

    @ParameterizedTest
	@ValueSource(strings = {
        "The Shawshank Redemption",
        "The Godfather",
        "The Dark Knight",
        "The Godfather Part II",
        "The Lord of the Rings: The Return of the King",
        "Pulp Fiction",
        "12 Angry Men",
        "The Good, the Bad and the Ugly",
        "Forrest Gump",
        "Fight Club",
        "Inception"
    })
    public void getByTitleTest(String title){
        //arrange
        String expectedTitle = title;
        
        //act
        MovieResultsDTO[] movieResults = movieController.getMathingTitle(title);

        //assert
        assertEquals(expectedTitle, movieResults[0].getTitle());
    }
}
