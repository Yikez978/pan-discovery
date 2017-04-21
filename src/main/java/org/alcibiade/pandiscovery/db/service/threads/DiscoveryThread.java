package org.alcibiade.pandiscovery.db.service.threads;

/**
 * Custom discovery threads.
 */
public class DiscoveryThread extends Thread {
    public DiscoveryThread(Runnable runnable, String s) {
        super(runnable, s);
    }
}
