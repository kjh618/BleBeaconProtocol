package kr.hs.gshs.blebeaconprotocol;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by kjh on 2017-10-05.
 */

public class DataFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_data, null);
        builder.setView(view);

        final Button buttonSave = (Button) view.findViewById(R.id.button_save);
        final EditText editTextRawData = (EditText) view.findViewById(R.id.editText_rawdata);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String rawData = editTextRawData.getText().toString();

                Intent data = new Intent();
                data.putExtra("raw_data", rawData);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);

                dismiss();
            }
        });

        return builder.create();
    }
}
