package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ex7 {
    public static void main(String[] args) {
        List<Transaction> transactions = Stream.generate(transactionSupplier()).limit(100).collect(Collectors.toList());
        double sum = transactions.stream().map(a -> a.getAmount()).collect(Collectors.reducing(0.0, ((a, b) -> a + b)));
        System.out.println(sum);
        System.out.println();

        Map<String, Double> sumByCurrentcy = transactions.stream().collect(
            Collectors.groupingBy(a -> a.getCurrency())).entrySet().stream().map(a -> Map.entry(a.getKey(), 
                a.getValue().stream().map(b -> b.getAmount()).reduce((b, c) -> b + c).get()
            )).collect(Collectors.toMap(a -> a.getKey(), a -> a.getValue()));
        System.out.println(sumByCurrentcy);

        System.out.println(transactions.stream().sorted(((a,b) -> (int)(b.getAmount()-a.getAmount()))).findFirst().get());

        System.out.println(transactions.stream().collect(Collectors.averagingDouble(a -> a.getAmount())));
    }

    private static Supplier<Transaction> transactionSupplier (){
        String[] currencyes = new String[]{"ERU", "DKK", "USD"};
        return () -> {
            int id = 0; // Transactions are 1 inxed 
            id++;
            Random random = new Random();
            double amount = random.nextFloat(1000);
            String currency = currencyes[random.nextInt(currencyes.length)];
            return new Transaction(id, amount, currency);
        };
    }
}
