package com.example.prathip.canvasproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Prathip on 30-08-2016.
 */

public class MyView extends SurfaceView implements Runnable,SurfaceHolder.Callback{

    Thread t = null;
    SurfaceHolder holder;
    boolean isItOk = false;
    protected My3DObj vertices[];
    protected int faces[][];
    protected int colors[];
    protected float ax;
    protected float ay;
    protected float az;
    protected float lastTouchX;
    protected float lastTouchY;


    public MyView(Context context) {
        super(context);
        holder = getHolder();
        t= new Thread(this);
        t.start();
    }

    public void initialize() {
        vertices = new My3DObj[] {
                new My3DObj(-1.5, 1.5, -1.5),
                new My3DObj(1.5, 1.5, -1.5),
                new My3DObj(1.5, -1.5, -1.5),
                new My3DObj(-1.5, -1.5, -1.5),
                new My3DObj(-1.5, 1.5, 1.5),
                new My3DObj(1.5, 1.5, 1.5),
                new My3DObj(1.5, -1.5, 1.5),
                new My3DObj(-1.5, -1.5, 1.5)
        };

        faces = new int[][] {{0, 1, 2, 3}, {1, 5, 6, 2}, {5, 4, 7, 6}, {4, 0, 3, 7}, {0, 4, 5, 1}, {3, 2, 6, 7}};
        colors = new int[] {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA};
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ||event.getAction() == MotionEvent.ACTION_UP ) {
            lastTouchX = event.getX();
            lastTouchY = event.getY();
        } else if( event.getAction() == MotionEvent.ACTION_MOVE ) {
            float dx = (event.getX() - lastTouchX) / 30.0f;
            float dy = (event.getY() - lastTouchY) / 30.0f;
            ax += dy;
            ay -= dx;
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        My3DObj t[]  = new My3DObj[8];
        double avgZ[] = new double[6];
        int order[] = new int[6];

        for( int i = 0; i < 8; i++ ) {
            t[i] = vertices[i].rotateX(ax).rotateY(ay).rotateZ(az);
            t[i] = t[i].project(getWidth(), getHeight(), 256, 4);
        }

        for( int i = 0; i < 6; i++ ) {
            avgZ[i] = (t[faces[i][0]].z + t[faces[i][1]].z + t[faces[i][2]].z + t[faces[i][3]].z) / 4;
            order[i] = i;
        }

        for( int i = 0; i < 6;i++)
        {
            int orderNo = i;
            for( int j = i + 1; j <6; j++ ) {
                if(avgZ[orderNo] <avgZ[j] )
                {
                    orderNo = j;
                }
            }
            if( orderNo != i ) {
                double tempValue = avgZ[i];
                avgZ[i] = avgZ[orderNo];
                avgZ[orderNo] = tempValue;

                int tempOrder = order[i];
                order[i] = order[orderNo];
                order[orderNo] = tempOrder;
            }
        }



        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();

        for( int i = 0; i < 6; i++ ) {
        int index = order[i];
        Path p = new Path();
        p.moveTo((float)t[faces[index][0]].x, (float)t[faces[index][0]].y);
        p.lineTo((float)t[faces[index][1]].x, (float)t[faces[index][1]].y);
        p.lineTo((float)t[faces[index][2]].x, (float)t[faces[index][2]].y);
        p.lineTo((float)t[faces[index][3]].x, (float)t[faces[index][3]].y);
        p.close();
        paint.setColor(colors[index]);
        canvas.drawPath(p, paint);
        }
        }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        initialize();
        setFocusableInTouchMode(true);
        while(isItOk == true){
            if(!holder.getSurface().isValid())
            {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
           try {
               onDraw(canvas);
           }finally {
               holder.unlockCanvasAndPost(canvas);
           }
        }
    }


    public void pause(){

        isItOk = false;
        while (true)
        {
            try {
                t.join();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume(){

        isItOk = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.holder=holder;
            initialize();
            setFocusableInTouchMode(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
