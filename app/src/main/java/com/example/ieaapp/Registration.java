package com.example.ieaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        AppCompatButton proceed_pay = findViewById(R.id.proceed_to_pay_btn);
        AutoCompleteTextView Annual_turn = findViewById(R.id.autocomplete_annual_turnover);
        TextView member_fee = findViewById(R.id.member_price);
        EditText referal_code = findViewById(R.id.reg_refer_code);


        proceed_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, payment.class);

                EditText fullname = findViewById(R.id.fullname);
                EditText email = findViewById(R.id.registrationEmail);
                EditText phoneNo = findViewById(R.id.number);
                EditText Comapany_name = findViewById(R.id.company_name);
                AutoCompleteTextView Department = findViewById(R.id.autocomplete_department_field);
                AutoCompleteTextView Annual_turn = findViewById(R.id.autocomplete_annual_turnover);
                TextView member_fee = findViewById(R.id.member_price);

                intent.putExtra("name", fullname.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("phoneno", phoneNo.getText().toString());
                intent.putExtra("cname", Comapany_name.getText().toString());
                intent.putExtra("department", Department.getText().toString());
                intent.putExtra("annual_term", Annual_turn.getText().toString());
                intent.putExtra("memberfee", member_fee.getText().toString());

                if (TextUtils.isEmpty(fullname.getText().toString())) {
                    fullname.setError("Name cannot be empty!");
                    fullname.requestFocus();
                } else if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("Email cannot be empty!");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(phoneNo.getText().toString())) {
                    phoneNo.setError("Contact Number cannot be empty!");
                    phoneNo.requestFocus();
                } else if (TextUtils.isEmpty(Comapany_name.getText().toString())) {
                    Comapany_name.setError("Company name cannot be empty!");
                    Comapany_name.requestFocus();
                } else if (TextUtils.isEmpty(Department.getText().toString())) {
                    Department.setError("Department cannot be empty!");
                    Department.requestFocus();
                } else if (TextUtils.isEmpty(Annual_turn.getText().toString())) {
                    Annual_turn.setError("Annual Turnover cannot be empty!");
                    Annual_turn.requestFocus();
                } else if (TextUtils.isEmpty(member_fee.getText().toString())) {
                    member_fee.setError("Please select a membership!");
                    member_fee.requestFocus();
                } else {
                    startActivity(intent);
                }

            }
        });

        dropdownInit();
        dropdownannualturnover();


        AppCompatButton registrationBackButton = findViewById(R.id.registration_back_button);


        registrationBackButton.setOnClickListener(view -> {
            finish();
        });




        Thread t = new Thread() {


            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!Annual_turn.getText().toString().isEmpty()) {

                                    if (Annual_turn.getText().toString().equals("Below 5cr")) {
                                        member_fee.setText("Rs. 3,658/Yearly *including gst*");


                                    } else if (Annual_turn.getText().toString().equals("Between 5cr and 10cr")) {
                                        member_fee.setText("Rs. 6,018/Yearly *including gst*");


                                    } else if (Annual_turn.getText().toString().equals("Above 10cr")) {
                                        member_fee.setText("Rs. 12,980/Yearly *including gst*");


                                    }
                                }
                                else{
                                    member_fee.setHint("Membership Fees");
                                }
                            }
                        });


                    } catch (InterruptedException e) {
                        Log.d("thread", "run: " + e);
                    }
                }

            }
        };
        t.start();


    }


    @Override
    public void onResume() {
        super.onResume();
        dropdownInit();
        dropdownannualturnover();
    }

    public void dropdownInit() {
        String[] departments = getResources().getStringArray(R.array.department);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, departments);
        AutoCompleteTextView autoCompleteTextViewDepartments = findViewById(R.id.autocomplete_department_field);
        autoCompleteTextViewDepartments.setAdapter(arrayAdapterDepartments);

    }


    public void dropdownannualturnover(){
        String[] turnover = getResources().getStringArray(R.array.company_turnover);
        ArrayAdapter<String> arrayAdapterTurnover = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, turnover);
        AutoCompleteTextView Annual_turns = findViewById(R.id.autocomplete_annual_turnover);
        Annual_turns.setAdapter(arrayAdapterTurnover);
    }
}