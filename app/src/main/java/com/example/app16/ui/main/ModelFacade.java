package com.example.app16.ui.main;

import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelFacade
  implements InternetCallback
{ 
  FileAccessor fileSystem;
  Context myContext;
  static ModelFacade instance = null; 

  public static ModelFacade getInstance(Context context)
  { if (instance == null) 
    { instance = new ModelFacade(context); }
    return instance;
  }


  private ModelFacade(Context context)
  { myContext = context; 
    fileSystem = new FileAccessor(context); 
  }

  public void internetAccessCompleted(String response)
  { 
    DailyQuote_DAO.makeFromCSV(response, myContext);

  }

  public String findQuote(String date, String date2)
  { 
    String result = "";
    long t1 = 0;
    t1 = DateComponent.getEpochSeconds(date);
    long t2 = 0;
    t2 = DateComponent.getEpochSeconds(date2);
    if ((t2 - t1) > 63113852)
    {
      result = "Please enter dates a maximum of 2 years apart.";
      return result;
    }
    String url = "";
    ArrayList<String> sq1 = null;
    sq1 = Ocl.copySequence(Ocl.initialiseSequence("period1","period2","interval","events"));
    ArrayList<String> sq2 = null;
    sq2 = Ocl.copySequence(Ocl.initialiseSequence((t1 + ""),(t2 + ""),"1d","history"));
    url = DailyQuote_DAO.getURL("GBPUSD=X", sq1, sq2);
    InternetAccessor x = null;
    x = new InternetAccessor();
    x.setDelegate(this);
    x.execute(url);
    JSONArray allData = DailyQuote_DAO.loadData(myContext);
    result = allData.toString();

    return result;
  }

  public GraphDisplay analyse()
  { 
    GraphDisplay result = null;
    result = new GraphDisplay();
    ArrayList<DailyQuote> quotes = null;
    quotes = Ocl.copySequence(DailyQuote.DailyQuote_allInstances);
    ArrayList<String> xnames = null;
    xnames = Ocl.copySequence(Ocl.collectSequence(quotes,(q)->{return q.date;}));
    ArrayList<Double> yvalues = null;
    yvalues = Ocl.copySequence(Ocl.collectSequence(quotes,(q)->{return q.close;}));
    result.setXNominal(xnames);
    result.setYPoints(yvalues);

    return result;
  }

}
