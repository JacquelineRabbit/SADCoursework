package com.example.app16;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws IOException, JSONException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.app16", appContext.getPackageName());

        InputStream is = appContext.getAssets().open("StocksData.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        String json = new String(buffer, StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(json);

        // get object from JSON
        JSONObject obj1 = jsonArray.getJSONObject(0);
        // add name to array
        String currentName = obj1.getString("name");
        ArrayList<Float> yPrices1 = new ArrayList<>(); // prices 1
        ArrayList<String> xDates1 = new ArrayList<>(); // dates 1
        ArrayList<String> stockNames = new ArrayList<>(); // stock names
        float y; // temp var for price
        String z; // temp var for date time
        int numberOfStocks = 2;
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

        ArrayList<Float> yPrices1EX = new ArrayList<>(); // prices 1
        ArrayList<String> xDates1EX = new ArrayList<>(); // dates 1
        ArrayList<String> stockNamesEX = new ArrayList<>(); // stock names

        yPrices1EX.add(10f);
        xDates1EX.add("2020-11-01");
        stockNamesEX.add("ww.prod");

        assertEquals(yPrices1EX.get(0), yPrices1.get(0));
        assertEquals(xDates1EX.get(0), xDates1.get(0));
        assertEquals(stockNamesEX.get(0), stockNames.get(0));
    }
}
