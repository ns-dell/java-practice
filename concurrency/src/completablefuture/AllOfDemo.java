package completablefuture;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllOfDemo {
    private int getNextValue(){
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return (int) (Math.random() * 100);
    }

    public CompletableFuture<Integer> getValue() {
        return CompletableFuture.supplyAsync(this::getNextValue);
    }

    public static void main(String[] args){
        AllOfDemo demo = new AllOfDemo();
        CompletableFuture[] completableFutures = Stream.generate(demo::getValue)
                .limit(10)
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(completableFutures).join();

        int sum = Arrays.stream(completableFutures)
                .map(CompletableFuture::join)
                .peek(e -> System.out.print(e + " "))
                .mapToInt(e -> (int) e)
                .reduce(0, Integer::sum);
        System.out.println("\nSum = " + sum);

    }
}
