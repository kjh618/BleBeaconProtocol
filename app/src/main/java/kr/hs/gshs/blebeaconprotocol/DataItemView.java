package kr.hs.gshs.blebeaconprotocol;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kjh on 2017-10-05.
 */

public class DataItemView extends LinearLayout{
    TextView textViewType;
    TextView textViewLength;
    TextView textViewData;

    public DataItemView(Context context) {
        super(context);

        init(context);
    }

    public DataItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.data_item, this, true);

        textViewType = (TextView) findViewById(R.id.textView_type);
        textViewLength = (TextView) findViewById(R.id.textView_length);
        textViewData = (TextView) findViewById(R.id.textView_data);
    }

    public void setType(String type) {
        textViewType.setText(type);
    }

    public void setData(String data) {
        textViewData.setText(data);
        textViewLength.setText(String.valueOf(data.length() + 1));
    }
}
