package com.danny.systracedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int SPIN_COUNT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        int numThreads = 4;

        System.out.println("Starting " + numThreads + " threads");
        long startWhen = System.nanoTime();

        SpinThread threads[] = new SpinThread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SpinThread(i);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ie) {
                System.err.println("join " + i + " failed: " + ie);
            }
        }

        long endWhen = System.nanoTime();
        System.out.println("All threads finished in " +
                ((endWhen - startWhen) / 1000000) + "ms");
    }

    static class SpinThread extends Thread {
        private int mTid;

        SpinThread(int tid) {
            mTid = tid;
        }

        public void run() {
            long startWhen = System.nanoTime();
            System.out.println("Thread " + mTid + " started");
            int tid = mTid;
            int reps = SPIN_COUNT + tid;
            int ret = 0;

            for (int i = 0; i < reps; i++) {
                for (int j = 0; j < 100000; j++) {
                    ret += i * j;
                }
            }

            long endWhen = System.nanoTime();
            System.out.println("Thread " + mTid + " finished in " +
                    ((endWhen - startWhen) / 1000000) + "ms (" + ret + ")");
        }
    }
}
