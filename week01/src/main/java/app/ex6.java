package app;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ex6 {

    private static Comparator<Book> bookRatingSorter = (a, b) -> b.rating - a.rating;

    public static void main(String[] args) {
        List<Book> books = Stream.generate(bookSupplier()).limit(100).collect(Collectors.toList());
        double avgRating = books.stream().map(a -> (double)a.rating).reduce((a, b) -> a + b).get() / books.size();
        System.out.println("avg rating: " + avgRating);
        List<Book> booksAfter2000 = books.stream().filter(a -> a.publicationYear.isAfter(LocalDate.of(2000, 1, 1))).collect(Collectors.toList());
        System.out.println();
        booksAfter2000.forEach(System.out::println);
        System.out.println();
        books.stream().sorted(bookRatingSorter).forEach(System.out::println);
        System.out.println();
        System.out.println(books.stream().max(bookRatingSorter).get());

        getAuthorAvgRating(books).forEach((a,b) -> System.out.println("Author: " + a + " Avg rating: " + b));
        System.out.println();
        System.out.println(books.stream().map(a -> a.pages).reduce(0, ((a,b) -> a+b)));
    }
    
    public static Map<String,Double> getAuthorAvgRating(List<Book> books){
        Map<String, Double> authorAvgRating = new HashMap<>();
        Set<String> authors = books.stream().map(a -> a.author).collect(Collectors.toSet());
        for (String author : authors) {
           Stream<Book> booksByAuthor =  books.stream().filter(a -> a.author == author);
           long numBooks = booksByAuthor.count();
           booksByAuthor =  books.stream().filter(a -> a.author == author);
           double avgRating = booksByAuthor.mapToDouble(a -> a.rating).reduce(0.0,((a, b) -> a + b))/numBooks;
           authorAvgRating.put(author, avgRating);
        }
        return authorAvgRating;
    }
    public static Supplier<Book> bookSupplier(){
        List<String> titles = Arrays.asList(
           "A",
           "B",
           "C",
           "D",
           "E" 
        );
        List<String> authors = Arrays.asList(
            "Alice",
            "Bob",
            "Charly",
            "David"
        );
        List<LocalDate> publicationDates = Arrays.asList(
            LocalDate.of(1999, 1, 1),
            LocalDate.of(1998, 2, 1),
            LocalDate.of(1997, 3, 1),
            LocalDate.of(1996, 4, 1),
            LocalDate.of(2020, 5, 1),
            LocalDate.of(2000, 6, 1),
            LocalDate.of(2001, 7, 1),
            LocalDate.of(2002, 8, 1),
            LocalDate.of(2003, 9, 1),
            LocalDate.of(2004, 10, 1),
            LocalDate.of(2005, 11, 1)
        );
        int[] numPages = new int[]{100, 150, 200, 250};
        int[] ratings = new int[]{1,2,3,4,5};
        return () -> {
            Random random = new Random();
            String title = titles.get(random.nextInt(titles.size()));
            String author = authors.get(random.nextInt(authors.size()));
            LocalDate publicationYear = publicationDates.get(random.nextInt(publicationDates.size()));
            int pages = numPages[random.nextInt(numPages.length)];
            int rating = ratings[random.nextInt(ratings.length)];
            return new Book(title, author, publicationYear, pages, rating);
        };
    }

}
