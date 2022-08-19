package completablefuture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CompletableFuturePractice {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Map<Integer, Product> cache = new ConcurrentHashMap<>();
    private Product getLocal(int id){
        return cache.get(id);
    }
    private Product getRemote(int id){
        try {
            Thread.sleep(100);
            if (id == 666) {
                throw new RuntimeException("Bad Request Id");
            }
        } catch (InterruptedException ignored){}
        return new Product(id, "product"+id, (int) Math.random()*100);
    }

    public CompletableFuture<Product> getProduct(int id){
        try {
            Product product = getLocal(id);
            if (product != null){
                logger.info("getLocal with id = " + id);
                return CompletableFuture.completedFuture(product);
            }
            else {
                logger.info("getRemote with id = " + id);
                return CompletableFuture.supplyAsync(() -> {
                    Product p = getRemote(id);
                    cache.put(id, p);
                    return p;
                });
            }
        }
        catch (Exception e) {
            logger.info("exception thrown for id = " + id);
            CompletableFuture<Product> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    public static void main(String[] args){
        try {
            CompletableFuturePractice service = new CompletableFuturePractice();
            Product product = service.getProduct(1).get();
            product = service.getProduct(1).get();
            product = service.getProduct(666).get();
        } catch (Exception e){
            System.out.println(e);
        }
    }
}

