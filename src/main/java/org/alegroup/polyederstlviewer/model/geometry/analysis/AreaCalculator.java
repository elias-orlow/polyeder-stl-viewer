package org.alegroup.polyederstlviewer.model.geometry.analysis;

import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs parallel surface area calculation for triangles.
 * Triangles can be added incrementally, and batches are processed
 * asynchronously using a fixed-size thread pool.
 *
 * @precondition Triangles added must be non-null and valid.
 * @postcondition Surface area can be computed in parallel while triangles are being streamed.
 */
public class AreaCalculator
{

    /**
     * Executor service handling parallel computation tasks.
     */
    private final ExecutorService executorService;

    /**
     * List of futures representing submitted area computation tasks.
     */
    private final List<Future<Double>> futures;

    /**
     * Current batch of triangles awaiting submission.
     */
    private final List<Triangle> currentBatch;

    /**
     * Number of worker threads used for computation.
     */
    private final int threadCount;

    /**
     * Number of triangles processed per batch.
     */
    private final int batchSize;

    /**
     * Total number of triangles added.
     */
    private int triangleCount;

    /**
     * Timestamp marking the start of the calculation.
     */
    private final long startTime;

    /**
     * Counter used to assign unique names to worker threads.
     */
    private final AtomicInteger threadCounter = new AtomicInteger();

    /**
     * Creates an AreaCalculator using the number of available processors
     * and a default batch size of 1000 triangles.
     *
     * @precondition none
     * @postcondition A new AreaCalculator instance is created
     */
    public AreaCalculator ()
    {
        this(Runtime.getRuntime().availableProcessors(), 1000);
    }

    /**
     * Creates an AreaCalculator with custom thread count and batch size.
     *
     * @param threadCount number of worker threads (must be > 0)
     * @param batchSize   number of triangles per batch (must be > 0)
     * @precondition threadCount > 0 AND batchSize > 0
     * @postcondition A new AreaCalculator instance is created
     */
    public AreaCalculator (int threadCount, int batchSize)
    {
        this.threadCount = threadCount;
        this.batchSize = batchSize;

        this.executorService = Executors.newFixedThreadPool(threadCount, runnable -> {
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
     * Once the batch size is reached, the batch is submitted for processing.
     *
     * @param triangle the triangle to add
     * @precondition triangle != null
     * @postcondition Triangle is added and may trigger batch submission
     */
    public void addTriangle (Triangle triangle)
    {
        currentBatch.add(triangle);
        triangleCount++;

        if (currentBatch.size() >= batchSize)
        {
            submitCurrentBatch();
        }
    }

    /**
     * Submits the current batch of triangles to the executor service.
     *
     * @precondition currentBatch is not empty
     * @postcondition Batch is submitted for parallel computation
     */
    private void submitCurrentBatch ()
    {

        List<Triangle> batch = new ArrayList<>(currentBatch);
        currentBatch.clear();

        Future<Double> future = executorService.submit(() -> {
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
     * Completes the calculation and returns the final result.
     * Any remaining triangles are submitted before aggregation.
     *
     * @return an AreaResult containing total area, duration, triangle count, and thread count
     * @precondition All triangles intended for calculation have been added
     * @postcondition Executor service is shut down and final result is returned
     */
    public AreaResult finish ()
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
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            executorService.shutdown();
        }

        long duration = System.nanoTime() - startTime;

        return new AreaResult(totalArea, duration, triangleCount, threadCount);
    }

    /**
     * Cancels all running tasks and stops further processing.
     *
     * @precondition none
     * @postcondition All running tasks are interrupted
     */
    public void cancel ()
    {
        executorService.shutdownNow();
    }
}