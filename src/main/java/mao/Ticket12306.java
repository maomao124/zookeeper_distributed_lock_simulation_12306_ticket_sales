package mao;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * Project name(项目名称)：zookeeper分布式锁模拟12306售票
 * Package(包名): mao
 * Class(类名): Ticket12306
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/4/21
 * Time(创建时间)： 16:52
 * Version(版本): 1.0
 * Description(描述)：
 * <p>
 * InterProcessSemaphoreMutex：分布式排它锁（非可重入锁）
 * InterProcessMutex：分布式可重入排它锁
 * InterProcessReadWriteLock：分布式读写锁
 * InterProcessMultiLock：将多个锁作为单个实体管理的容器
 * InterProcessSemaphoreV2：共享信号量
 */

public class Ticket12306 implements Runnable
{
    //票数
    private int ticket = 300;

    //分布式可重入排它锁
    private InterProcessMutex lock;

    public Ticket12306()
    {
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        //zookeeper创建链接，第一种
                        /*
                        CuratorFramework client =
                                CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                                        60 * 1000,
                                        15 * 1000,
                                        retryPolicy);
                        client.start();
                        */

        //zookeeper创建链接，第二种
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("127.0.0.1:2181")
                        .sessionTimeoutMs(60 * 1000)
                        .connectionTimeoutMs(15 * 1000)
                        .retryPolicy(retryPolicy)
                        .namespace("test")
                        .build();
        client.start();

        //创建锁
        lock = new InterProcessMutex(client, "/lock");
    }


    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                //获得许可
                lock.acquire(3, TimeUnit.SECONDS);
                if (ticket > 0)
                {
                    System.out.println(Thread.currentThread() + ":" + ticket);
                    ticket--;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
            finally
            {
                try
                {
                    //释放锁
                    lock.release();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
