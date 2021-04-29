package com.interlog.ilcargoreporting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.interlog.ilcargoreporting.DBHelper.TAG;

public class NetworkChecker extends BroadcastReceiver {
    private Context context;
    private DBHelper db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        db = new DBHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        userSignUp(
                                cursor.getInt(cursor.getColumnIndex(DBHelper.COL1)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL2)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL3)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL4)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL5)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL6)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL7)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL8)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL9)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL10)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL11)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL12)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL13)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL14)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.COL15))

                        );
                    } while (cursor.moveToNext());
                }
                Log.d(TAG, "add data: adding ");
            }
        }
    }

    private void userSignUp(final int id, final String custmName, final String address, final String prodcName, final String cdat, final String rdat, final String vessNam, final String opeBal, final String takOn, final String relz1, final String relz2, final String relz3, final String clsBal, final String bankRlz, final String bankBlc){
        /** do data upload using api call **/
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getNaSurvey()
                .submitResponse(custmName, address, prodcName, cdat, rdat, vessNam, opeBal, takOn, relz1, relz2, relz3, clsBal, bankRlz, bankBlc);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    if (!obj.getBoolean("error")) {
                        //updating the status in sqlite
                        db.updateNameStatus(id, WcStock.SYNC_STATUS_OK);

                        //sending the broadcast to refresh the list
                        context.sendBroadcast(new Intent(WcStock.DATA_SAVED_BROADCAST));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Map<String, String> params = new HashMap<>();
                params.put("custmName", custmName);
                params.put("address", address);
                params.put("prodcName", prodcName);
                params.put("cdat", cdat);
                params.put("rdat", rdat);
                params.put("vessNam", vessNam);
                params.put("opeBal", opeBal);
                params.put("takOn", takOn);
                params.put("relz1", relz1);
                params.put("relz2", relz2);
                params.put("relz3", relz3);
                params.put("clsBal", clsBal);
                params.put("bankRlz", bankRlz);
                params.put("bankBlc", bankBlc);
                return;

            }
        });

    }
}