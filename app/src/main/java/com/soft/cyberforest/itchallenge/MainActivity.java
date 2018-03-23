package com.soft.cyberforest.itchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    Timer timer = null;
    static int SHADOW = 400, GOST = 200, POLTERGAIST = 100;
    public static int StepTime = GOST;
    GameFrame GF;
    View.OnTouchListener GameFieldTouch;
    final int SNK_WIN = 1, SNK_LOSE = 2, SNK_PAUSE = 3, SNK_START = 4;
    int GameState = SNK_START;
    float control_x = -1, control_y = -1;
    MainActivity act = this;
    ProgressBar score;
    TextView score_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LogicIni();

        GF = (GameFrame) findViewById(R.id.gf);
        EventsInit();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSnack();
            }
        });

        GF.setOnTouchListener(GameFieldTouch);
        findViewById(R.id.score_panel).setOnTouchListener(GameFieldTouch);
        findViewById(R.id.score_panel).setBackgroundColor(Color.BLACK);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        score = (ProgressBar)findViewById(R.id.progressBar2);
        score.setMax(0);
        score.setMax(100);

        score_label = (TextView)findViewById(R.id.score_label);


        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/pixelfont3.ttf");
        score_label.setTypeface(tf);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    void StartTimer() {
        Logic.Reset();
        LogicIni();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!Logic.getInstance().GameOver) {
                    Logic.getInstance().MakeStep(Logic.getInstance().CurrentDirection);
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GF.Build();
                            score.setProgress((int)Logic.getInstance().getScore(0,100));
                            int score = (int)Logic.getInstance().getScore();
                            if(score>0)
                                score_label.setText("Just "+score+"  left!");
                            else
                                score_label.setText("You Win!!!");
                        }
                    });
                }else {
                    this.cancel();
                    StopTimer();
                    if (Logic.getInstance().IsWin())
                        GameOver(true);
                    else
                        GameOver(false);
                }



            }
        }, StepTime, StepTime);
    }

    void StopTimer() {
        timer.cancel();
        timer.purge();
    }

    void EventsInit() {
        GameFieldTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        control_x = e.getX();
                        control_y = e.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float offsetX = control_x - e.getX();
                        float offsetY = control_y - e.getY();

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < 0) Logic.getInstance().CurrentDirection = Logic.RIGHT;
                            else Logic.getInstance().CurrentDirection = Logic.LEFT;
                        } else {
                            if (offsetY < 0) Logic.getInstance().CurrentDirection = Logic.DOWN;
                            else Logic.getInstance().CurrentDirection = Logic.UP;
                        }

                        break;
                    default:
                        return false;

                }
                return true;
            }
        };
    }

    void GameOver(boolean iswin) {
        if (iswin) {
            GameState = SNK_WIN;
            ShowSnack();
        } else {
            GameState = SNK_LOSE;
            ShowSnack();
        }
    }

    void ShowSnack() {
        switch (GameState) {
            case SNK_LOSE:
                GameState = SNK_LOSE;
                Snackbar.make(GF, "Game over", Snackbar.LENGTH_LONG)
                        .setAction("Restart", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StartTimer();
                                GameState = SNK_PAUSE;
                            }
                        }).setActionTextColor(Color.YELLOW).show();
                break;
            case SNK_WIN:

                GameState = SNK_WIN;
                Snackbar.make(GF, "You win", Snackbar.LENGTH_LONG)
                        .setAction("Restart", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StartTimer();

                                GameState = SNK_PAUSE;
                            }
                        }).setActionTextColor(Color.YELLOW).show();

                GameState = SNK_WIN;
                break;
            case SNK_PAUSE:
                Snackbar.make(GF, "Stop the game?", Snackbar.LENGTH_LONG)
                        .setAction("Stop", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StopTimer();
                                GameState = SNK_START;
                            }
                        }).setActionTextColor(Color.YELLOW).show();
                break;
            case SNK_START:
                Snackbar.make(GF, "Start the game?", Snackbar.LENGTH_LONG)
                        .setAction("Start", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                StartTimer();
                                GameState = SNK_PAUSE;
                            }
                        }).setActionTextColor(Color.YELLOW).show();

                GameState = SNK_START;
                break;
        }
    }

    void LogicIni() {
        Logic.getInstance().setLevel();
        // Logic.getInstance().setLevel(R.raw.level1, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_htp) {
            Intent htp = new Intent(this,HTPActivity.class);
            startActivity(htp);
        } else if (id == R.id.nav_restart) {
            if (timer != null) StopTimer();
            Logic.Reset();
            LogicIni();
            StartTimer();
            GameState = SNK_PAUSE;
        } else if (id == R.id.lvl_shadow){
            StepTime = SHADOW;
        } else if (id == R.id.lvl_gost){
            StepTime = GOST;
        } else if (id == R.id.lvl_poltergaist){
            StepTime = POLTERGAIST;
        }else if(id==R.id.nav_share){
            String text = "";
            if(Logic.getInstance().IsWin()){
                text+="I have won in Swippy Pacman";
            }else {
                text+="I lacked only "+(int)Logic.getInstance().getScore()+" points to win in Swippy Pacman";
            }
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/txt");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent,"Share you score"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                control_x = e.getX();
                control_y = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float offsetX = control_x - e.getX();
                float offsetY = control_y - e.getY();

                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (offsetX < 0) Logic.getInstance().CurrentDirection = Logic.RIGHT;
                    else Logic.getInstance().CurrentDirection = Logic.LEFT;
                } else {
                    if (offsetY < 0) Logic.getInstance().CurrentDirection = Logic.DOWN;
                    else Logic.getInstance().CurrentDirection = Logic.UP;
                }

                break;
            default:
                return false;

        }
        return false;
    }
}
