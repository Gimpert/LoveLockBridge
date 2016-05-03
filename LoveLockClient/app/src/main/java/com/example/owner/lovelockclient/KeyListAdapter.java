package com.example.owner.lovelockclient;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.BridgeCommunication.ResponseParser;
import com.example.owner.BridgeCommunication.ServerRelay;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Owner on 2/16/2016.
 */
public class KeyListAdapter extends ArrayAdapter<Lock> {
    static final String NO_LOCK_MESSAGE = "You are not in range of a bridge";
    static final String NO_LOCATION = "Loction Unknown";
    static final String NO_LOCATION_DETAILS = "Turn on your GPS";
    private final static int SEND_LOCK = 0, UNLOCK_LOCK = 1;

    Button sendSubmitButton;

    public KeyListAdapter(Context context) {
        super(context, R.layout.key_list_group_item, LockList.getInstance().getList());

    }

    private void removeLock(Lock lock) {
        LockList.getInstance().removeLock(lock);
        LockList.getInstance().getList().remove(lock);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Lock lock = getItem(position);
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        if (view == null){
            view = inflater.inflate(R.layout.key_list_group_item, parent, false);

        }

        TextView keyName = (TextView) view.findViewById(R.id.key_name);
        Button send_button = (Button) view.findViewById(R.id.send_button);
        Button unlock_button = (Button) view.findViewById(R.id.unlock_button);
        Button throw_button = (Button) view.findViewById(R.id.throw_away_button);
        keyName.setText(lock.getName());


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = inflater.inflate(R.layout.send_lock_popup, null, false);
                final PopupWindow popupWindow = new PopupWindow(popupView, 1, 1, true);
                final EditText recipientEmail = (EditText) popupView.findViewById(R.id.recipient_email_form);
                final EditText recipientName = (EditText) popupView.findViewById(R.id.recipient_name_form);
                final EditText senderName = (EditText) popupView.findViewById(R.id.sender_name_form);
                final EditText senderMessaage = (EditText) popupView.findViewById(R.id.sender_message_form);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

                popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);

                sendSubmitButton = (Button) popupView.findViewById(R.id.send_submit_button);
                sendSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recipientEmail.getText().toString().equals("") || recipientName.getText().toString().equals("") ||
                                senderName.getText().toString().equals("") || senderMessaage.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.getContext(), "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(recipientEmail.getText().toString(),recipientName.getText().toString(),
                                senderName.getText().toString(), senderMessaage.getText().toString(), lock);
                        httpAsyncTask.execute();
                        String result = "";
                        try {
                            result = httpAsyncTask.get(MainActivity.TIMEOUT_SECONDS, TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            Toast.makeText(MainActivity.getContext(),"Server Timeout", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(result == null) {
                            Toast.makeText(MainActivity.getContext(),"Network Error", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if(result.toLowerCase().equals("message sent")) {
                                Toast.makeText(MainActivity.getContext(), "Sent key for " + lock.getName() +
                                        " to " + recipientName.getText().toString(), Toast.LENGTH_SHORT).show();
                                removeLock(lock);
                            } else {
                                Toast.makeText(MainActivity.getContext(),"Invalid Field(s)", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        popupWindow.dismiss();
                    }
                });
            }
        });

        throw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLock(lock);

                Toast burnt = Toast.makeText(MainActivity.getContext(), "Removed " + lock.getName(), Toast.LENGTH_SHORT);
                burnt.setGravity(0,0,0);
                burnt.show();
            }
        });



        unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.message_popup, null, false), 1, 1, true);
                TextView message_name = (TextView) popupWindow.getContentView().findViewById(R.id.message_name);
                TextView message_body = (TextView) popupWindow.getContentView().findViewById(R.id.message_body);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                message_name.setTextSize(20);
                String headerMessage = "Message from " + lock.getName();
                if (lock.getMessage() != null) {
                    message_name.setText(headerMessage);
                    message_body.setText(lock.getMessage());

                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                } else {
                    Location currentLoction = BridgeProximity.getInstance().getCurrentlocation();
                    if (currentLoction != null) {

                        HttpAsyncTask httpAsyncTask = new HttpAsyncTask("" + currentLoction.getLatitude(), "" + currentLoction.getLongitude(), lock);
                        httpAsyncTask.execute();

                        String message;
                        try {
                            message = ResponseParser.parseMessage(httpAsyncTask.get(MainActivity.TIMEOUT_SECONDS, TimeUnit.SECONDS));
                            lock.setMessage(message);
                        } catch (TimeoutException e) {
                            Toast.makeText(MainActivity.getContext(),"Server Timeout", Toast.LENGTH_SHORT).show();
                            message = null;
                        } catch (Exception e) {
                            message = null;
                        }

                        if (message == null) {
                            message_name.setText(NO_LOCK_MESSAGE);
                            message_body.setText("");
                        } else {
                            Toast.makeText(MainActivity.getContext(),"Unlocked", Toast.LENGTH_SHORT).show();
                            message_name.setText(headerMessage);
                            message_body.setText(message);
                        }
                    } else {
                        message_name.setText(NO_LOCATION);
                        message_body.setText(NO_LOCATION_DETAILS);
                    }

                    popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
                }
            }
        });


        return view;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private String  recipientEmail, recipientName;
        private String senderName, senderMessage;

        private String lat,lng;
        private Lock lock;

        private int taskCode;

        public HttpAsyncTask( String recipientEmail, String recipientName, String senderName, String senderMessage, Lock lock){
            this.recipientEmail = recipientEmail;
            this.recipientName = recipientName;
            this.senderName = senderName;
            this.senderMessage = senderMessage;
            this.lock = lock;
            this.taskCode = SEND_LOCK;
        }

        public HttpAsyncTask(String lat, String lng, Lock lock){
            this.lat = lat;
            this.lng = lng;
            this.lock = lock;
            this.taskCode = UNLOCK_LOCK;
        }


        @Override
        protected String doInBackground(String... params) {
            switch (taskCode) {
                case SEND_LOCK:
                    String response = ServerRelay.sendKey(lock.getId(), lock.getPassword(), this.recipientEmail, this.recipientName, this.senderName, this.senderMessage);
                    return ResponseParser.parseSendResponse(response);
                case UNLOCK_LOCK:
                    return ServerRelay.unlockLock(lock.getId(), lat, lng, lock.getPassword());
            }
            return null;
        }

        public void updateLockList(){
            notifyDataSetChanged();
        }
    }
}
