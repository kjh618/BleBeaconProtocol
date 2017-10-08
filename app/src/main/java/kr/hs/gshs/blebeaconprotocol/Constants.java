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

import android.os.ParcelUuid;

import static android.bluetooth.BluetoothClass.Service.INFORMATION;

/**
 * Constants for use in the Bluetooth Advertisements sample
 */
public class Constants {

    /**
     * UUID identified with this app - set as Service UUID for BLE Advertisements.
     *
     * Bluetooth requires a certain format for UUIDs associated with Services.
     * The official specification can be found here:
     * {@link https://www.bluetooth.org/en-us/specification/assigned-numbers/service-discovery}
     */
    public static final ParcelUuid Service_UUID = ParcelUuid
            .fromString("0000b81d-0000-1000-8000-00805f9b34fb");

    public static final int REQUEST_ENABLE_BT = 1;

    public enum PacketTypes {
        INFORMATION, ADVERTISEMENT, COUPON, CAUTION
    }
    public enum DataTypes {
        TEXT_UNCOMPRESSED, TEXT_RUN_LENGTH_ENCODING, TEXT_HUFFMAN_CODING, REGULAR_URL, GOOGL_URL, DEVICE_NAME, SERVICE_NAME
    }

    public static final String[] toPacketType, toDataType;

    static {
        PacketTypes[] pts = PacketTypes.values();
        toPacketType = new String[pts.length];
        int i = 0;
        for(PacketTypes pt : pts) {
            toPacketType[i++] = pt.name();
        }

        DataTypes[] dts = DataTypes.values();
        toDataType = new String[dts.length];
        i = 0;
        for(DataTypes dt : dts) {
            toDataType[i++] = dt.name();
        }
    }
}
