package com.example.wojciech.jasiewiczwojciech;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.Random;


public class splashScreen extends AppCompatActivity {


    final int SPLASH_TIME_OUT = 5000;//timeout
    boolean transferToMainActivity = true;
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        LinearLayout layout = (LinearLayout) findViewById(R.id.Linear2);

        //init data tables
        final int[] colorTable = new int[]{
                Color.BLACK,
                Color.BLUE,
                Color.GRAY,
                Color.RED,
                Color.GREEN,
                Color.YELLOW,
                Color.MAGENTA,
        };
        final int[] colorNumbers = new int[7];
        Random      r1 = new Random();
        final int   colorIndex = r1.nextInt(7);
        final int[] quantityNums = new int[7];
        final int[] corners = new int[4];

        layout.setPadding(70,70,0,0);
        //Generate pseudo immage on splash screen
        TableLayout table = new TableLayout(this);

        for (int i = 0; i < 7; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < 7; j++) {

                TextView cell = new TextView(this);

                Random r = new Random();
                int    a = r.nextInt(7);
                colorNumbers[a]++;

                int b = r.nextInt(7);
                quantityNums[b]++;

                //corners values
                if((i == 0) && (j == 0))
                    corners[0] = b;
                if((i == 0) && (j == 6))
                    corners[1] = b;
                if((i == 6) && (j == 0))
                    corners[2] = b;
                if((i == 6) && (i == 6))
                    corners[3] = b;


                quantityNums[b]++;

                cell.setText("" + b);
                cell.setHeight(50);
                cell.setHeight(50);
                cell.setBackgroundColor(colorTable[a]);
                cell.setTextColor(Color.WHITE);

                cell.setPadding(20, 10, 20, 10);
                row.addView(cell);
            }
            table.addView(row);
        }
        if (layout == null) throw new AssertionError();
        layout.addView(table);

        ImageView immage = new ImageView(this);
        immage.setImageResource(R.drawable.smile2);
        layout.addView(immage);

        //init close Button
        closeButton = (Button)findViewById(R.id.button3);
        final OnClickListener closeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        closeButton.setOnClickListener(closeListener);

        // init back button
        Button backButton = (Button)findViewById(R.id.button);
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                transferToMainActivity = false;
                closeButton.setVisibility(View.VISIBLE);
                closeButton.setText("Close app");
            }
        };
        assert backButton != null;
        backButton.setOnClickListener(listener);

        //transfer Handler
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //transfer data to Main Activity, start MainActivity and close splash screen
                if(transferToMainActivity){
                    Intent i = new Intent(splashScreen.this, MainActivity.class);
                    i.putExtra("numbers1",colorNumbers);
                    i.putExtra("colors", colorTable);
                    i.putExtra("index", colorIndex);
                    i.putExtra("quantity", quantityNums);
                    i.putExtra("corners", corners);
                    startActivity(i);

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
