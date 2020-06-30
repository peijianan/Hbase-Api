package com.tensor.nacos.api.pojo;

/**
 * @author liaochuntao
 */
public class SystemMonitor {

    private long inactive;
    private long cached;
    private long buffers;
    private long slab;
    private long shared;
    private long available;
    private long active;
    private long used;
    private long free;
    private double percent;
    private long total;

    public long getInactive() {
        return inactive;
    }

    public void setInactive(long inactive) {
        this.inactive = inactive;
    }

    public long getCached() {
        return cached;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public long getBuffers() {
        return buffers;
    }

    public void setBuffers(long buffers) {
        this.buffers = buffers;
    }

    public long getSlab() {
        return slab;
    }

    public void setSlab(long slab) {
        this.slab = slab;
    }

    public long getShared() {
        return shared;
    }

    public void setShared(long shared) {
        this.shared = shared;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public long getActive() {
        return active;
    }

    public void setActive(long active) {
        this.active = active;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SystemMonitor{" +
                "inactive=" + mb(inactive) +
                ", cached=" + mb(cached) +
                ", buffers=" + mb(buffers) +
                ", slab=" + mb(slab) +
                ", shared=" + mb(shared) +
                ", available=" + mb(available) +
                ", active=" + mb(active) +
                ", used=" + mb(used) +
                ", free=" + mb(free) +
                ", percent=" + percent +
                ", total=" + mb(total) +
                '}';
    }

    private String mb(long f) {
        return (f * 1.0 / 1024 / 1024) + " mb";
    }

    private String mb(double f) {
        return (f / 1024 / 1024) + " mb";
    }
}