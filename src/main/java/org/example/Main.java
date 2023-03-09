package org.example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //Tomcat tomcat = new Tomcat();
        //tomcat.start();
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // submit tasks to the thread pool
        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.println(i+"start");
            executor.submit(new Task(i));
            System.out.println(i+"end");
        }

        // shut down the thread pool
        executor.shutdown();

    }
}

class Task implements Runnable {
    private int taskId;

    public Task(int id) {
        this.taskId = id;
    }

    public void run() {
        System.out.println("Task " + taskId + " is running.");
    }
}





