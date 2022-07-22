package com.example.ieaadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class NotificationBroadcast extends AppCompatActivity {
    EditText notificationBroadcastTitleEdtTxt, notificationBroadcastContentEdtTxt;
    AppCompatButton notificationBroadcastSendBtn, notificationBroadcastBackBtn;
    ProgressDialog sendingNotificationPd;
    final DatabaseReference memberNotificationRef = FirebaseDatabase.getInstance().getReference("Notification");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_broadcast);

        notificationBroadcastTitleEdtTxt = findViewById(R.id.notification_broadcast_title_edtTxt);
        notificationBroadcastContentEdtTxt = findViewById(R.id.notification_broadcast_content_edtTxt);
        notificationBroadcastBackBtn = findViewById(R.id.notification_broadcast_back_icon);
        notificationBroadcastSendBtn = findViewById(R.id.notification_broadcast_send_btn);
        sendingNotificationPd = new ProgressDialog(this);
        sendingNotificationPd.setMessage("Sending Notification");


        notificationBroadcastBackBtn.setOnClickListener(view -> finish());

        notificationBroadcastSendBtn.setOnClickListener(view -> {
            if (notificationBroadcastTitleEdtTxt.getText().toString().isEmpty()) {
                notificationBroadcastTitleEdtTxt.setError("Please provide title");
                notificationBroadcastTitleEdtTxt.requestFocus();
            } else if (notificationBroadcastContentEdtTxt.getText().toString().isEmpty()) {
                notificationBroadcastContentEdtTxt.setError("Please provide content");
                notificationBroadcastContentEdtTxt.requestFocus();
            } else {
                sendingNotificationPd.show();
                FirebaseDatabase.getInstance().getReference("Member Directory Token").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot memberToken : snapshot.getChildren()) {
                            String memberTokenStr = Objects.requireNonNull(memberToken.getValue()).toString();

                            FcmNotificationsSender grievanceNotificationSender = new FcmNotificationsSender(memberTokenStr,
                                    notificationBroadcastTitleEdtTxt.getText().toString(),
                                    notificationBroadcastContentEdtTxt.getText().toString(),
                                    getApplicationContext(),
                                    NotificationBroadcast.this);

                            grievanceNotificationSender.SendNotifications();

                            Log.d("MemberToken", notificationBroadcastTitleEdtTxt.getText().toString());
                            Log.d("MemberToken", notificationBroadcastContentEdtTxt.getText().toString());
                        }
                        for(DataSnapshot memberEmail : snapshot.getChildren()) {
                            String memberEmailStr = memberEmail.getKey();

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                            String notificationDate = df.format(date);

                            MemberNotificationModel memberNotificationModel = new MemberNotificationModel(notificationBroadcastTitleEdtTxt.getText().toString(),
                                    notificationBroadcastContentEdtTxt.getText().toString(),
                                    notificationDate);

                            memberNotificationRef.child(memberEmailStr).push().setValue(memberNotificationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    sendingNotificationPd.dismiss();
                                    Toast.makeText(NotificationBroadcast.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    sendingNotificationPd.dismiss();
                                    Toast.makeText(NotificationBroadcast.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}