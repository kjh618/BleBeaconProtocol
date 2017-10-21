/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.hs.gshs.blebeaconprotocol;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static kr.hs.gshs.blebeaconprotocol.Constants.toDataType;
import static kr.hs.gshs.blebeaconprotocol.Constants.toPacketType;

/**
 * Holds and displays {@link ScanResult}s, used by {@link ScannerFragment}.
 */
public class ScanResultAdapter extends BaseAdapter {

    private ArrayList<ScanResult> mArrayList;

    private Context mContext;

    private LayoutInflater mInflater;

    ScanResultAdapter(Context context, LayoutInflater inflater) {
        super();
        mContext = context;
        mInflater = inflater;
        mArrayList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Parse scan result
    private byte[] rawBytes = new byte[26];

    private String packetType;
    private DataItem[] tempDataItems = new DataItem[10];
    private DataItem[] dataItems;

    private void ParseScanResult() {
        packetType = toPacketType[Math.max(0, Math.min(toPacketType.length-1, rawBytes[0]))];

        int currentByte = 1, i;
        for(i = 0; i < 10; i++)
        {
            if(currentByte >= rawBytes.length || rawBytes[currentByte] == 0)
                break;

            //tempDataItems[i].length = rawBytes[currentByte];
            int currentLength = rawBytes[currentByte];
            currentByte++;
            //tempDataItems[i].type = toDataType[rawBytes[currentByte]];
            String currentType = toDataType[rawBytes[currentByte]];
            currentByte++;

            byte[] byteToString = new byte[currentLength - 1];
            for(int j = 0; j < currentLength - 1; j++){
                byteToString[j] = rawBytes[currentByte];
                currentByte ++;
            }
            //tempDataItems[i].data = new String(byteToString);
            String currentData = new String(byteToString);

            tempDataItems[i] = new DataItem(currentType, currentData);
        }

        dataItems = new DataItem[i];
        for(i=0; i<dataItems.length; ++i)
            dataItems[i] = tempDataItems[i];
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Reuse an old view if we can, otherwise create a new one.
        if (view == null) {
            view = mInflater.inflate(R.layout.listitem_scanresult, null);
        }

        TextView textViewPacketType = (TextView) view.findViewById(R.id.packet_type);
        TextView textViewLastSeen = (TextView) view.findViewById(R.id.last_seen);

        ScanResult scanResult = mArrayList.get(position);
        System.arraycopy(scanResult.getScanRecord().getBytes(), 5, rawBytes, 0, 26);
        ParseScanResult();

        String text = packetType + (ScannerFragment.isBlocked[Constants.PacketTypes.valueOf(packetType).ordinal()]? " (BLOCKED)" : "" );
        textViewPacketType.setText(text);

        ListView listViewScanData = (ListView) view.findViewById(R.id.listView_scandataitems);
        final DataItemAdapter scanDataItemAdapter = new DataItemAdapter(view.getContext(), LayoutInflater.from(view.getContext()));

        listViewScanData.setAdapter(scanDataItemAdapter);

        for(DataItem di : dataItems)
            scanDataItemAdapter.addItem(di);
        scanDataItemAdapter.notifyDataSetChanged();

        listViewScanData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DataItem di = (DataItem) scanDataItemAdapter.getItem(position);
                if(di.getType().equals("REGULAR_URL") || di.getType().equals("GOOGL_URL")) {
                    String url = di.getData();
                    if(!url.startsWith("http://") || !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                }
            }
        });

        textViewLastSeen.setText(getTimeSinceString(mContext, scanResult.getTimestampNanos()));

        return view;
    }

    /**
     * Search the adapter for an existing device address and return it, otherwise return -1.
     */
    /*private int getPosition(String address) {
        int position = -1;
        for (int i = 0; i < mArrayList.size(); i++) {
            if (mArrayList.get(i).getDevice().getAddress().equals(address)) {
                position = i;
                break;
            }
        }
        return position;
    }*/


    /**
     * Add a ScanResult item to the adapter if a result from that device isn't already present.
     * Otherwise updates the existing position with the new ScanResult.
     */
    public void add(ScanResult scanResult) {

        /*int existingPosition = getPosition(scanResult.getDevice().getAddress());

        if (existingPosition >= 0) {
            // Device is already in list, update its record.
            mArrayList.set(existingPosition, scanResult);
        } else {*/
            // Add new Device's ScanResult to list.
        mArrayList.add(scanResult);
        /*}*/
    }

    /**
     * Clear out the adapter.
     */
    public void clear() {
        mArrayList.clear();
    }

    /**
     * Takes in a number of nanoseconds and returns a human-readable string giving a vague
     * description of how long ago that was.
     */
    public static String getTimeSinceString(Context context, long timeNanoseconds) {
        String lastSeenText = "";

        long timeSince = SystemClock.elapsedRealtimeNanos() - timeNanoseconds;
        long secondsSince = TimeUnit.SECONDS.convert(timeSince, TimeUnit.NANOSECONDS);

        if (secondsSince < 5) {
            lastSeenText += context.getResources().getString(R.string.just_now);
        } else if (secondsSince < 60) {
            lastSeenText += secondsSince + " " + context.getResources()
                    .getString(R.string.seconds_ago);
        } else {
            long minutesSince = TimeUnit.MINUTES.convert(secondsSince, TimeUnit.SECONDS);
            if (minutesSince < 60) {
                if (minutesSince == 1) {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minute_ago);
                } else {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minutes_ago);
                }
            } else {
                long hoursSince = TimeUnit.HOURS.convert(minutesSince, TimeUnit.MINUTES);
                if (hoursSince == 1) {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hour_ago);
                } else {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hours_ago);
                }
            }
        }

        return lastSeenText;
    }
}
