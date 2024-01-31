package app;
import java.time.LocalDate;

public class Book {

    String title;
    String author;
    LocalDate publicationYear;
    int pages; 
    int rating;
    
    public Book(String title, String author, LocalDate publicationYear, int pages, int rating) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.pages = pages;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + ", publicationYear=" + publicationYear + ", pages="
                + pages + ", rating=" + rating + "]";
    }

    
}