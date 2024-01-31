package app;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ex5 {

    public static void main(String[] args) {
        IntStream str = Stream.iterate((int)1, a -> a+1).limit(100).mapToInt(a -> a);
        ex2.map(new int[]{1,2,3}, ex5::doubleValue);
        
    }

    public static int doubleValue(int a){
        return a * 2;
    }
}
