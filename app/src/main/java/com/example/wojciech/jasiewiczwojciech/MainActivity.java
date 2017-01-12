package com.example.wojciech.jasiewiczwojciech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import java.lang.String;
import java.lang.Integer;

public class MainActivity extends AppCompatActivity {

    EditText[] et = new EditText[4];
    MultiAutoCompleteTextView mtv;
    String[] colorNames = new String[]{
            "black",
            "blue",
            "gray",
            "red",
            "green",
            "yellow",
            "pink"
    };
    int[] cornersValues;
    int[] colorTable;
    int[] theMostPopularColorInfo;
    int[] theMostPopularDigitInfo;

    Button closeButton;
    Button logoutButton;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent  = getIntent();
        colorTable = intent.getIntArrayExtra("colorsTable");
        theMostPopularColorInfo = intent.getIntArrayExtra("color");
        theMostPopularDigitInfo = intent.getIntArrayExtra("digit");
        cornersValues = intent.getIntArrayExtra("corners");

        TextView welcomeTextView = (TextView)findViewById(R.id.welcome);
        assert welcomeTextView != null;
        welcomeTextView.setText("Welcome " + getEmail());

        //Generating color list
        TableLayout table2 = (TableLayout) findViewById(R.id.table2);
        generateColorList(table2);

        //Generating mulit auto complete text
        mtv = (MultiAutoCompleteTextView) findViewById(R.id.mtv);
        mtv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> adp = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, colorNames);
        mtv.setThreshold(1);
        mtv.setAdapter(adp);

        //getting editText objects
        generateEditTexts();

        //closeButton code
        closeButton = (Button)findViewById(R.id.button4);
        final OnClickListener closeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        };
        closeButton.setOnClickListener(closeListener);

        //check answer button code
        Button button = (Button) findViewById(R.id.button2);
        OnClickListener onClick1 = new OnClickListener() {
            public void onClick(View v) {
                closeButton.setVisibility(View.VISIBLE);
                int points = countGoodAnswers();
                TextView tv5 = (TextView)findViewById(R.id.tv5);
                assert tv5 != null;
                tv5.setText("You have " + points + "/5 points\n");
            }
        };
        assert button != null;
        button.setOnClickListener(onClick1);

        logoutButton = (Button) findViewById(R.id.logoutButton);

        OnClickListener logoutListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePreferences();
                Intent backToLoginActivity = new Intent(MainActivity.this, LoginActivity.class );
                copyDataToIntent(backToLoginActivity);
                startActivity(backToLoginActivity);
                finish();
            }
        };

        logoutButton.setOnClickListener(logoutListener);
}
    //Go to home screen action
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void generateEditTexts(){
        int[] etIds = new int[]{R.id.et1, R.id.et2, R.id.et3, R.id.et4 };
        for(int i = 0; i < 4; i++){
            et[i] = (EditText)findViewById(etIds[i]);
            et[i].setTextSize(15);
        }
    }

    public void generateColorList(TableLayout table){

        for (int j = 0; j < 7; j++) {
            TableRow row = new TableRow(this);
            TextView colorCell = new TextView(this);
            colorCell.setBackgroundColor(colorTable[j]);
            colorCell.setText("\t\t\t");
            row.addView(colorCell);
            TextView nameCell = new TextView(this);
            nameCell.setText("\t" + colorNames[j] + "\t");
            row.addView(nameCell);
            assert table != null;
            table.addView(row);
        }
    }
    public int countGoodAnswers(){

        int points = 0;
        int index = theMostPopularColorInfo[0];
        String answer0 = mtv.getText().toString();
        String correct0 = colorNames[index];

        if((answer0.compareTo(correct0 + ",") == 1) || (answer0.compareTo(correct0) == 1)){
            points++;
            mtv.setTextColor(Color.BLACK);
        }else mtv.setTextColor(Color.RED);

        int[] correct = new int[4];

        correct[0] = theMostPopularColorInfo[1];
        correct[1] = theMostPopularDigitInfo[0];
        correct[2] = cornersValues[0];
        correct[3] = cornersValues[0] + cornersValues[1] + cornersValues[2] + cornersValues[3];

        for (int i = 0; i < 4; i++){
            try{
                int answer = Integer.parseInt(et[i].getText().toString());
                if(answer == correct[i]){
                    points++;
                    et[i].setTextColor(Color.BLACK);
                }else{
                    et[i].setTextColor(Color.RED);
                }
            }catch(NumberFormatException e){
                Log.i("INFO:","empty field " + i);
            }
        }

        return points;
    }
    public void copyDataToIntent(Intent intent){
        intent.putExtra("colorsTable", colorTable);
        intent.putExtra("color", theMostPopularColorInfo);
        intent.putExtra("digit", theMostPopularDigitInfo);
        intent.putExtra("corners", cornersValues);
    }
    public void updatePreferences(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("Logged", false);
        editor.putString("email", "");
        editor.commit();
    }

    public String getEmail(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("email","");
    }

}
