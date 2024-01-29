import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ex4 {
    public static void main(String[] args) {
        
        List<String> names = Arrays.asList("John", "Jane", "Jack", "Joe", "Jill");
        List<LocalDate> birthDates = Arrays.asList(
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
            LocalDate.of(2005, 11, 1),
            LocalDate.of(2006, 12, 1));
        Supplier<Employee> employeeSupplier = () -> {
            Random random = new Random();
            int randomIndex = random.nextInt(names.size());
            String randomName = names.get(randomIndex);
            int randomAge = random.nextInt(birthDates.size());
            LocalDate randomBirthDay = birthDates.get(randomAge);
            return new Employee(randomName, randomBirthDay);
        };

        List<Employee> employees = Stream.generate(employeeSupplier).limit(100).collect(Collectors.toList());
        
        for (Employee employee : employees) {
            System.out.println(employee.age);
        }
        System.out.println(avgAge(employees).getYears());
        
        filterByMonth(employees, Month.JANUARY).stream().map(e -> ex3.empToName.apply(e)).forEach(System.out::println);
        System.out.println();
        Arrays.stream(groupByMonth(employees)).forEach(a -> {System.out.print(a.size() > 0 ? a.get(0).birthDay.getMonth() + ": " : "");a.stream().map(e -> ex3.empToName.apply(e)).forEach(b -> System.out.print(b + " "));System.out.println();});
        System.out.println();
        System.out.println("Birthdays this month:");
        filterByMonth(employees, LocalDate.now().getMonth()).stream().map(a -> ex3.empToName.apply(a)).forEach(System.out::println);
    }

    public static Period avgAge(List<Employee> employees){
        long totalAge = 0;
        for (Employee employee : employees) {
            totalAge += employee.birthDay.toEpochDay();
        }
        totalAge /= employees.size();
        return Period.between(LocalDate.ofEpochDay(totalAge),LocalDate.now());
    }

    public static List<Employee> filterByMonth(List<Employee> employees, Month month){
        return employees.stream().filter(e -> e.birthDay.getMonth().equals(month)).collect(Collectors.toList());
    }
    public static List<Employee>[] groupByMonth(List<Employee> employees){
        List<Employee>[] groups = new ArrayList[12];
        for (int i = 0; i < 12; i++) {
            groups[i] = filterByMonth(employees, Month.of(i+1));
        }
        return groups;
    }
}
