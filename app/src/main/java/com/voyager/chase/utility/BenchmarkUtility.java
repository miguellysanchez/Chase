package com.voyager.chase.utility;

import java.util.HashMap;

import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/25/16.
 */
public class BenchmarkUtility {

    private static HashMap<String, Long> sBenchmarkTimerStartMap;

    /**
     * Usage for benchmarking:
     * Call once at the start of benchmarking with a key, then
     * call again with the same key to get elapsed time from start
     * time stored for that key
     */
    public static long startOrStopBenchmarkTimer(String key) {
        long benchmarkStart = System.currentTimeMillis();
        if (sBenchmarkTimerStartMap == null) {
            sBenchmarkTimerStartMap = new HashMap<>();
        }
        long timeMillis;
        Long storedTime = sBenchmarkTimerStartMap.get(key);
        if (storedTime == null) { // first call == start timer
            timeMillis = System.currentTimeMillis();
            sBenchmarkTimerStartMap.put(key, timeMillis);
            Timber.d(">>> [%s] Time started at: %s" ,key, ""+timeMillis);
            return timeMillis;
        } else { // second call == stop timer, and reset key map
            long timeElapsed = benchmarkStart - sBenchmarkTimerStartMap.get(key);
            sBenchmarkTimerStartMap.remove(key);
            Timber.d(">>> [%s] Time end at: %s" ,key, ""+benchmarkStart);
            Timber.d(">>> [%s] Time ELAPSED at: %s" ,key, ""+timeElapsed);
            return timeElapsed;
        }
    }
}
