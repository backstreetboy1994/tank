package callable;

import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    public static void main(String[] args) throws Exception, InterruptedException {
        FutureTask<Long> ft = new FutureTask(new MyTask());
        new Thread(ft).start();
        long l = ft.get();
        System.out.println(l);
    }
}
