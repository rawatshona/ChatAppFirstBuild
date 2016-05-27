package com.firebase.androidchat;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class MainActivity extends ListActivity {

    private static final String URL = "https://apptestfor.firebaseio.com/";

    private String myUserName;
    private Firebase refVariable;

    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRegister();

        setTitle("Chatting as " + myUserName);

        refVariable = new Firebase(URL);

        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        final ListView listView = getListView();
        mChatListAdapter = new ChatListAdapter(refVariable.limit(50), this, R.layout.chat_message, myUserName);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
 }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public void onStop() {
        super.onStop();

        mChatListAdapter.cleanup();
    }

    private void isRegister() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        myUserName = prefs.getString("myusername", null);
        if (myUserName == null) {
            Intent i=new Intent(this,Registration.class);
            startActivity(i);
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            Chat chat = new Chat(input, myUserName);
            refVariable.push().setValue(chat);
            inputText.setText("");
        }
    }
}
