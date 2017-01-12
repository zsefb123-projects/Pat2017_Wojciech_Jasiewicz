package com.example.wojciech.jasiewiczwojciech;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.SharedPreferences;

import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 5000;//timeout
    final int SQUARE_SIZE = 7;
    public static final String PREFS_NAME = "MyPrefsFile";
    final int[] colorTable = new int[]{
            Color.BLACK,
            Color.BLUE,
            Color.GRAY,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.MAGENTA,
    };
    final int[] cornersValues = new int[4];
    int[] theMostPopularColorInfo;
    int[] theMostPopularDigitInfo;

    public Handler transferHandler;
    public Runnable transferRunnable;
    public Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);

        LinearLayout layout = (LinearLayout) findViewById(R.id.Linear2);
        assert layout != null;
        layout.setPadding(70,70,0,0);

        //Generate pseudo immage on splash screen
        TableLayout table = new TableLayout(this);
        generateImage(table);
        layout.addView(table);

        // add smile image
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.smile2);
        layout.addView(image);

        //transfer Handler
        transferHandler = new Handler();
        transferRunnable = new Runnable() {
            @Override
            public void run() {

                // Restore preferences
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean logged = settings.getBoolean("Logged", false);

                // Choose between login and main activity
                Intent i;
                if(logged)i = new Intent(SplashScreenActivity.this, MainActivity.class);
                else i = new Intent(SplashScreenActivity.this, LoginActivity.class);

                //transfer data to Main Activity, start MainActivity and close splash screen
                i.putExtra("colorsTable", colorTable);
                i.putExtra("corners", cornersValues);
                i.putExtra("color", theMostPopularColorInfo);
                i.putExtra("digit", theMostPopularDigitInfo);
                startActivity(i);
                finish();
            }
        };
        transferHandler.postDelayed(transferRunnable, SPLASH_TIME_OUT);
        closeButton = (Button)findViewById(R.id.closeButton);
        closeButton.setVisibility(View.INVISIBLE);
        OnClickListener closeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        };
        closeButton.setOnClickListener(closeListener);

    }

    //Stop application on splash screen
    @Override
    public void onBackPressed() {
        transferHandler.removeCallbacks(transferRunnable);
        closeButton.setVisibility(View.VISIBLE);
    }

    public void generateImage(TableLayout table){

        final int[] colorsQuantity = new int[7];
        final int[] digitsQuantity = new int[7];
        Random randomObject = new Random();

        for (int i = 0; i < SQUARE_SIZE; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < SQUARE_SIZE; j++) {

                //count digits and colors quantity
                int colorIndex = randomObject.nextInt(7);
                colorsQuantity[colorIndex]++;
                int digitIndex = randomObject.nextInt(7);
                digitsQuantity[digitIndex]++;

                //corners values
                if((i == 0) && (j == 0)) cornersValues[0] = digitIndex;
                if((i == 0) && (j == 6)) cornersValues[1] = digitIndex;
                if((i == 6) && (j == 0)) cornersValues[2] = digitIndex;
                if((i == 6) && (j == 6)) cornersValues[3] = digitIndex;

                //generate one cell
                TextView cell = generateOneCell(digitIndex, colorTable[colorIndex]);
                row.addView(cell);
            }
            table.addView(row);
        }
        theMostPopularColorInfo = findTheMostPopular(colorsQuantity);
        theMostPopularDigitInfo = findTheMostPopular(digitsQuantity);
    }

    public int[] findTheMostPopular(int[] Array){
        int quantity = Array[0];
        int valueIndex = 0;
        for(int i = 0; i < Array.length; i++){
            if(quantity < Array[i] ){
                quantity = Array[i];
                valueIndex = i;
            }
        }

        return new int[]{valueIndex, quantity};
    }

    public TextView generateOneCell(int digit, int color){
        TextView cell = new TextView(this);
        cell.setText("" + digit);
        cell.setHeight(50);
        cell.setHeight(50);
        cell.setBackgroundColor(color);
        cell.setTextColor(Color.WHITE);
        cell.setPadding(20, 10, 20, 10);
        return cell;
    }
}
