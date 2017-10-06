package kr.hs.gshs.blebeaconprotocol;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kjh on 2017-10-05.
 */

public class AdvertiseDataDialogFragment extends DialogFragment {
    // Packet types and data types
    String[] packetTypes = {"Advertisement", "Coupon", "Information", "Others"};
    String[] types = {"Text", "URL", "Others"};

    String selectedPacketType, selectedType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_advertise_data, null);
        builder.setView(view);

        // Packet type spinner
        Spinner spinnerPacketType = (Spinner) view.findViewById(R.id.spinner_packettype);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, packetTypes);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPacketType.setAdapter(arrayAdapter1);

        spinnerPacketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPacketType = packetTypes[position];

                Toast.makeText(getActivity().getApplicationContext(), "Packet Type Selected: " + selectedPacketType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Data items list
        ListView listViewData = (ListView) view.findViewById(R.id.listView_dataitems);
        final DataItemAdapter dataItemAdapter = new DataItemAdapter(getActivity().getApplicationContext(), LayoutInflater.from(getActivity()));

        listViewData.setAdapter(dataItemAdapter);

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DataItem item = (DataItem) dataItemAdapter.getItem(position);
                dataItemAdapter.removeItem(position);
                dataItemAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity().getApplicationContext(), "Item Removed: " + item.getType() + "/" + item.getData(), Toast.LENGTH_LONG).show();
            }
        });

        // Data type spinner
        Spinner spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, types);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(arrayAdapter2);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedType = types[position];

                Toast.makeText(getActivity().getApplicationContext(), "Data Type Selected: " + selectedType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Data editor and add button
        final EditText editTextData = (EditText) view.findViewById(R.id.editText_data);
        Button buttonAdd = (Button) view.findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredData = editTextData.getText().toString();
                dataItemAdapter.addItem(new DataItem(selectedType, enteredData));
                dataItemAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity().getApplicationContext(), "Item Added: " + selectedType + "/" + enteredData, Toast.LENGTH_LONG).show();
            }
        });

        // Save button
        final Button buttonSave = (Button) view.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("packet_type", selectedPacketType);

                ArrayList<DataItem> temp = new ArrayList<DataItem>(dataItemAdapter.getCount());
                for(int i = 0; i< dataItemAdapter.getCount(); ++i)
                    temp.add((DataItem) dataItemAdapter.getItem(i));

                data.putParcelableArrayListExtra("data_items", temp);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                dismiss();
            }
        });

        return builder.create();
    }
}
