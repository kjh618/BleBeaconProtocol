package kr.hs.gshs.blebeaconprotocol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * FilterAdapter
 * Created by kjh on 2017-10-08.
 */

public class FilterAdapter extends BaseAdapter {
    ArrayList<String> items;
    private Context mContext;
    private LayoutInflater mInflater;

    FilterAdapter(Context context, LayoutInflater inflater) {
        super();
        mContext = context;
        mInflater = inflater;
        items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.filter_item, null);

        TextView textViewFilter = (TextView) view.findViewById(R.id.textView_filter);

        textViewFilter.setText(items.get(position));

        return view;
    }
}
