package app.Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ex3 {

    public static void main(String[] args) {
        ExecutorService workingJack = Executors.newFixedThreadPool(17);
        System.out.println("Main starts");
        IntegerList integerList = new IntegerList();
        Lock lock = new ReentrantLock();
        for (int count = 0; count < 1000; count++) {
            workingJack.submit(new TaskToAddCount(integerList, count, lock));
        }
        System.out.println("Main is done");
        workingJack.shutdown();

    }

    private static class IntegerList {
        private static List<Integer> list = new ArrayList<>();
        public void addCount(int count, Lock lock) {
            lock.lock();
            try{
                list.add(count);
            } finally {
                lock.unlock();
            }
            System.out.println("Task: " + count + ": List size = " + list.size());
        }
    }
    private static class TaskToAddCount implements Runnable {
        // Gets a reference to the shared list and the count to add
        private IntegerList integerList;
        private int count;
        Lock lock; 

        TaskToAddCount(IntegerList integerList, int count, Lock lock) {
            this.integerList = integerList;
            this.count = count;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((int) Math.random()*800+200);
                
                integerList.addCount(count, lock);
            } catch (InterruptedException ex) {
                System.out.println("Thread was interrupted");
            }
        }
    }
}

