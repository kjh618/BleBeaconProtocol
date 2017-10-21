package kr.hs.gshs.blebeaconprotocol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * DataItemAdapter
 * Created by kjh on 2017-10-06.
 */

public class DataItemAdapter extends BaseAdapter{
    private ArrayList<DataItem> items;
    private Context mContext;
    private LayoutInflater mInflater;

    DataItemAdapter(Context context, LayoutInflater inflater) {
        super();
        mContext = context;
        mInflater = inflater;
        items = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(DataItem item) {
        items.add(item);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.data_item, null);

        TextView textViewType = (TextView) view.findViewById(R.id.textView_type);
        TextView textViewLength = (TextView) view.findViewById(R.id.textView_length);
        TextView textViewData = (TextView) view.findViewById(R.id.textView_data);
        DataItem item = items.get(position);

        textViewType.setText(item.getType());
        textViewData.setText(item.getData());
        textViewLength.setText(String.valueOf(item.getLength()));

        return view;
    }
}
