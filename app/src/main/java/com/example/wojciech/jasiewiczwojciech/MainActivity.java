package com.example.wojciech.jasiewiczwojciech;

import android.content.Intent;
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

    TextView[]                tv = new TextView[4];
    EditText[]                et = new EditText[4];
    MultiAutoCompleteTextView mtv;
    int                       index;
    int                       maxNumIndex;
    int                       maxColorIndex;
    String[]                  colorNames;
    int[]                     quantityNumbers;
    int[]                     numbersTable;
    int[]                     corners;
    int[]                     colorTable;
    Button                    closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent    = getIntent();
        colorTable       = intent.getIntArrayExtra("colors");
        quantityNumbers  = intent.getIntArrayExtra("quantity");
        corners          = intent.getIntArrayExtra("corners");
        numbersTable     = intent.getIntArrayExtra("numbers1");
        index            = intent.getIntExtra("index", 0);
        maxNumIndex      = findMaxIndex(quantityNumbers);
        maxColorIndex    = findMaxIndex(numbersTable);

        TableLayout table1 = (TableLayout) findViewById(R.id.table1);
        TableRow    row1 = new TableRow(this);
        TextView    t1 = new TextView(this);
        t1.setText(R.string.question1);
        t1.setTextSize(20);
        row1.addView(t1);
        assert table1 != null;
        table1.addView(row1);

        colorNames = new String[]{
                "black",
                "blue",
                "gray",
                "red",
                "green",
                "yellow",
                "pink"
        };
        //Generating color list
        TableLayout table2 = (TableLayout) findViewById(R.id.table2);
        for (int j = 0; j < 7; j++) {
            TableRow row = new TableRow(this);
            TextView colorCell = new TextView(this);
            colorCell.setBackgroundColor(colorTable[j]);
            colorCell.setText("\t\t\t");
            row.addView(colorCell);
            TextView nameCell = new TextView(this);
            nameCell.setText("\t" + colorNames[j] + "\t");
            row.addView(nameCell);
            assert table2 != null;
            table2.addView(row);
        }
        //generating mulit auto complete text
        mtv = (MultiAutoCompleteTextView) findViewById(R.id.mtv);
        mtv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, colorNames);
        mtv.setThreshold(1);
        mtv.setAdapter(adp);

        //getting editText and textViews objects
        final int[] etIds = new int[]{R.id.et1, R.id.et2, R.id.et3, R.id.et4 };
        int[] tvIds = new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4 };

        for(int i = 0; i < 4; i++){
            tv[i] = (TextView)findViewById(tvIds[i]);
            tv[i].setTextSize(20);
            et[i] = (EditText)findViewById(etIds[i]);
            et[i].setTextSize(15);
        }
        tv[0].setText(getString(R.string.question2) + colorNames[index] + getString(R.string.question21));
        tv[1].setText(R.string.question3);
        tv[2].setText(R.string.question4);
        tv[3].setText(R.string.question5);

        //closeButton code
        closeButton = (Button)findViewById(R.id.button4);
        final OnClickListener closeListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        closeButton.setOnClickListener(closeListener);

        //check answer button code
        Button button = (Button) findViewById(R.id.button2);
        OnClickListener onClick1 = new OnClickListener() {
            public void onClick(View v) {
                closeButton.setVisibility(View.VISIBLE);
                int    points = 0;
                String answer0 = mtv.getText().toString();
                String correct0 = colorNames[maxColorIndex];
                if((answer0.compareTo(correct0 + ",") == 1) || (answer0.compareTo(correct0) == 1)){
                    points++;
                    mtv.setTextColor(Color.BLACK);
                }else{
                    mtv.setTextColor(Color.RED);
                }

                int[] correct = new int[4];

                correct[0] = numbersTable[index];
                correct[1] = maxNumIndex;
                correct[2] = corners[0];
                correct[3] = corners[0] + corners[1] + corners[2] + corners[3];

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

                TextView tv5 = (TextView)findViewById(R.id.tv5);
                assert tv5 != null;
                tv5.setText("You have " + points + "/5 points\n");
            }
        };
        button.setOnClickListener((View.OnClickListener) onClick1);
}

    protected int findMaxIndex(int[] numbersTable){

        int maxValue = 0;
        int maxIndex = 0;
        for(int i = 0; i < 7; i++){
            if(numbersTable[i] > maxValue){
                maxValue = numbersTable[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

}
