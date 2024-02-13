package Lombok;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("John", "Doe", 25);
        System.out.println(person); // This should print something like "Person(firstName=John, lastName=Doe, age=25)"

        person.setAge(26);
        System.out.println(person.getAge()); // This should print "26"
    }
}
