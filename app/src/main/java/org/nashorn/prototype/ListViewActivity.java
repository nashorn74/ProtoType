package org.nashorn.prototype;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    ListView listView = null;
    class Item {
        int image;
        String title;
        String text;
        Item(int image, String title, String text) {
            this.image = image;
            this.title = title;
            this.text = text;
        }
    }
    ArrayList<Item> itemList = new ArrayList<Item>();
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
            TextView text1View = (TextView)convertView.findViewById(R.id.title);
            TextView text2View = (TextView)convertView.findViewById(R.id.text);
            Item item = itemList.get(position);
            imageView.setImageResource(item.image);
            text1View.setText(item.title);
            text2View.setText(item.text);
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }
    ItemAdapter itemAdpater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView)findViewById(R.id.listview);
        itemList.add(new Item(R.drawable.f150, "Title01", "Text0101"));
        itemList.add(new Item(R.drawable.f150, "Title02", "Text0102"));
        itemList.add(new Item(R.drawable.f150, "Title03", "Text0103"));
        itemList.add(new Item(R.drawable.f150, "Title04", "Text0104"));
        itemList.add(new Item(R.drawable.f150, "Title05", "Text0105"));
        itemAdpater = new ItemAdapter(ListViewActivity.this, R.layout.list_item,
                itemList);
        listView.setAdapter(itemAdpater);
    }
}
