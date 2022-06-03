package com.quasiris.qsf.commons.http;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

import java.util.ArrayList;
import java.util.List;

public class IdleConnectionMonitorThread extends Thread {
    private final List<PoolingHttpClientConnectionManager> managers = new ArrayList<>();
    private int waitTimeMills = 5000;
    private boolean closeIdleConnections = true;
    private int idleConnectionExpirationTimeMills = 30 * 1000;
    private volatile boolean shutdown;

    @Override
    public void run() {
        if (closeIdleConnections) {
            runWithClosingIdleConnections();
        } else {
            runWithoutClosingIdleConnections();
        }
    }


    private void runWithClosingIdleConnections() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(waitTimeMills);
                    for (PoolingHttpClientConnectionManager manager : managers) {
                        manager.closeExpired();
                        manager.closeIdle(Timeout.ofMilliseconds(idleConnectionExpirationTimeMills));
                    }
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }

    private void runWithoutClosingIdleConnections() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(waitTimeMills);
                    for (PoolingHttpClientConnectionManager manager : managers) {
                        manager.closeExpired();
                    }
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

    public int getWaitTimeMills() {
        return waitTimeMills;
    }

    public void setWaitTimeMills(int waitTimeMills) {
        this.waitTimeMills = waitTimeMills;
    }

    public boolean isCloseIdleConnections() {
        return closeIdleConnections;
    }

    public void setCloseIdleConnections(boolean closeIdleConnections) {
        this.closeIdleConnections = closeIdleConnections;
    }

    public int getIdleConnectionExpirationTimeMills() {
        return idleConnectionExpirationTimeMills;
    }

    public void setIdleConnectionExpirationTimeMills(int idleConnectionExpirationTimeMills) {
        this.idleConnectionExpirationTimeMills = idleConnectionExpirationTimeMills;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public List<PoolingHttpClientConnectionManager> getManagers() {
        return managers;
    }
}
