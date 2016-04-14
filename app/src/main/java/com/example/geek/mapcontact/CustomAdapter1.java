package com.example.geek.mapcontact;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class CustomAdapter1 extends BaseAdapter{
	ArrayList<HashMap<String, String>> result;
    Context context;
    ImageView imdb;int posi;
    ArrayList<Bitmap> imageId= new ArrayList<Bitmap>();

      private static LayoutInflater inflater=null;
    public CustomAdapter1(MainActivity mainActivity, ArrayList<HashMap<String, String>> prgmNameList) {
      // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,tv1,tv2,tv3;

    }
    Holder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder=new Holder();
        View rowView;
             rowView = inflater.inflate(R.layout.program_list, null);
             holder.tv=(TextView) rowView.findViewById(R.id.Name);
        holder.tv.setText(result.get(position).get("name"));
             holder.tv1=(TextView) rowView.findViewById(R.id.Phone);
        holder.tv1.setText(result.get(position).get("phone"));
        holder.tv2=(TextView) rowView.findViewById(R.id.Email);
        holder.tv2.setText(result.get(position).get("email"));
        holder.tv3=(TextView) rowView.findViewById(R.id.Officenumber);
        holder.tv3.setText(result.get(position).get("officePhone"));
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

        Intent i=new Intent(Intent.ACTION_CALL);
		Uri data1=Uri.parse("tel:"+result.get(position).get("phone"));
		i.setData(data1);
		context.startActivity(i);
            }
        });
      rowView.setOnLongClickListener(new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			
			return false;
		}
	});
        return rowView;
    }
    


}
