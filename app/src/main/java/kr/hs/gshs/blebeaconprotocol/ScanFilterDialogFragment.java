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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import static kr.hs.gshs.blebeaconprotocol.Constants.toPacketType;

/**
 * Created by kjh on 2017-10-05.
 */

public class ScanFilterDialogFragment extends DialogFragment {

    String selectedPacketType, selectedType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_scan_filter, null);
        builder.setView(view);

        //
        ListView listViewFilters = (ListView) view.findViewById(R.id.listView_filters);
        final FilterAdapter filterAdapter = new FilterAdapter(getActivity().getApplicationContext(), LayoutInflater.from(getActivity()));

        listViewFilters.setAdapter(filterAdapter);

        listViewFilters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Item Removed: " + filterAdapter.getItem(position), Toast.LENGTH_LONG).show();

                filterAdapter.removeItem(position);
                filterAdapter.notifyDataSetChanged();
            }
        });

        //
        Spinner spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, toPacketType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(arrayAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPacketType = toPacketType[position];

                Toast.makeText(getActivity().getApplicationContext(), "Filter Selected: " + selectedPacketType, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Data editor and add button
        Button buttonAdd2 = (Button) view.findViewById(R.id.button_add2);

        buttonAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterAdapter.addItem(selectedPacketType);
                filterAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity().getApplicationContext(), "Filter Added: " + selectedPacketType, Toast.LENGTH_LONG).show();
            }
        });

        // Save button
        final Button buttonSave2 = (Button) view.findViewById(R.id.button_save2);

        buttonSave2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();

                data.putStringArrayListExtra("filter", filterAdapter.items);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                dismiss();
            }
        });

        return builder.create();
    }
}