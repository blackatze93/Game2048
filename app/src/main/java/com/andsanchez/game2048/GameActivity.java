package com.andsanchez.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameActivity extends Activity implements View.OnClickListener {
    private Button btnNew;

    private int gridSize;

    private GridLayout grid;
    private TextView[][] cell;
    private TextView score;
    private TextView max;

    private Tile[] myTiles;
    boolean myWin = false;
    boolean myLose = false;
    int myScore = 0;
    int myMax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridSize = 4;

        cell = new TextView[gridSize][gridSize];

        btnNew = findViewById(R.id.button_new);

        btnNew.setOnClickListener(this);

        grid = findViewById(R.id.grid);
        score = findViewById(R.id.score);
        max = findViewById(R.id.max);

        grid.setRowCount(gridSize);
        grid.setColumnCount(gridSize);
        grid.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    up();
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }

                draw();
            }

            public void onSwipeRight() {
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    right();
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }

                draw();
            }

            public void onSwipeLeft() {
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    left();
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }

                draw();
            }

            public void onSwipeBottom() {
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    down();
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }

                draw();
            }

        });

        int size = (int) getResources().getDimension(R.dimen.side_size);

        for (int y = 0; y < gridSize; y++){
            for (int x = 0; x < gridSize; x++) {
                cell[x][y] = new TextView(this);
                cell[x][y].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
                cell[x][y].setGravity(Gravity.CENTER);
                cell[x][y].setLayoutParams(new ViewGroup.LayoutParams(size, size));
                grid.addView(cell[x][y]);
            }
        }

        resetGame();
        draw();
    }

    private void draw(){
        if (myLose) {
            Toast.makeText(this, "Has perdido", Toast.LENGTH_SHORT).show();
        } else if (myWin) {
            Toast.makeText(this, "Has ganado", Toast.LENGTH_SHORT).show();
        }

        for (int y = 0; y < gridSize; y++){
            for (int x = 0; x < gridSize; x++) {
                if (!tileAt(x, y).isEmpty()) {
                    cell[x][y].setText(String.valueOf(tileAt(x, y).value));
                    if (tileAt(x, y).value <= 2048) {
                        String bg = "bg_" + tileAt(x, y).value;
                        int drawable = getResources().getIdentifier(bg, "drawable", getApplicationContext().getPackageName());
                        cell[x][y].setBackgroundResource(drawable);
                    } else {
                        cell[x][y].setBackgroundResource(R.drawable.bg_major);
                    }
                } else {
                    cell[x][y].setText("");
                    cell[x][y].setBackgroundResource(R.drawable.bg_default);
                }
            }
        }

        score.setText(String.valueOf(myScore));
        max.setText(String.valueOf(myMax));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_new) {
            resetGame();
            draw();
        }
    }

    private int generateNumber() {
        int numbers[] = {2, 4, 8};
        int position = (int) (Math.random() * 3);
        return numbers[position];
    }

    public void resetGame() {
        myScore = 0;
        myMax = 0;
        myWin = false;
        myLose = false;
        myTiles = new Tile[gridSize * gridSize];
        for (int i = 0; i < myTiles.length; i++) {
            myTiles[i] = new Tile();
        }
        addTile();
        addTile();
    }

    public void left() {
        boolean needAddTile = false;
        for (int i = 0; i < gridSize; i++) {
            Tile[] line = getLine(i);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            if (!needAddTile && !compare(line, merged)) {
                needAddTile = true;
            }
        }

        if (needAddTile) {
            addTile();
        }
    }

    public void right() {
        myTiles = rotate(180);
        left();
        myTiles = rotate(180);
    }

    public void up() {
        myTiles = rotate(270);
        left();
        myTiles = rotate(90);
    }

    public void down() {
        myTiles = rotate(90);
        left();
        myTiles = rotate(270);
    }

    private Tile tileAt(int x, int y) {
        return myTiles[x + y * gridSize];
    }

    private void addTile() {
        List<Tile> list = availableSpace();
        if (!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.value = generateNumber();
        }
    }

    private List<Tile> availableSpace() {
        final List<Tile> list = new ArrayList<Tile>(gridSize * gridSize);
        for (Tile t : myTiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        return list;
    }

    private boolean isFull() {
        return availableSpace().size() == 0;
    }

    boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Tile t = tileAt(x, y);
                if ((x < (gridSize - 1) && t.value == tileAt(x + 1, y).value)
                        || ((y < (gridSize - 1)) && t.value == tileAt(x, y + 1).value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2) {
            return true;
        } else if (line1.length != line2.length) {
            return false;
        }

        for (int i = 0; i < line1.length; i++) {
            if (line1[i].value != line2[i].value) {
                return false;
            }
        }
        return true;
    }

    private Tile[] rotate(int angle) {
        Tile[] newTiles = new Tile[gridSize * gridSize];
        int offsetX = (gridSize - 1), offsetY = (gridSize - 1);
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * gridSize] = tileAt(x, y);
            }
        }
        return newTiles;
    }

    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<Tile>();
        for (int i = 0; i < gridSize; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[gridSize];
            ensureSize(l, gridSize);
            for (int i = 0; i < gridSize; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < gridSize && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].value;
            if (i < (gridSize - 1) && oldLine[i].value == oldLine[i + 1].value) {
                num *= 2;
                myScore += num;
                int ourTarget = 2048;
                if (num == ourTarget) {
                    myWin = true;
                }
                if (num > myMax) {
                    myMax = num;
                }
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, gridSize);
            return list.toArray(new Tile[gridSize]);
        }
    }

    private static void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }

    private Tile[] getLine(int index) {
        Tile[] result = new Tile[gridSize];
        for (int i = 0; i < gridSize; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    private void setLine(int index, Tile[] re) {
        System.arraycopy(re, 0, myTiles, index * gridSize, gridSize);
    }
}
