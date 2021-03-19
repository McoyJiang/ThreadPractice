package com.danny.thread_practice;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static void main(String[] args) throws InterruptedException {
        ExampleUnitTest et = new ExampleUnitTest();
        et.testFooWithReentrantLock();
    }

    Foo foo = new Foo();

    private Semaphore two = new Semaphore(0);
    private Semaphore three = new Semaphore(0);

    public void testFooWithSemaphore() {
        Thread t1 = new Thread(() -> {
            foo.first();
            two.release();
        });

        Thread t2 = new Thread(() -> {
            try {
                two.acquire();
                foo.second();
                three.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                three.acquire();
                foo.third();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t3.start();
        t1.start();
        t2.start();
    }

    private void testFooWithReentrantLock() {
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                foo.first();
                //firstPrinted = true;
                c1.signal();
                System.out.println("c1 already signaled");
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("c1 await here");
                c1.await();
//                if (!firstPrinted) {
//                    c1.await();
//                }
                foo.second();
                //secondPrinted = true;
                c2.signal();
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                lock.lock();
//                if (!secondPrinted) {
//                    c2.await();
//                }
                c2.await();
                foo.third();
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }
        });

        t3.start();
        t2.start();
        t1.start();
    }

    private boolean firstPrinted = false;
    private boolean secondPrinted = false;

    final byte[] lock1 = new byte[0];
    final byte[] lock2 = new byte[0];

    public void testFooWithSynchronized() {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 running");
            synchronized (lock) {
                System.out.println("t1 acquire lock success");
                try {
                    //Thread.sleep(5000);
                    foo.first();
                    firstPrinted = true;
                    lock1.notify();
                } catch (Exception e) {

                }
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println("t2 running");
            synchronized (lock) {
                System.out.println("t2 acquire lock success");
                try {
                    //Thread.sleep(5000);
                    if (!firstPrinted) {
                        System.out.println("t2 await");
                        lock1.wait();
                    }
                    foo.second();
                    lock2.notify();
                } catch (Exception e) {

                }
            }
        });

        Thread t3 = new Thread(() -> {
            System.out.println("t3 running");
            synchronized (lock) {
                try {
                    System.out.println("t3 acquire lock success");
                    //Thread.sleep(5000);
                    if (!secondPrinted) {
                        System.out.println("t3 await");
                        lock2.wait();
                    }
                    foo.third();
                } catch (Exception e) {

                }
            }
        });

        t3.start();
        t1.start();
        t2.start();
    }

    FooBar fooBar = new FooBar();

    ReentrantLock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();

    public void testFooBar() {
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                fooBar.foo();
                c2.signal();
                c1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                lock.lock();
                fooBar.bar();
                c1.signal();
                c2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        t1.start();
        t2.start();

    }
}