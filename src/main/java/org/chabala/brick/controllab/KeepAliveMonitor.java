/*
 * Copyright © 2016 Greg Chabala
 *
 * This file is part of brick-control-lab.
 *
 * brick-control-lab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * brick-control-lab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with brick-control-lab.  If not, see http://www.gnu.org/licenses/.
 */
package org.chabala.brick.controllab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * Handles sending keep alive messages in the absence of other commands.
 */
class KeepAliveMonitor implements Closeable {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPort serialPort;
    private final ScheduledExecutorService executor;
    private final long keepAlivePeriodMs;
    private static final byte KEEP_ALIVE_COMMAND = 0x02;
    /** Slightly less than two seconds. */
    private static final int KEEP_ALIVE_PERIOD_MS = 1800;
    private ScheduledFuture<?> task;

    /**
     * Creates a keep alive monitor for the specified serial port. As soon
     * as it's constructed, will send a keep alive message to the serial
     * port with a frequency just under two seconds, as long as the port
     * is open.
     * @param serialPort the port to receive keep alive messages
     */
    KeepAliveMonitor(SerialPort serialPort) {
        this(serialPort, Duration.ofMillis(KEEP_ALIVE_PERIOD_MS));
    }

    KeepAliveMonitor(SerialPort serialPort, Duration keepAlivePeriod) {
        this.serialPort = serialPort;
        this.keepAlivePeriodMs = keepAlivePeriod.toMillis();
        executor = Executors.newSingleThreadScheduledExecutor(
                new NamedDaemonThreadFactory(
                        "KeepAlive " + serialPort.getPortName()));
        scheduleTask();
    }

    /**
     * Call this method when a command is being sent, to reset the countdown
     * on the keep alive monitor. It will reschedule the next keep alive
     * message from this moment.
     */
    void reset() {
        task.cancel(false);
        scheduleTask();
    }

    private void scheduleTask() {
        task = executor.scheduleAtFixedRate(() -> {
            if (serialPort.isOpen()) {
                sendCommand(KEEP_ALIVE_COMMAND);
            }
        }, keepAlivePeriodMs, keepAlivePeriodMs, TimeUnit.MILLISECONDS);
    }

    private void sendCommand(byte b) {
        try {
            log.info("TX -> {}", String.format("0x%02X", b));
            serialPort.write(b);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Cancel the next keep alive message and shutdown the monitor.
     */
    @Override
    public void close() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    /**
     * A {@link ThreadFactory} which produces named, daemon threads.
     */
    private static class NamedDaemonThreadFactory implements ThreadFactory {
        private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
        private final String threadName;

        NamedDaemonThreadFactory(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = defaultThreadFactory.newThread(r);
            thread.setName(threadName);
            thread.setDaemon(true);
            return thread;
        }
    }
}
