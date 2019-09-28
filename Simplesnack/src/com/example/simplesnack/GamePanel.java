package com.example.simplesnack;

import java.util.ArrayList;
import java.util.Random;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class GamePanel extends View implements OnTouchListener{

    public int wid, hei;//获得此view的宽高
    public ArrayList<SnackBody> body = new ArrayList<SnackBody>();//蛇身
    public int score;//分数
    public double speed;//速度
    public boolean start;//开关标志
    private static final int MAX_Height = 20;
    private static final int MAX_Width = 20;//将活动区域分为20格
    private int rectSize;//每一格宽度
    private int snackLength;//蛇长（暂时无用）
    private int foodX, foodY;//食物坐标
    private Random random;
    private Paint paint;
    private int direction;//方向
    private int eatSign;//用于刷出下一个食物时判断是否和蛇身重叠
    private MyThread mThread;
    private DirectionThread lt = new DirectionThread(this, 1);
    private DirectionThread ut = new DirectionThread(this, 2);
    private DirectionThread rt = new DirectionThread(this, 3);
    private DirectionThread dt = new DirectionThread(this, 4);
    private int itemX, itemY; // 预判蛇头位置
    public int lastDirection; // 用于限制快速按键时出现反向吃自己的BUG

    public GamePanel(Context context) {
        super(context);
    }

    public GamePanel(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void init() {
        rectSize = wid / MAX_Width;
        score = 0;
        speed = 100;
        itemX = -1;
        itemY = -1;
        body.clear();
        snackLength = 0;
        random = new Random();
        paint = new Paint();
        paint.setAntiAlias(true);
        direction = 3;
        lastDirection = 3;
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (start) {
            paint.setColor(Color.RED);
            canvas.drawRect(foodX * rectSize, foodY * rectSize, foodX
                    * rectSize + rectSize, foodY * rectSize + rectSize, paint);
            if (body.size() > 0) {
                paint.setColor(Color.YELLOW);
                canvas.drawRect(body.get(0).getX() * rectSize, body.get(0)
                        .getY() * rectSize, body.get(0).getX() * rectSize
                        + rectSize, body.get(0).getY() * rectSize + rectSize,
                        paint);
                paint.setColor(Color.rgb(255, 215, 0));
                for (int i = 1; i < body.size(); i++) {
                    canvas.drawRect(body.get(i).getX() * rectSize, body.get(i)
                            .getY() * rectSize, body.get(i).getX() * rectSize
                            + rectSize, body.get(i).getY() * rectSize
                            + rectSize, paint);
                }
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);
                canvas.drawText("得分：" + score, wid - 120, wid - 20, paint);
            }
        }
    }

    public void startGame() {
        init();
        start = true;
        SnackBody newBody = new SnackBody();
        newBody.setX(MAX_Width / 2);
        newBody.setY(MAX_Height / 2);
        SnackBody newBody2 = new SnackBody();
        newBody2.setX(MAX_Width / 2 - 1);
        newBody2.setY(MAX_Width / 2);
        body.add(newBody);
        body.add(newBody2);
        do {
            foodX = random.nextInt(MAX_Width - 1);
            foodY = random.nextInt(MAX_Height - 1);
        } while (foodX == newBody.getX() && foodY == newBody.getY());
        mThread = new MyThread(this);
        mThread.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        wid = w;
        hei = h;
    }

    public void move(int x, int y) {
        itemX = body.get(0).getX() + x;
        itemY = body.get(0).getY() + y;
        if (itemX == foodX && itemY == foodY) {
            eat();

            for (int i = body.size() - 2; i > 0; i--) {
                body.get(i).setX(body.get(i - 1).getX());
                body.get(i).setY(body.get(i - 1).getY());
            }

        } else {

            for (int i = body.size() - 1; i > 0; i--) {
                body.get(i).setX(body.get(i - 1).getX());
                body.get(i).setY(body.get(i - 1).getY());
            }

        }
        body.get(0).setX(body.get(0).getX() + x);
        body.get(0).setY(body.get(0).getY() + y);
    }

    public void eat() {
        score = score + 10;
        snackLength++;
        do {
            eatSign = 0;
            foodX = random.nextInt(MAX_Height - 1);
            foodY = random.nextInt(MAX_Width - 1);
            for (int i = 0; i < body.size(); i++) {
                if (foodX == body.get(i).getX() && foodY == body.get(i).getY()) {
                    eatSign++;
                }
            }
            if (foodX == itemX && foodY == itemY)
                eatSign++;
        } while (eatSign > 0);
        SnackBody growBody = new SnackBody();
        growBody.setX(body.get(body.size() - 1).getX());
        growBody.setY(body.get(body.size() - 1).getY());
        body.add(growBody);
    }

    public boolean hitOrBite() {
        if (body.get(0).getX() < 0 || body.get(0).getX() >= 20
                || body.get(0).getY() < 0 || body.get(0).getY() >= 20)
            return true;
        else {
            for (int i = body.size() - 1; i > 2; i--) {
                if (body.get(0).getX() == body.get(i).getX()
                        && body.get(0).getY() == body.get(i).getY())
                    return true;
            }
        }
        return false;
    }

    public void dead() {

    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (v.getId() == R.id.left && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (lastDirection != 3) {
                lt = new DirectionThread(this, 1);
                ut.canRun = false;
                rt.canRun = false;
                dt.canRun = false;
                lt.canRun = true;
                lt.start();
            }
        } else if (v.getId() == R.id.left
                && e.getAction() == MotionEvent.ACTION_UP) {
            lt.canRun = false;
        }
        if (v.getId() == R.id.up && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (lastDirection != 4) {
                ut = new DirectionThread(this, 2);
                lt.canRun = false;
                ut.canRun = true;
                rt.canRun = false;
                dt.canRun = false;
                ut.start();
            }
        } else if (v.getId() == R.id.up
                && e.getAction() == MotionEvent.ACTION_UP) {
            ut.canRun = false;
        }
        if (v.getId() == R.id.right && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (lastDirection != 1) {
                rt = new DirectionThread(this, 3);
                lt.canRun = false;
                ut.canRun = false;
                rt.canRun = true;
                dt.canRun = false;
                rt.start();
            }
        } else if (v.getId() == R.id.right
                && e.getAction() == MotionEvent.ACTION_UP) {
            rt.canRun = false;
        }
        if (v.getId() == R.id.down && e.getAction() == MotionEvent.ACTION_DOWN) {
            if (lastDirection != 2) {
                dt = new DirectionThread(this, 4);
                lt.canRun = false;
                ut.canRun = false;
                rt.canRun = false;
                dt.canRun = true;
                dt.start();
            }
        } else if (v.getId() == R.id.down
                && e.getAction() == MotionEvent.ACTION_UP) {
            dt.canRun = false;
        }
        if (v.getId() == R.id.start && e.getAction() == MotionEvent.ACTION_UP&&start==false) {
            this.startGame();
        }
        if (v.getId() == R.id.gameContinue
                && e.getAction() == MotionEvent.ACTION_UP&&start==false) {
            start = true;
            mThread = new MyThread(this);
            mThread.start();
        }
        if (v.getId() == R.id.gameStop
                && e.getAction() == MotionEvent.ACTION_UP) {
            start = false;
        }
        return false;
    }
}
