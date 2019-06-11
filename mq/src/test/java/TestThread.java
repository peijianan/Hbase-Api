import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class TestThread {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        for (int i = 1; i < 1000; i++){
            executorService.execute(new Thread(() -> System.out.println("hello")));
            System.out.println(executorService.getPoolSize());
        }
        Thread.sleep(61000);
        System.out.println(executorService.getPoolSize());
    }

}
