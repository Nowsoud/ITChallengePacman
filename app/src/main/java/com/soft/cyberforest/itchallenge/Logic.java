package com.soft.cyberforest.itchallenge;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

public class Logic {

    public static Logic instance = null;

    public static Logic getInstance(){

        if(instance == null){
            instance = new Logic();
        }

        return instance;
    }

    private void fastfix(){
        for (int i = 0; i < GameDeskSize; i++){
            for(int j = 0; j < GameDeskSize; j++){
                if(gamefield[i][j].getState() == 40&&(Gosts.Gosts.get(0).x != i||Gosts.Gosts.get(0).y != j))
                    gamefield[i][j].setState(0);

                if(gamefield[i][j].getState() == 41&&(Gosts.Gosts.get(1).x != i||Gosts.Gosts.get(1).y != j))
                    gamefield[i][j].setState(0);

                if(gamefield[i][j].getState() == 42&&(Gosts.Gosts.get(2).x != i||Gosts.Gosts.get(2).y != j))
                    gamefield[i][j].setState(0);

                if(gamefield[i][j].getState() == 43&&(Gosts.Gosts.get(3).x != i||Gosts.Gosts.get(3).y != j))
                    gamefield[i][j].setState(0);
            }
        }
    }

    public static void Reset(){
        instance = new Logic();
    }

    public Levels levels;
    int[] CurrentDirection = Logic.DOWN;
    public int GameDeskSize = 13;
    public static final int[] UP = {0,-1}, DOWN = {0,1}, LEFT={-1,0},RIGHT={1,0};
    Pacman pacman = new Pacman();
    GostCoordinator Gosts = new GostCoordinator();
    public boolean GameOver = false;
    Point gamefield[][] = new Point[GameDeskSize][GameDeskSize];
    float voids = 0, food = 0;
    public float getScore(int min, int max){
        int stars = max - min;
        return  (voids/food) * stars;
    }
    public float getScore(){
        return  (food - voids)-2;
    }
    protected Logic(){


    }

    public void setLevel(int LevelResouce, Context context){
        levels = new Levels(context);
        gamefield = levels.getLevel(LevelResouce);
        gamefield[pacman.x][pacman.y].setState(2);
    }

    public void setLevel() {
        //random generation
        new MapBuilder().Build();
    }

    public void MakeStep(int[] Direction){
        if(Gosts.IsPacmanOnUs()) {
            GameOver = true;
            return;
        }
        if(IsWin()) {
            GameOver = true;
            return;
        }
        if(pacman.my_point_state == 1) voids++;
        gamefield[pacman.x][pacman.y].setState(0);
        pacman.Move(Direction);
        pacman.my_point_state = gamefield[pacman.x][pacman.y].getState();
        gamefield[pacman.x][pacman.y].setState(2);

        if(Gosts.IsPacmanOnUs()) {
            GameOver = true;
            return;
        }


        Gosts.GostsStep();

        fastfix();

    }

    public boolean IsWin(){
        for (int i = 0; i < GameDeskSize; i++){
            for(int j = 0; j < GameDeskSize; j++){
                if(gamefield[i][j].getState() == 1) return false;
            }
        }
        return true;
    }

    class Pacman{
        public int x,y;
        int my_point_state = 1;
        Pacman(){
            x = GameDeskSize /2;
            y = GameDeskSize/ 2;
        }

        void Move(int[] direction){
            if(x+direction[0] >= GameDeskSize) {
                x = 0;
                y += direction[1];
            }else if(x+direction[0] < 0) {
                x = GameDeskSize-1;
                y += direction[1];
            }else if(y+direction[1] >= GameDeskSize) {
                x += direction[0];
                y = 0;
            }else
            if(y+direction[1] < 0) {
                x += direction[0];
                y = GameDeskSize-1;
            }else if(gamefield[x+direction[0]][y+direction[1]].getState() == 3){
                //if pacmac meets wall - do nothing
            }else
            {
                x += direction[0];
                y += direction[1];
            }
        }
    }

    class Gost{
        int id;
        int my_point_state = 1;
        int[] direction = RIGHT;
        public int x,y;
        Gost(int id, int x, int y){
            this.x = x;
            this.y = y;

            this.id = id;
        }
        void ChangeDirection(){
            switch (new Random().nextInt(4)){
                case 0:
                    direction = RIGHT;
                    break;
                case 1:
                    direction = LEFT;
                    break;
                case 2:
                    direction = UP;
                    break;
                case 3:
                    direction = DOWN;
                    break;
            }
        }
        void Move(){
            if(new Random().nextInt(4) == 0) ChangeDirection();


            if(x+direction[0] >= GameDeskSize) {
                x = 0;
                y += direction[1];
            }else if(x+direction[0] < 0) {
                x = GameDeskSize-1;
                y += direction[1];
            }else if(y+direction[1] >= GameDeskSize) {
                x += direction[0];
                y = 0;
            }else
            if(y+direction[1] < 0) {
                x += direction[0];
                y = GameDeskSize-1;
            }else if(gamefield[x+direction[0]][y+direction[1]].getState() >= 3){
                //if gost meets wall - he changes his direction
                //if gost meets another gosts - he changes his direction
                ChangeDirection();
            }else
            {
                x += direction[0];
                y += direction[1];
            }
        }


    }

    class GostCoordinator{
        //gost id can be from 4 to 7 (4 colors of gosts)
        ArrayList<Gost> Gosts = new ArrayList<>();
        GostCoordinator(){
            Gosts.add(new Gost(40,1,1));

            Gosts.add(new Gost(41,GameDeskSize - 2,1));

            Gosts.add(new Gost(42,1,GameDeskSize - 2));

            Gosts.add(new Gost(43,GameDeskSize - 2,GameDeskSize - 2));
        }

        public void GostsStep(){
            for (Gost gost: Gosts) {
                gamefield[gost.x][gost.y].setState(gost.my_point_state);
                gost.Move();
                if(pacman.x == gost.x&&pacman.y == gost.y) {
                    GameOver = true;
                    return;
                }
                gost.my_point_state = gamefield[gost.x][gost.y].getState();
                gamefield[gost.x][gost.y].setState(gost.id);
            }


        }

        boolean IsPacmanOnUs(){
            for (Gost gost: Gosts) {
                if(pacman.x == gost.x&&pacman.y == gost.y) {
                    return true;
                }
            }
            return false;
        }
    }


    class MapBuilder {
        ArrayList<BuildUnit> units = new ArrayList<>();
        int OrnateIndex = 4;
        float Emptines = 2;

        MapBuilder() {
            for (int i = 0; i < GameDeskSize; i++)
                for (int j = 0; j < GameDeskSize; j++)
                    gamefield[i][j] = new Point(3);

            units.add(new BuildUnit(1, 1));
            units.add(new BuildUnit(1, GameDeskSize - 2));
            units.add(new BuildUnit(GameDeskSize - 2, 1));
            units.add(new BuildUnit(GameDeskSize - 2, GameDeskSize - 2));
            units.add(new BuildUnit());
        }

        int[] ChangeDirection() {
            int[] direction = new int[2];
            switch (new Random().nextInt(4)) {
                case 0:
                    direction = RIGHT;
                    break;
                case 1:
                    direction = LEFT;
                    break;
                case 2:
                    direction = UP;
                    break;
                case 3:
                    direction = DOWN;
                    break;
            }
            return direction;
        }

        private float EmptyDivWall() {
            float walls = 0, empty = 0;

            for (int i = 0; i < GameDeskSize; i++)
                for (int j = 0; j < GameDeskSize; j++)
                    if (gamefield[i][j].getState() == 3) walls++;
                    else empty++;
            return empty / walls;
        }
        private void SetFoodCount() {
            food = 0;
            for (int i = 0; i < GameDeskSize; i++)
                for (int j = 0; j < GameDeskSize; j++)
                    if (gamefield[i][j].getState() == 1) food++;
        }

        public void Build() {
            while (EmptyDivWall() < Emptines) {
                for (BuildUnit u : units) {
                    if (new Random().nextInt(OrnateIndex) == 0) u.direction = ChangeDirection();
                    u.Move();
                }
            }
            for (int i = 0; i < GameDeskSize; i++) {
                if (gamefield[i][0].getState() != gamefield[i][GameDeskSize - 1].getState())
                    gamefield[i][0].setState(gamefield[i][GameDeskSize - 1].getState());

                if (gamefield[0][i].getState() != gamefield[GameDeskSize - 1][i].getState())
                    gamefield[0][i].setState(gamefield[GameDeskSize - 1][i].getState());
            }

            SetFoodCount();
        }

        class BuildUnit {

            int[] direction = new int[2];

            public int x, y;

            BuildUnit() {
                x = GameDeskSize / 2;
                y = GameDeskSize / 2;
                this.direction = ChangeDirection();
            }

            BuildUnit(int x, int y) {
                this.x = x;
                this.y = y;
                this.direction = ChangeDirection();
            }

            void Move() {
                try {
                    x += direction[0];
                    y += direction[1];

                    gamefield[x][y].setState(1);
                } catch (Exception e) {
                    if (x >= GameDeskSize)
                        x = 0;
                    if (x < 0)
                        x = GameDeskSize - 1;
                    if (y >= GameDeskSize)
                        y = 0;
                    if (y + direction[1] < 0)
                        y = GameDeskSize - 1;

                    if(new Random().nextBoolean() == true)
                        gamefield[x][y].setState(1);
                    else
                        gamefield[x][y].setState(0);
                }
            }
        }
    }

}

class Point{
    int state;

    //1 - Eatable, 0 - Empty, 2 - PacMan, 3 - Wall, 4 - Gost
    public Point(int state){
        this.state = state;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return this.state;
    }
}