package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getJSON();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  void getJSON() throws FileNotFoundException {
        // Variables
        String json; // json string from file
        ArrayList<Float> yPrices1 = new ArrayList<>(); // prices 1
        ArrayList<String> xDates1 = new ArrayList<>(); // dates 1
        ArrayList<Float> yPrices2 = new ArrayList<>(); // prices 2
        ArrayList<String> xDates2 = new ArrayList<>(); // dates 2
        ArrayList<String> stockNames = new ArrayList<>(); // stock names
        ArrayList<Entry> yValues1 = new ArrayList<>(); // values on the y axis
        ArrayList<Entry> yValues2 = new ArrayList<>(); // values on the y axis
        float y; // temp var for price
        String z; // temp var for date time
        int numberOfStocks = 2;

        // SET UP THE CHART
        mChart = findViewById(R.id.lineChart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.getDescription().setEnabled(false);

        try {
            // read the file
            InputStream is = getAssets().open("StocksData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // assign the json string and create an array
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            // get object from JSON
            JSONObject obj1 = jsonArray.getJSONObject(0);
            // add name to array
            String currentName = obj1.getString("name");
            stockNames.add(currentName);
            // get prices and dates
            JSONArray pricesJSON1 = obj1.getJSONArray("prices");
            JSONArray datesJSON1 = obj1.getJSONArray("dateTime");
            // iterate the prices
            for (int a = 0; a < pricesJSON1.length(); a++) {
                y = Float.parseFloat(pricesJSON1.getString(a));
                yPrices1.add(y);
            }
            // iterae the dates
            for (int b = 0; b < datesJSON1.length(); b++) {
                z = datesJSON1.getString(b);
                xDates1.add(z);
            }

            // get object from JSON 2
            JSONObject obj2 = jsonArray.getJSONObject(1);
            // add name to array
            currentName = obj2.getString("name");
            stockNames.add(currentName);
            // get prices and dates
            JSONArray pricesJSON2 = obj2.getJSONArray("prices");
            JSONArray datesJSON2 = obj2.getJSONArray("dateTime");
            // iterate the prices
            for (int a = 0; a < pricesJSON2.length(); a++) {
                y = Float.parseFloat(pricesJSON2.getString(a));
                yPrices2.add(y);
            }
            // iterae the dates
            for (int b = 0; b < datesJSON2.length(); b++) {
                z = datesJSON2.getString(b);
                xDates2.add(z);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // assign stock prices to y AXIS
        for (int i = 0; i < yPrices1.size(); i++) {
            float inX = i;
            yValues1.add(new Entry(inX, yPrices1.get(i)));
        }

        for (int i = 0; i < yPrices2.size(); i++) {
            float inX = i;
            yValues2.add(new Entry(inX, yPrices2.get(i)));
        }

        // add the date values from the array
        String[] datesValue = new String[xDates1.size()];
        for (int i = 0; i < xDates1.size(); i++) {
            datesValue[i] = xDates1.get(i);
        }
        // Set the format
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyAxisValueFormatter(datesValue));
        xAxis.setGranularity(1);

        // SET 1 name
        LineDataSet set1 = new LineDataSet(yValues1, stockNames.get(0));
        set1.setFillAlpha(110); // fill
        set1.setColor(Color.GREEN);
        set1.setLineWidth(2f);
        set1.setValueTextSize(12f);
        // SET 2 name
        LineDataSet set2 = new LineDataSet(yValues2, stockNames.get(1));
        set2.setFillAlpha(110); // fill
        set2.setColor(Color.BLUE);
        set2.setLineWidth(2f);
        set2.setValueTextSize(12f);
        // Create a datasets
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        // add a dataset to the array
        dataSets.add(set1);
        dataSets.add(set2);
        // line draw to be put on the chart
        LineData data = new LineData(dataSets);
        // add the line graph to the chart
        mChart.setData(data);
    }

    public class MyAxisValueFormatter extends ValueFormatter {
        private String[] mValues;
        public MyAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValues[(int)value];
        }
    }
}