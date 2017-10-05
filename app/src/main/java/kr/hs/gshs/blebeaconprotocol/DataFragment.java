package kr.hs.gshs.blebeaconprotocol;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kjh on 2017-10-05.
 */

public class DataFragment extends DialogFragment {
    String[] packetTypes = {"Advertisement", "Coupon", "Information", "Others"};
    String[] types = {"Text", "URL", "Others"};
    String selectedPacketType, selectedType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_data, null);
        builder.setView(view);

        Spinner spinnerPacketType = (Spinner) view.findViewById(R.id.spinner_packettype);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, packetTypes);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPacketType.setAdapter(arrayAdapter1);

        spinnerPacketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPacketType = packetTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ListView listViewData = (ListView) view.findViewById(R.id.listView_data);
        final DataAdapter dataAdapter = new DataAdapter();

        dataAdapter.addItem(new DataItem(types[0], "Yay!"));
        dataAdapter.addItem(new DataItem(types[1], "https://www.naver.com/"));

        listViewData.setAdapter(dataAdapter);

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DataItem item = (DataItem) dataAdapter.getItem(position);
                Toast.makeText(getActivity().getApplicationContext(), item.getType() + " " + item.getData(), Toast.LENGTH_LONG).show();
            }
        });

        Spinner spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, types);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(arrayAdapter2);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedType = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button buttonAdd = (Button) view.findViewById(R.id.button_add);
        final EditText editTextData = (EditText) view.findViewById(R.id.editText_data);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataAdapter.addItem(new DataItem(selectedType, editTextData.getText().toString()));
                dataAdapter.notifyDataSetChanged();
            }
        });

        final Button buttonSave = (Button) view.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("packet_type", selectedPacketType);

                DataItem[] temp = new DataItem[dataAdapter.getCount()];
                for(int i=0; i<dataAdapter.getCount(); ++i)
                    temp[i] = (DataItem) dataAdapter.getItem(i);

                data.putExtra("data_items", temp);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                dismiss();
            }
        });

        return builder.create();
    }

    class DataAdapter extends BaseAdapter {
        ArrayList<DataItem> items = new ArrayList<DataItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(DataItem item) {
            items.add(item);
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            DataItemView view = new DataItemView(getActivity().getApplicationContext());
            DataItem item = items.get(position);
            view.setType(item.getType());
            view.setData(item.getData());

            return view;
        }
    }
}
