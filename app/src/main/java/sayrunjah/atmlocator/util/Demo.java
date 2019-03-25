package sayrunjah.atmlocator.util;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sayrunjah.atmlocator.R;
import sayrunjah.atmlocator.classes.AtmClass;

public class Demo extends AppCompatActivity {


    Button test;
    TextView textView;
    String sresponse;
    Connection connection;

    //Listss
    ArrayList<AtmClass> atmClassArrayList;
    ArrayList<AtmClass> distTime;
    ArrayList<AtmClass> finalDistTime;

    ///
    AtmClass atmListHolder;
    String place_name="",vicinity="",lat= "",lng="",distance="",duration="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        textView = (TextView)findViewById(R.id.textView);
        test = (Button)findViewById(R.id.test);
        connection = new Connection();
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getList(){
        atmClassArrayList = new ArrayList<>();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + "0.366758" + "," +  "32.568913"
                + "&radius=" + "5000"
                + "&types=" + "atm"
                +"&sensor=" + true
                + "&key="+ Config.googleKey;
        final HashMap<String,String> map = new HashMap<String, String>();
        connection.makeRequest(url,map, new Connection.GetResponse() {
            @Override
            public void response(final String response) {
                sresponse = response;
                HashMap<String,String> map = new HashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray myPlaceList = jsonObject.getJSONArray("results");
                    for (int i = 0; i < myPlaceList.length(); i++) {
                        JSONObject place = myPlaceList.getJSONObject(i);
                        place_name = place.getString("name");
                        vicinity = place.getString("vicinity");
                        lat = place.getJSONObject("geometry").getJSONObject("location").getString("lat");
                        lng = place.getJSONObject("geometry").getJSONObject("location").getString("lng");
                        String dis = "https://maps.googleapis.com/maps/api/distancematrix/json?unimperialrial"
                                + "&origins=" + "0.366758" + "," + "32.568913"
                                + "&destinations=" + lat + "," + lng
                                + "&key=" + Config.googleKey;
                        connection.makeRequest(dis, map, new Connection.GetResponse() {
                            @Override
                            public void response(String response) {
                                JSONObject jsonRespRouteDistance = null;
                                try {
                                    jsonRespRouteDistance = new JSONObject(response)
                                            .getJSONArray("rows")
                                            .getJSONObject(0)
                                            .getJSONArray("elements")
                                            .getJSONObject(0);
                                    distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
                                    duration = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        atmClassArrayList.add(new AtmClass(place_name,vicinity,distance,duration,lat,lng));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String message = "";
                        for (int i=0;i < atmClassArrayList.size();i++){
                            message = message +" "+ atmClassArrayList.get(i).getDistance()+ " \n";
                        }
                        textView.setText(message);
                    }
                });

            }
        });
    }

    public ArrayList<AtmClass> parseJsonPlaces(String results){
        String place_name="",vicinity="",lat= "",lng="";
        final String[] distance = {""};
        final String[] duration = { "" };
        ArrayList<AtmClass> arrayList = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(results);
            JSONArray myPlaceList = jsonObject.getJSONArray("results");
            for (int i = 0; i < myPlaceList.length(); i++) {
                JSONObject place = myPlaceList.getJSONObject(i);
                place_name = place.getString("name");
                vicinity = place.getString("vicinity");
                lat = place.getJSONObject("geometry").getJSONObject("location").getString("lat");
                lng = place.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String dis = "https://maps.googleapis.com/maps/api/distancematrix/json?unimperialrial"
                        + "&origins=" + "0.366758" + "," + "32.568913"
                        + "&destinations=" + lat + "," + lng
                        + "&key=" + Config.googleKey;
                connection.makeRequest(dis, map, new Connection.GetResponse() {
                    @Override
                    public void response(String response) {
                        JSONObject jsonRespRouteDistance = null;
                        try {
                            jsonRespRouteDistance = new JSONObject(response)
                                    .getJSONArray("rows")
                                    .getJSONObject(0)
                                    .getJSONArray("elements")
                                    .getJSONObject(0);
                            distance[0] = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
                            duration[0] = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                arrayList.add(new AtmClass(place_name,vicinity,distance[0],duration[0],lat,lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }


    public AtmClass parseJsonDistance(String jsonDistance){
        ArrayList<AtmClass> subList = new ArrayList<>();
        String distance = "",duration= "";
        JSONObject jsonRespRouteDistance = null;
        try {
            jsonRespRouteDistance = new JSONObject(jsonDistance)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0);
            distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
            duration = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return new AtmClass(distance,duration);
    }

   /* public void getPlacesandDistances(){
        String message = "";
        for (int i=0;i < atmClassArrayList.size();i++){
            message = message + atmClassArrayList.get(i).getDistance()+distTime.get(i).get(i).getDistance();
        }
        responseDialog(message);
    }*/

    private void responseDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder
                .setMessage(message)
                .setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .show();
    }
}
