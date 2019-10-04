package org.muzima.turkana.async;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * An a custom implementation of {@link java.util.concurrent.Future }
 *
 * A Future represents the result of an asynchronous computation.
 * Methods are provided to check if the computation is complete, to wait for its completion,
 * and to retrieve the result of the computation.
 *
 * The result can only be retrieved using method get when the computation has completed,
 * blocking if necessary until it is ready. Cancellation is performed by the cancel method.
 * Additional methods are provided to determine if the task completed normally or was cancelled.
 * Once a computation has completed, the computation cannot be cancelled.
 * If you would like to use a Future for the sake of cancellability but not provide a usable result,
 * you can declare types of the form Future<?> and return null as a result of the underlying task.
 *
 * @param <V>
 */

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

    public ListenableFutureTask(Callable<V> callable, Object identifier, Executor callbackExecutor) {
        super(callable);
        this.identifier = identifier;
        this.callbackExecutor = callbackExecutor;
    }


    public ListenableFutureTask(final V result) {
        this(result, null);
    }

    public ListenableFutureTask(final V result, Object identifier) {
        super(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return result;
            }
        });
        this.identifier = identifier;
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
        else callbackExecutor.execute(callbackRunnable);
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
        else return super.hashCode();
    }
}
