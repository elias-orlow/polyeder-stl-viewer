package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.DoubleAdder;

public class ParallelSurfaceAreaAccumulator
{
    private final BlockingQueue<SurfaceTask> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService;
    private final List<Future<?>> workerFutures = new ArrayList<>();
    private final DoubleAdder surfaceAreaSum = new DoubleAdder();
    private final int threadCount;

    public ParallelSurfaceAreaAccumulator()
    {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ParallelSurfaceAreaAccumulator(int threadCount)
    {
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount);
        startWorkers();
    }

    private void startWorkers()
    {
        for (int i = 0; i < threadCount; i++)
        {
            workerFutures.add(executorService.submit(this::work));
        }
    }

    private void work()
    {
        try
        {
            while (true)
            {
                SurfaceTask task = queue.take();

                if (task.isPoison())
                {
                    return;
                }

                surfaceAreaSum.add(task.getTriangle().area());
            }
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void accept(Triangle triangle)
    {
        try
        {
            queue.put(new SurfaceTask(triangle, false));
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public double finish()
    {
        try
        {
            for (int i = 0; i < threadCount; i++)
            {
                queue.put(new SurfaceTask(null, true));
            }

            for (Future<?> future : workerFutures)
            {
                future.get();
            }

            return surfaceAreaSum.sum();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            executorService.shutdown();
        }
    }

    public void cancel()
    {
        executorService.shutdownNow();
    }

    private static class SurfaceTask
    {
        private final Triangle triangle;
        private final boolean poison;

        public SurfaceTask(Triangle triangle, boolean poison)
        {
            this.triangle = triangle;
            this.poison = poison;
        }

        public Triangle getTriangle()
        {
            return triangle;
        }

        public boolean isPoison()
        {
            return poison;
        }
    }
}