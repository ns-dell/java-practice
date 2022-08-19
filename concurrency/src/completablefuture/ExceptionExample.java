package completablefuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExceptionExample {

    public static void main(String[] args){

//        ExecutorService executor1 = Executors.newSingleThreadExecutor();

        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            throw new IllegalStateException("No data");
//            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers1 = ids -> {
            sleep(150);
            Supplier<List<User>> userSupplier =
                    () -> ids.stream()
                            .map(User::new)
                            .collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers2 = ids -> {
            sleep(5000);
            Supplier<List<User>> userSupplier =
                    () -> ids.stream()
                            .map(User::new)
                            .collect(Collectors.toList());
            return CompletableFuture.supplyAsync(userSupplier);
        };

        Function<List<Long>, CompletableFuture<List<Email>>> fetchEmails = ids -> {
            sleep(350);
            Supplier<List<Email>> userSupplier =
                    () -> {
                        return ids.stream()
                                .map(Email::new)
                                .collect(Collectors.toList());

                    };
            return CompletableFuture.supplyAsync(userSupplier);

        };

        Consumer<List<User>> displayer = users -> {
            users.forEach(System.out::println);
        };

//        CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
//        CompletableFuture<List<User>> userFuture = completableFuture.thenCompose(fetchUsers1);
//        CompletableFuture<List<Email>> emailFuture = completableFuture.thenCompose(fetchEmails);
//
//        CompletableFuture<List<User>> users1 = completableFuture.thenComposeAsync(fetchUsers1);
//        CompletableFuture<List<User>> users2 = completableFuture.thenComposeAsync(fetchUsers2);

        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);


//        users1.thenRun(() -> System.out.println("Users 1"));
//        users2.thenRun(() -> System.out.println("Users 2"));
//        users1.acceptEither(users2, displayer);

        sleep(1000);
        System.out.println("Supply   : done=" + supply.isDone() +
                            " exception=" + supply.isCompletedExceptionally());
        System.out.println("Fetch    : done=" + supply.isDone() +
                            " exception=" + supply.isCompletedExceptionally());
        System.out.println("Display  : done=" + supply.isDone() +
                            " exception=" + supply.isCompletedExceptionally());
//        executor1.shutdown();

    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
