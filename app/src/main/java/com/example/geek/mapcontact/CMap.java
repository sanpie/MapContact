package com.example.geek.mapcontact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CMap extends Fragment {
    static final LatLng iiitm = new LatLng(26.250438, 78.173469);
    LatLng p1 = null;
    LatLng latLng;
    private GoogleMap googleMap;
    MapView mapview;
    MarkerOptions markerOptions;
    View view;
    private ProgressDialog pDialog;
    private static String url = "http://private-b08d8d-nikitest.apiary-mock.com/contacts";
   String data1=""; String str="";
    public ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_cmap, container, false);
        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())
        {
            try {

                initilizeMap();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "please on internet", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);



    }


    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
          //  googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(iiitm).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            new GetProfilee().execute();
        }
    }

    private class GetProfilee extends AsyncTask<Void, Void, Void> {           // background data fetching

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance


            try {

                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                str = sh.makeServiceCall(url, ServiceHandler.GET);
                Log.d("Response: ", "> " + str);

                if (str != null) {

                    JSONArray ja1=new JSONArray(str);
                    JSONObject j0=ja1.getJSONObject(0);
                    JSONArray ja=j0.getJSONArray("contacts");
                    for(int i=0;i<ja.length();i++)
                    {
                        JSONObject jo=ja.getJSONObject(i);
                        String name=jo.getString("name");
                        String phone=jo.getString("phone");
                        String latitude=jo.getString("latitude");
                        String longitude=jo.getString("longitude");


                        HashMap<String, String> contact = new HashMap<String, String>();
                        contact.put("name", name);
                        contact.put("phone", phone);
                        contact.put("latitude", latitude);
                        contact.put("longitude", longitude);
                        contactList.add(contact);



                    }

                } else {
                    Log.d("Response: ", "> " + "no data");
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            for(int i=0;i<contactList.size();i++)
            {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(contactList.get(i).get("latitude") ), Double.parseDouble(contactList.get(i).get("longitude")))).title(contactList.get(i).get("name")+"   phone : "+contactList.get(i).get("phone")));
            }
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(contactList.get(0).get("latitude") ), Double.parseDouble(contactList.get(0).get("longitude")))).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }

    }



    @Override
    public void onPause(){
        super.onPause();
        if(googleMap != null) {
            googleMap = null;
            {
            }
        }
    }
}