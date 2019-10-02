package org.muzima.turkana.async;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class ListenableFutureTask<V> extends FutureTask<V> {

    private final List<FutureTaskListener<V>> listeners = new LinkedList<>();

    private final Object identifier;

    private final Executor callbackExecutor;

    public ListenableFutureTask(Callable<V> callable) {
        this(callable, null);
    }

    public ListenableFutureTask(Callable<V> callable, Object identifier) {
        this(callable, identifier, null);
    }

    public ListenableFutureTask(Callable<V> callable,Object identifier,Executor callbackExecutor) {
        super(callable);
        this.identifier       = identifier;
        this.callbackExecutor = callbackExecutor;
    }


    public ListenableFutureTask(final V result) {
        this(result, null);
    }

    public ListenableFutureTask(final V result,Object identifier) {
        super(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return result;
            }
        });
        this.identifier       = identifier;
        this.callbackExecutor = null;
        this.run();
    }

    public synchronized void addListener(FutureTaskListener<V> listener) {
        if (this.isDone()) {
            callback(listener);
        } else {
            this.listeners.add(listener);
        }
    }

    public synchronized void removeListener(FutureTaskListener<V> listener) {
        this.listeners.remove(listener);
    }

    @Override
    protected synchronized void done() {
        callback();
    }

    private void callback() {
        Runnable callbackRunnable = new Runnable() {
            @Override
            public void run() {
                for (FutureTaskListener<V> listener : listeners) {
                    callback(listener);
                }
            }
        };

        if (callbackExecutor == null) callbackRunnable.run();
        else                          callbackExecutor.execute(callbackRunnable);
    }

    private void callback(FutureTaskListener<V> listener) {
        if (listener != null) {
            try {
                listener.onSuccess(get());
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            } catch (ExecutionException e) {
                listener.onFailure(e);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof ListenableFutureTask && this.identifier != null) {
            return identifier.equals(other);
        } else {
            return super.equals(other);
        }
    }

    @Override
    public int hashCode() {
        if (identifier != null) return identifier.hashCode();
        else                    return super.hashCode();
    }
}
