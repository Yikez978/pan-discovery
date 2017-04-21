package org.alcibiade.pandiscovery.db.service.threads;

import java.util.concurrent.ThreadFactory;

/**
 * Discovery thread pool
 */
public class DiscoveryThreadFactory implements ThreadFactory {

    private int nextThreadId = 1;

    @Override
    public Thread newThread(Runnable runnable) {
        String threadName = String.format("Discovery-%02d", nextThreadId++);
        DiscoveryThread workerThread = new DiscoveryThread(runnable, threadName);
        return workerThread;
    }
}
