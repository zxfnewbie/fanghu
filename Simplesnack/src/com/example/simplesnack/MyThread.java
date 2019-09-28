package com.example.simplesnack;

public class MyThread extends Thread{
    private GamePanel mv;
    public MyThread(GamePanel mv){
        this.mv=mv;
    }
    @Override
    public void run() {
        while (mv.start) {
            try {
                long before=System.currentTimeMillis();
                mv.postInvalidate();
                long after=System.currentTimeMillis();
                Thread.sleep((int)((10000/mv.speed)-(after-before)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (mv.getDirection()) {
            case 1:
                mv.move(-1, 0);
                mv.lastDirection=1;
                break;
            case 2:
                mv.move(0, -1);
                mv.lastDirection=2;
                break;
            case 3:
                mv.move(1, 0);
                mv.lastDirection=3;
                break;
            case 4:
                mv.move(0, 1);
                mv.lastDirection=4;
                break;
            default:
                break;
            }
            if(mv.hitOrBite()){
                mv.start=false;
                mv.dead();
                continue;
            }
        }
    }
}

