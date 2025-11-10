package org.example;

import org.example.third.ThirdTask;

public class Main {
    public static void main(String[] args) {
//        FirstTask firstTask = new FirstTask();
//        firstTask.execute(10);

        ThirdTask task = new ThirdTask();
        task.execute();

    }
}