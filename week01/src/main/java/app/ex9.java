package app;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ex9 {
    public static void main(String[] args) {
        CompletableFuture<Void> f1 = new CompletableFuture().runAsync(() -> new Task().run());
        CompletableFuture<Void> f2 = new CompletableFuture().runAsync(() -> new Task().run());
        CompletableFuture<Void> f3 = new CompletableFuture().runAsync(() -> new Task().run());
        CompletableFuture<Void> f4 = new CompletableFuture().runAsync(() -> new Task().run());

        CompletableFuture<Void> allFs = CompletableFuture.allOf(f1, f2, f3, f4);
        allFs.thenRun(() -> System.out.println("Compleateble futurs are done"));

        ExecutorService ex = Executors.newFixedThreadPool(4);
        ex.submit(() -> new Task().run());
        ex.submit(() -> new Task().run());
        ex.submit(() -> new Task().run());
        ex.submit(() -> new Task().run());

        ex.shutdown();
        System.out.println("Executor servies done");
    }
}
