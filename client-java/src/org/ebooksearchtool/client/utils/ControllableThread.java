package org.ebooksearchtool.client.utils;

/**
 * Date: 11.04.2010
 * Time: 18:39:42
 */
public abstract class ControllableThread extends Thread{

    class InterruptController implements Runnable{

        Thread myMasterThread;
        Object myMonitor;

        InterruptController(Thread master, Object monitor){
            myMasterThread = master;
            myMonitor = monitor;
        }

        public void run() {
            //setDaemon(true);
            //while(!myMasterThread.isInterrupted()){}
            //synchronized (myMonitor){
            //setInterrupted(true);
           // }
        }

    }

    boolean myIsFinished = false;
    boolean myIsInterrupted = false;
    final Object monitor = new Object();

    public void setFinished(){
        myIsFinished = true;
    }

    public boolean isFinished(){
        return myIsFinished;
    }

    public void setInterrupted(boolean interrupt){
        myIsInterrupted = interrupt;
    }

    public boolean isInterrupted(){
        return myIsInterrupted;
    }

    abstract public void execute();

    public void run(){
        Thread intCon = new Thread(new InterruptController(Thread.currentThread(), monitor));
        synchronized (monitor){
        intCon.start();
        execute();
        }
    }

}
