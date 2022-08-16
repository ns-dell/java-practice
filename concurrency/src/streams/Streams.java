package streams;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args){
        isParallel();
        doubleItService(false);
        doubleItService(true);
    }

    public static void isParallel(){
        boolean parallel = Stream.of(1,2,3,4,5,6)
                .isParallel();
        System.out.println(parallel);

        parallel = Arrays.asList(1,2,3,4,5,6).stream()
                .isParallel();
        System.out.println(parallel);

        parallel = Stream.of(3, 1, 4, 1, 5, 9)
                .parallel()
                .isParallel();
        System.out.println(parallel);

        parallel = Arrays.asList(3, 1, 4, 1, 5, 9).parallelStream()
                .isParallel();
        System.out.println(parallel);
        System.out.println();
    }

    public static int doubleIt(int n){
        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + "with n=" + n);
        } catch (InterruptedException ignore){
        }
        return n * 2;
    }

    public static void doubleItService(boolean parallel){
//        List<Integer> ints = Arrays.asList(3,1,4,1,5,9);
        int[] ints = {3,1,4,1,5,9};
        // sequential: should take about 600 ms
        // parallel: less based on cpu cores
        Instant before = Instant.now();
        int total = -1;
        if(parallel){
            total = IntStream.of(ints)
                .parallel()
                .map(Streams::doubleIt)
                .sum();
        }
        else{
            total = IntStream.of(ints)
                    .map(Streams::doubleIt)
                    .sum();
        }
        Instant after = Instant.now();
        Duration duration = Duration.between(before, after);
        System.out.println("Total of doubles = " + total);
        System.out.println("Time = " + duration.toMillis() + " ms\n\n");

    }
}
