package mao;

/**
 * Project name(项目名称)：zookeeper分布式锁模拟12306售票
 * Package(包名): mao
 * Class(类名): TicketTest
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/4/21
 * Time(创建时间)： 16:56
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class TicketTest
{
    public static void main(String[] args)
    {
        Ticket12306 ticket12306 = new Ticket12306();

        Thread thread1 = new Thread(ticket12306, "携程");
        Thread thread2 = new Thread(ticket12306, "飞猪");
        Thread thread3 = new Thread(ticket12306, "去哪儿");

        thread1.start();
        thread2.start();
        thread3.start();

    }
}
