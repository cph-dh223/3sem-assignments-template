package app.Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ex1 {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(4);

        letters(es);
        Counter counter = new Counter();
    }
    private static void letters(ExecutorService es) {
        for (int i = 65; i < 91; i++) {
            char letter = (char)i;
            es.submit(() -> {
                System.out.println(letter + "" + letter + "" + letter);
            });
        }
        es.shutdown();
        
        System.out.println("shotdonw");
    }
    private static class Counter {
        private int count = 0;

        // Method to increment the count, synchronized to ensure thread safety
        public synchronized void increment() {
            count++;
        }

        // Method to retrieve the current count value
        public int getCount() {
            return count;
        }

    }
}
