package rpc.demo.util.client;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClientChannel  extends NioSocketChannel {

    private String responseMessage;

    private ReentrantLock lock = new ReentrantLock();

    private Condition hasMessage = lock.newCondition();

    public void reset() {
        this.responseMessage = null;
    }

    public String get(long timeout) throws InterruptedException {
        lock.lock();
        try {
            long end = System.currentTimeMillis() + timeout;
            long time = timeout;
            while (responseMessage == null) {
                boolean ok = hasMessage.await(time, TimeUnit.MILLISECONDS);
                if (ok || (time = end - System.currentTimeMillis()) <= 0) {
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
        return responseMessage;
    }

    public void set(String message) {
        lock.lock();
        try {
            responseMessage = message;
            hasMessage.signal();
        } finally {
            lock.unlock();
        }
    }

}
