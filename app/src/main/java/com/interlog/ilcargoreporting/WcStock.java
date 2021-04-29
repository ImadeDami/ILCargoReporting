package com.interlog.ilcargoreporting;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WcStock extends Fragment {
    EditText vesNam, opBal, tkOn, rel1, rel2, rel3, bankRz, bankBa;
    TextView clBal, cdt, rdt;
    Spinner custName, locatAdr, prodName;
    Button submit, editDa;
    DBHelper dbHelper;
    DatePickerDialog datePickerDialog;

    public WcStock() {

    }

    BroadcastReceiver broadcastReceiver;

    public static final int SYNC_STATUS_OK = 1;
    public static final int SYNC_STATUS_FAILED = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    public static final String UI_UPDATE_BROADCAST = "com.interlog.ilcargoreporting.uiupdatebroadcast";
    public static final String URL_SAVE_NAME = "http://interlog-ng.com/interlogmobile/wc.php";

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_wc, container, false);

        dbHelper = new DBHelper(getActivity());

        //registering the broadcast receiver to update sync status
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        getActivity().registerReceiver(new NetworkChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        dbHelper.getData();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dbHelper.getData();
            }
        };

        custName = view.findViewById(R.id.custName);
        locatAdr = view.findViewById(R.id.locatAdr);
        prodName = view.findViewById(R.id.prodName);
        cdt = view.findViewById(R.id.cdt);
        rdt = view.findViewById(R.id.rdt);
        vesNam = view.findViewById(R.id.vesNam);
        opBal = view.findViewById(R.id.opBal);
        tkOn = view.findViewById(R.id.tkOn);
        rel1 = view.findViewById(R.id.rel1);
        rel2 = view.findViewById(R.id.rel2);
        rel3 = view.findViewById(R.id.rel3);
        clBal = view.findViewById(R.id.clBal);
        bankRz = view.findViewById(R.id.bankRz);
        bankBa = view.findViewById(R.id.bankBa);
        submit = view.findViewById(R.id.submitBtn);
        editDa = view.findViewById(R.id.editDa);

        editDa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditData.class);
                startActivity(intent);
            }
        });

        cdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        cdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        rdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        rdt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        TextWatcher autoAddTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double myNum1 = TextUtils.isEmpty(opBal.getText().toString()) ? 0 : Double.parseDouble(opBal.getText().toString());
                double myNum2 = TextUtils.isEmpty(tkOn.getText().toString()) ? 0 : Double.parseDouble(tkOn.getText().toString());
                double myNum3 = TextUtils.isEmpty(rel1.getText().toString()) ? 0 : Double.parseDouble(rel1.getText().toString());
                double myNum4 = TextUtils.isEmpty(rel2.getText().toString()) ? 0 : Double.parseDouble(rel2.getText().toString());
                double myNum5 = TextUtils.isEmpty(rel3.getText().toString()) ? 0 : Double.parseDouble(rel3.getText().toString());

                double tot = (myNum1 + myNum2) - (myNum3 + myNum4 + myNum5);
                clBal.setText(Double.toString(tot));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        opBal.addTextChangedListener(autoAddTextWatcher);
        tkOn.addTextChangedListener(autoAddTextWatcher);
        rel1.addTextChangedListener(autoAddTextWatcher);
        rel2.addTextChangedListener(autoAddTextWatcher);
        rel3.addTextChangedListener(autoAddTextWatcher);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custmName = custName.getSelectedItem().toString();
                String address = locatAdr.getSelectedItem().toString();
                String prodcName = prodName.getSelectedItem().toString();
                String cdat = cdt.getText().toString();
                String rdat = rdt.getText().toString();
                String vessNam = vesNam.getText().toString();
                String opeBal = opBal.getText().toString();
                String takOn = tkOn.getText().toString();
                String relz1 = rel1.getText().toString();
                String relz2 = rel2.getText().toString();
                String relz3 = rel3.getText().toString();
                String clsBal = clBal.getText().toString();
                String bankRlz = bankRz.getText().toString();
                String bankBlc = bankBa.getText().toString();

                if (custmName.isEmpty()) {
                    Toast.makeText(getActivity(), "enter customer name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (address.isEmpty()) {
                    Toast.makeText(getActivity(), "enter address", Toast.LENGTH_SHORT).show();
                }
                if (prodcName.isEmpty()) {
                    Toast.makeText(getActivity(), "enter product name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cdat.isEmpty()) {
                    Toast.makeText(getActivity(), "enter customer name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rdat.isEmpty()) {
                    Toast.makeText(getActivity(), "enter address", Toast.LENGTH_SHORT).show();
                }
                if (vessNam.isEmpty()) {
                    Toast.makeText(getActivity(), "enter product name", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**if (cdat.isEmpty()) {
                 cdt.setError("some fields are empty");
                 cdt.requestFocus();
                 return;
                 }
                 if (rdat.isEmpty()) {
                 rdt.setError("some fields are empty");
                 rdt.requestFocus();
                 return;
                 }**/
                if (opeBal.isEmpty()) {
                    Toast.makeText(getActivity(), "enter opening balance", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (takOn.isEmpty()) {
                    Toast.makeText(getActivity(), "enter take on", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (relz1.isEmpty()) {
                    Toast.makeText(getActivity(), "enter release value", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper.addData(custmName, address, prodcName, cdat, rdat, vessNam, opeBal, takOn, relz1, relz2, relz3, clsBal, bankRlz, bankBlc, SYNC_STATUS_OK);
                Toast.makeText(getActivity(), "Submitted...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);


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
                                //if there is a success
                                //storing the name to sqlite with status synced
                                dbHelper.addData(custmName, address, prodcName, cdat, rdat, vessNam, opeBal, takOn, relz1, relz2, relz3, clsBal, bankRlz, bankBlc, SYNC_STATUS_OK);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                dbHelper.addData(custmName, address, prodcName, cdat, rdat, vessNam, opeBal, takOn, relz1, relz2, relz3, clsBal, bankRlz, bankBlc, SYNC_STATUS_OK);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            // String s = response.body().toString();
                            Toast.makeText(getActivity(), "Submitted...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dbHelper.addData(custmName, address, prodcName, cdat, rdat, vessNam, opeBal, takOn, relz1, relz2, relz3, clsBal, bankRlz, bankBlc, SYNC_STATUS_OK);
                        //Toast.makeText(SurveyActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "data has been saved on phone and will submitted once there is internet connection", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        return view;
    }


    public void onStart(){
        super.onStart();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(UI_UPDATE_BROADCAST));
    }

    }