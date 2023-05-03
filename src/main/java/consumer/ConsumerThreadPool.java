package consumer;

import java.util.ArrayList;

public class ConsumerThreadPool {
    ArrayList<Thread> pool;

    private int threadCount = 0;

    public ConsumerThreadPool(int threads){
        this.threadCount = threads;
        pool = new ArrayList<>(threads);
    }

    private void init(){
        for(int i=0; i < threadCount ; i++){
            LoopitisConsumer taskGrabber = new LoopitisConsumer();
            Thread t = new Thread(LoopitisConsumer::new);
            t.start();
            pool.add(t);

        }
    }

}
