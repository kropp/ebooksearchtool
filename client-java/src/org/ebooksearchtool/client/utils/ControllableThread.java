package org.ebooksearchtool.client.utils;

/**
 * Date: 11.04.2010
 * Time: 18:39:42
 */
public abstract class ControllableThread extends Thread{

    class InterruptController implements Runnable{

        Thread myMasterThread;

        InterruptController(Thread master){
            myMasterThread = master;
        }

        public void run() {
            setDaemon(true);
            while(!myMasterThread.isInterrupted()){}
            setInterrupted(true);
        }

    }

    boolean myIsFinished = false;
    boolean myIsInterrupted = false;

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
        Thread intCon = new Thread(new InterruptController(Thread.currentThread()));
        intCon.start();
        execute();
    }

}
