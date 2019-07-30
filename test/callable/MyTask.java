package callable;

import java.util.concurrent.Callable;

//MyTask实现了Callable接口，非常类似于实现了Runnable接口，
//也就是说，这个任务是可以扔到线程中去运行的
//实现Runnable接口需要重写run方法
//方法是带返回值的，返回值是通过泛型来指定的
public class MyTask implements Callable<Long>{
    @Override
    public Long call() throws Exception {
        long r = 0L;
        for (long i=0L; i<10L; i++) {
            r += i;
            Thread.sleep(500);
            System.out.println(i + " added!");
        }
        return r;
    }
}
