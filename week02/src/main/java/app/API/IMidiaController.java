package app.API;

public interface IMidiaController {
    public MovieResultsDTO[] getByRatingOrHeighere(int rating);
    public MovieResultsDTO[] getSortedByReleaseDate();
    public MovieResultsDTO[] getMathingTitle(String title);

}
