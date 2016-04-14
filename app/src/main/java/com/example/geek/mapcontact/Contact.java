package com.example.geek.mapcontact;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Contact extends Fragment {
    private ProgressDialog pDialog;
    private static String url = "http://private-b08d8d-nikitest.apiary-mock.com/contacts";
    EditText e;String data1=""; String str="";
     public ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    ListView lv=null;
    View rootView=null;
     FloatingActionButton fab;
    RelativeLayout r;
    int ij=0;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_contact, container, false);

        ConnectivityManager cm =(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())
        {
            new GetProfile().execute();
        }
else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


            alertDialog.setTitle("Internet is OFF...");


            alertDialog.setMessage("Internet is OFF... Are you sure you want to ON it?");


            alertDialog.setIcon(R.mipmap.ic_launcher);


            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {


                    Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent);
                }
            });


            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    //    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                    getActivity().finish();
                }
            });


            alertDialog.show();
        }





        r=(RelativeLayout)rootView.findViewById(R.id.relative);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Thread(new Task()).start();

            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    /*    Bundle args = new Bundle();
        args.putString("cont","safasfasfa");
        CMap newFragment = new CMap ();
        newFragment.setArguments(args);
*/
    }

    class Task implements Runnable {
        @Override
        public void run() {
            for(int i=0;i<contactList.size();i++)
            {
                Snackbar snackbar = Snackbar
                        .make(r, ij+" Contact synced", Snackbar.LENGTH_SHORT);

                snackbar.show();
                addContact(contactList.get(i).get("name"),contactList.get(i).get("phone"));
                ij++;
            }
            Snackbar snackbar = Snackbar
                    .make(r, "All Contact synced", Snackbar.LENGTH_LONG);

            snackbar.show();
            ij=0;
            }
        }


        private void addContact(String name, String phone) {
         ContentValues values = new ContentValues();
         values.put(People.NUMBER, phone);
         values.put(People.TYPE, Phone.TYPE_CUSTOM);
         values.put(People.LABEL, name);
         values.put(People.NAME, name);
         Uri dataUri = getActivity().getContentResolver().insert(People.CONTENT_URI, values);
         Uri updateUri = Uri.withAppendedPath(dataUri, People.Phones.CONTENT_DIRECTORY);
         values.clear();
         values.put(People.Phones.TYPE, People.TYPE_MOBILE);
         values.put(People.NUMBER, phone);
         updateUri = getActivity().getContentResolver().insert(updateUri, values);
     }
    private class GetProfile extends AsyncTask<Void, Void, Void> {           // background data fetching

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
                        String email=jo.getString("email");
                        String phone=jo.getString("phone");
                        String officePhone=jo.getString("officePhone");
                        String latitude=jo.getString("latitude");
                        String longitude=jo.getString("longitude");


                        HashMap<String, String> contact = new HashMap<String, String>();
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("phone", phone);
                        contact.put("officePhone", officePhone);
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

            lv=(ListView) rootView.findViewById(R.id.onstagelist);
            lv.setAdapter(new CustomAdapter1((MainActivity) getActivity(), contactList));
            CMap.str=str;



        }

    }


}
