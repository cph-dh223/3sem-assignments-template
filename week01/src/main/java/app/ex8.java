package app;

public class ex8 {
    public static void main(String[] args) {
        DataStorage<String> memoryStorage = new MemoryStorage<>();
        memoryStorage.store("Hello, world!");
        String retrievedString = memoryStorage.retrieve(null);
        System.out.println(retrievedString);

        DataStorage<Employee> fileStorage = new FileStorage<>();
        String filename = fileStorage.store(new Employee("John", 30));
        Employee retrievedInt = fileStorage.retrieve(filename);
        System.out.println(retrievedInt);

        // Create and demonstrate DatabaseStorage
    }
}
