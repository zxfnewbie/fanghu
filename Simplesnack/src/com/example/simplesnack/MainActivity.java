package com.example.simplesnack;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.graphics.Color;

public class MainActivity extends Activity {
    private GamePanel mv;
    private Button left,up,right,down;
    private Button start;
    private Button gameContinue;
    private Button gameStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);  
        int width = metric.widthPixels;     // ÆÁÄ»¿í¶È£¨ÏñËØ£©  
        int height = metric.heightPixels;   // ÆÁÄ»¸ß¶È£¨ÏñËØ£©  
        mv = (GamePanel)findViewById(R.id.mv);
        mv.setBackgroundColor(Color.LTGRAY);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mv.getLayoutParams();
        params.width = width;
        params.height = width;
        mv.setLayoutParams(params);
        left=(Button)findViewById(R.id.left);
        up=(Button)findViewById(R.id.up);
        right=(Button)findViewById(R.id.right);
        down=(Button)findViewById(R.id.down);       
        left.setWidth(width/4);
        left.setHeight(width/5);
        up.setWidth(width/4);
        up.setHeight(width/5);
        right.setWidth(width/4);
        right.setHeight(width/5);
        down.setWidth(width/4);
        down.setHeight(width/5);
        left.setX(width/2-width/8-width/5);
        left.setY(width+(height-width)/3+width/10);
        up.setX(width/2-width/8);
        up.setY(width+(height-width)/3-width/20);
        right.setX(width/2-width/8+width/5);
        right.setY(width+(height-width)/3+width/10);
        down.setX(width/2-width/8);
        down.setY(width+(height-width)/3+width/5+width/20);
        start=(Button)findViewById(R.id.start);
        start.setOnTouchListener(mv);
        gameContinue=(Button)findViewById(R.id.gameContinue);
        gameContinue.setOnTouchListener(mv);
        gameStop=(Button)findViewById(R.id.gameStop);
        gameStop.setOnTouchListener(mv);
        left.setOnTouchListener(mv);
        up.setOnTouchListener(mv);
        right.setOnTouchListener(mv);
        down.setOnTouchListener(mv);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mv.start=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
