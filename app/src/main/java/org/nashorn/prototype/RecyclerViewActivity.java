package org.nashorn.prototype;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Initialize a new String array
        String[] colors = {
                "Red","Green","Blue","Yellow","Magenta","Cyan","Orange",
                "Aqua","Azure","Beige","Bisque","Brown","Coral","Crimson",
                "Red","Green","Blue","Yellow","Magenta","Cyan","Orange",
                "Aqua","Azure","Beige","Bisque","Brown","Coral","Crimson",
                "Red","Green","Blue","Yellow","Magenta","Cyan","Orange",
                "Aqua","Azure","Beige","Bisque","Brown","Coral","Crimson"
        };

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        RecyclerView.Adapter adapter =
                new ColorAdapter(RecyclerViewActivity.this, colors);

        // Set the adapter for RecyclerView
        recyclerView.setAdapter(adapter);
    }
}

class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder>{
    private String[] mDataSet;
    private Context mContext;
    private Random mRandom = new Random();

    public ColorAdapter(Context context,String[] DataSet){
        mDataSet = DataSet;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(View v){
            super(v);
            Log.i("onCreateViewHolder","ViewHolder");
            Log.i("onCreateViewHolder",v.toString());
            mTextView = (TextView)v.findViewById(R.id.card_view_textview);
            Log.i("mTextView", mTextView+"");
        }
    }

    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        Log.i("onCreateViewHolder","onCreateViewHolder");
        LayoutInflater layoutInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,parent,false);
        View v = layoutInflater.inflate(R.layout.recyclerview_item,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Log.i("position",position+"");
        Log.i("holder",holder.toString());
        try {
            holder.mTextView.setText(mDataSet[position]);
            // Set a random height for TextView
            holder.mTextView.getLayoutParams().height = getRandomIntInRange(250, 75);
            // Set a random color for TextView background
            holder.mTextView.setBackgroundColor(getRandomHSVColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){
        return mDataSet.length;
    }

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min){
        return mRandom.nextInt((max-min)+min)+min;
    }

    // Custom method to generate random HSV color
    protected int getRandomHSVColor(){
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        // We make the color depth full
        float saturation = 1.0f;
        // We make a full bright color
        float value = 1.0f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }
}
