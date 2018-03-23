package com.soft.cyberforest.itchallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameFrame extends FrameLayout {
    Bitmap cow_bitmap, wall_bitmap, alien_bitmap, floor_bitmap, point_bitmap;
    float PointSize;
    ImageView Cow;
    ArrayList<ImageView> Aliens = new ArrayList<>();
    GameView GV;

    public GameFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);

        PointSize =  width/Logic.getInstance().GameDeskSize;
    }

    void Build(){
        float cow_x = Logic.getInstance().pacman.x * PointSize;
        float cow_y = Logic.getInstance().pacman.y * PointSize;
        Cow.animate().x(cow_x).y(cow_y).setDuration(0).start();

        for (int i = 0; i<4; i++) {
            float al_x =  Logic.getInstance().Gosts.Gosts.get(i).x * PointSize;
            float al_y =  Logic.getInstance().Gosts.Gosts.get(i).y * PointSize;
            Aliens.get(i).animate().x(al_x).y(al_y).setDuration(0).start();
        }
    }
    void Init(Context context){
        GV = new GameView(context);
        GV.invalidate();
        PointSize = GV.PointSize;
        cow_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);
        cow_bitmap = Bitmap.createScaledBitmap(cow_bitmap, (int)PointSize,(int)PointSize,false);
        int t = 0;
        Cow = new ImageView(context);
        Cow.setLayoutParams(new LayoutParams((int)PointSize, (int)PointSize));
        Cow.setImageBitmap(cow_bitmap);


        alien_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_gost);
        alien_bitmap = Bitmap.createScaledBitmap(alien_bitmap, (int)PointSize,(int)PointSize,false);
        for (int i = 0; i<4; i++) {
            Aliens.add(new ImageView(context));
            Aliens.get(i).setImageBitmap(alien_bitmap);
            Aliens.get(i).setLayoutParams(new LayoutParams((int)PointSize, (int)PointSize));
        }
        Build();
    }

    public GameFrame(Context context) {
        super(context);
    }

}
