package Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static final ThreadManager instance = new ThreadManager();
    private ExecutorService service;
    private ThreadManager(){
        service = Executors.newCachedThreadPool();
    }

    public static void stop(){
        instance.service.shutdown();
    }

    public static void submit(Runnable r){
        instance.service.submit(r);
    }

    public static void execute(Runnable r){
        instance.service.execute(r);
    }
}
