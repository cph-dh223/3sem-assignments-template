package app;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ex3 {

    public static Consumer<String> println = a -> System.out.println(a);
    public static Function<Employee,String> empToName = a -> a.name;
    public static Predicate<Employee> ofAge = a -> a.age > 18;
    public static void main(String[] args) {
        int[] arr = Stream.iterate((int)1, a -> a+1).limit(100).mapToInt(a -> a).toArray();
        IntPredicate divBySeven = a -> a % 7 == 0;
        Arrays.stream(arr).filter(divBySeven).forEach(a -> System.out.print(a + " "));
        System.out.println();
        System.out.println();

        List<String> names = Arrays.asList("John", "Jane", "Jack", "Joe", "Jill");
        Supplier<Employee> employeeSupplier = () -> {
            Random random = new Random();
            int randomIndex = random.nextInt(names.size());
            String randomName = names.get(randomIndex);
            int randomAge = random.nextInt(100);
            return new Employee(randomName, randomAge);
        };

        for (int i = 0; i < 5; i++) {
            Employee emp = employeeSupplier.get();
            println.accept(empToName.apply(emp));
            System.out.println(ofAge.test(emp));
            System.out.println();
        }
    }
}
