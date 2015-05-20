package ru.ifmo.ctddev.Nemchenko.UIFileCopy;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class stores information about copy state
 * and contain some static methods for converting various quantities to human readable format.
 */
public class CopyProperties {
    private long copiedBytes;
    private long lastCopiedBytes;
    private long totalBytes;
    private long startTime;
    private long lastTime;
    private boolean evaluatingTotalSize;
    private boolean canceled;
    private boolean finishedCopying;
    private Exception error;

    private static final long KILOBYTE = 1024;
    private static final long MEGABYTE = 1024 * KILOBYTE;
    private static final long GIGABYTE = 1024 * MEGABYTE;

    private static final long SECONDS = 1000;
    private static final long MINUTE = SECONDS * 60;
    private static final long HOUR = MINUTE * 60;

    /**
     * Returns human readable representation of {@code bytes}.
     *
     * @param bytes target bytes
     * @return human readable representation of {@code bytes}
     */
    public static String byteToString(long bytes) {
        DecimalFormat outFormat = new DecimalFormat("#0.00");
        double normalizedBytes;
        if (bytes >= GIGABYTE) {
            normalizedBytes = normalizeBytes(bytes, GIGABYTE);
            return outFormat.format(normalizedBytes) + " Gb";
        } else if (bytes >= MEGABYTE) {
            normalizedBytes = normalizeBytes(bytes, MEGABYTE);
            return outFormat.format(normalizedBytes) + " Mb";
        } else if (bytes >= KILOBYTE) {
            normalizedBytes = normalizeBytes(bytes, KILOBYTE);
            return outFormat.format(normalizedBytes) + " Kb";
        }
        return outFormat.format(bytes) + " b";
    }

    /**
     * Returns human readable representation of {@code time}
     *
     * @param time target time in milliseconds
     * @return string representation of time
     */
    public static String getTimeFormat(long time) {
        Date date = new Date(time);
        if (time >= HOUR) {
            return new SimpleDateFormat("HH 'h' mm 'm' ss 's'").format(date);
        } else if (time >= MINUTE) {
            return new SimpleDateFormat("mm 'm' ss 's'").format(date);
        }
        return new SimpleDateFormat("ss 's'").format(date);
    }

    /**
     * Converts bytes to megabytes
     *
     * @param bytes target bytes
     * @return megabytes = bytes / 2 ^ 20
     */
    public static double bytesToMega(long bytes) {
        return normalizeBytes(bytes, MEGABYTE);
    }

    private static double normalizeBytes(long bytes, long constant) {
        return bytes * 1.0 / constant;
    }

    /**
     * Converts milliseconds to seconds
     *
     * @param milli target milli seconds
     * @return seconds = milli / 1000
     */
    public static double milliToSeconds(long milli) {
        return milli / 1e3;
    }

    public boolean isEvaluatingTotalSize() {
        return evaluatingTotalSize;
    }

    public void setEvaluatingTotalSize(boolean evaluatingTotalSize) {
        this.evaluatingTotalSize = evaluatingTotalSize;
    }

    public long getCopiedBytes() {
        return copiedBytes;
    }

    public void setCopiedBytes(long copiedBytes) {
        this.copiedBytes = copiedBytes;
    }

    public long getLastCopiedBytes() {
        return lastCopiedBytes;
    }

    public void setLastCopiedBytes(long lastCopiedBytes) {
        this.lastCopiedBytes = lastCopiedBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isFinishedCopying() {
        return finishedCopying;
    }

    public void setFinishedCopying(boolean finishedCopying) {
        this.finishedCopying = finishedCopying;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }
}
