package com.soft.cyberforest.itchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
    Bitmap  wall_bitmap, floor_bitmap, point_bitmap;
    public float PointSize;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wall_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        floor_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor);
        point_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point);
    }
    public GameView(Context context) {
        super(context);
        wall_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        floor_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floor);
        point_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point);
    }
    private static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static Bitmap FlipBitmap(Bitmap source)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 0);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }
    void DrawEpizode(Canvas c){
        //We have square field
        PointSize = c.getWidth()/Logic.getInstance().GameDeskSize;
        c.drawColor(Color.BLACK);
        floor_bitmap = Bitmap.createScaledBitmap(floor_bitmap, (int)PointSize,(int)PointSize,false);
        wall_bitmap = Bitmap.createScaledBitmap(wall_bitmap, (int)PointSize,(int)PointSize,false);
        point_bitmap = Bitmap.createScaledBitmap(point_bitmap, (int)PointSize,(int)PointSize,false);


        for (int i = 0; i < Logic.getInstance().GameDeskSize; i++){
            for(int j = 0; j < Logic.getInstance().GameDeskSize; j++){
                int State =Logic.getInstance().gamefield[i][j].getState();

                if(State>=40){
                    State = Logic.getInstance().Gosts.Gosts.get(State-40).my_point_state;
                }

                switch (State) {
                    case 0:
                        //c.drawRect(i*PointSize, j*PointSize,i+1*PointSize, j+1*PointSize ,new Paint());
                        c.drawBitmap(floor_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                        break;
                    case 1:
                        c.drawBitmap(floor_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                        c.drawBitmap(point_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                        break;
                    case 2:
                        c.drawBitmap(floor_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                    case 3:
                        c.drawBitmap(floor_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                        c.drawBitmap(wall_bitmap,(i*(PointSize)),(j*(PointSize)),new Paint());
                        break;
                    default: break;

                }
            }
        }
    }
    @Override
    public void draw(Canvas c){
        DrawEpizode(c);
    }

}
