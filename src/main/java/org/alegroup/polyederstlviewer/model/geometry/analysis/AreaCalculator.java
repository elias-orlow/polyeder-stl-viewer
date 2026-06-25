package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Calculates the surface area of triangles in parallel.
 *
 * @precondition Triangles are valid and not null.
 * @postcondition Surface area can be calculated while triangles are being read.
 */
public class AreaCalculator
{
    private final ExecutorService executorService;
    private final List<Future<Double>> futures;
    private final List<Triangle> currentBatch;

    private final int threadCount;
    private final int batchSize;

    private int triangleCount;
    private final long startTime;

    private final AtomicInteger threadCounter = new AtomicInteger();

    /**
     * Creates an area calculator using the available processor count.
     *
     * @precondition None.
     * @postcondition A new AreaCalculator object is created.
     */
    public AreaCalculator()
    {
        this(Runtime.getRuntime().availableProcessors(), 1000);
    }

    /**
     * Creates an area calculator with custom thread count and batch size.
     *
     * @param threadCount the number of worker threads
     * @param batchSize the number of triangles per calculation task
     *
     * @precondition threadCount > 0 and batchSize > 0.
     * @postcondition A new AreaCalculator object is created.
     */
    public AreaCalculator(int threadCount, int batchSize)
    {
        this.threadCount = threadCount;
        this.batchSize = batchSize;
        this.executorService = Executors.newFixedThreadPool(threadCount, runnable ->
        {
            Thread thread = new Thread(runnable);
            thread.setName("area-worker-" + threadCounter.incrementAndGet());
            return thread;
        });
        this.futures = new ArrayList<>();
        this.currentBatch = new ArrayList<>(batchSize);
        this.triangleCount = 0;
        this.startTime = System.nanoTime();
    }

    /**
     * Adds a triangle to the parallel surface area calculation.
     *
     * @param triangle the triangle to calculate
     *
     * @precondition triangle is not null.
     * @postcondition The triangle is added to the calculation.
     */
    public void addTriangle(Triangle triangle)
    {
        currentBatch.add(triangle);
        triangleCount++;

        if (currentBatch.size() >= batchSize)
        {
            submitCurrentBatch();
        }
    }

    /**
     * Submits the current batch to the executor service.
     *
     * @precondition currentBatch is not empty.
     * @postcondition The batch is submitted for parallel calculation.
     */
    private void submitCurrentBatch()
    {
        List<Triangle> batch = new ArrayList<>(currentBatch);
        currentBatch.clear();

        Future<Double> future = executorService.submit(() ->
        {
            double localSum = 0.0;

            for (Triangle triangle : batch)
            {
                localSum += triangle.area();
            }

            return localSum;
        });

        futures.add(future);
    }

    /**
     * Finishes the calculation and returns the result.
     *
     * @return the calculated area result
     *
     * @precondition All triangles have been added.
     * @postcondition The final surface area result is returned.
     */
    public AreaResult finish()
    {
        if (!currentBatch.isEmpty())
        {
            submitCurrentBatch();
        }

        double totalArea = 0.0;

        try
        {
            for (Future<Double> future : futures)
            {
                totalArea += future.get();
            }
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

        long duration = System.nanoTime() - startTime;

        return new AreaResult(totalArea, duration, triangleCount, threadCount);
    }

    /**
     * Cancels the calculation.
     *
     * @precondition None.
     * @postcondition Running tasks are interrupted.
     */
    public void cancel()
    {
        executorService.shutdownNow();
    }
}