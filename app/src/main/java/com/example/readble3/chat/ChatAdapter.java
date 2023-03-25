package com.example.readble3.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readble3.MemoryData;
import com.example.readble3.R;

import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{
//    MenuItem menuItem;
    private List<ChatList> chatLists;
    private final String userMobile;
    Context context;
    TextToSpeech textToSpeech;
    public ChatAdapter(List<ChatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userMobile = MemoryData.getMobile(context);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        ChatList list2 = chatLists.get(position);

        if (list2.getMobile().equals(userMobile)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myTime.setText(list2.getDate() + " " + list2.getTime());
        } else {
            holder.myLayout.setVisibility(View.GONE);
            holder.oppoLayout.setVisibility(View.VISIBLE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoTime.setText(list2.getDate() + " " + list2.getTime());
        }
        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech. ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
        holder.oppoMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtToSpeech = holder.oppoMessage.getText().toString();
                textToSpeech.speak(txtToSpeech,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
        holder.myMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtToSpeech = holder.myMessage.getText().toString();
                textToSpeech.speak(txtToSpeech,TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });


    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<ChatList> chatLists) {
        this.chatLists = chatLists;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout oppoLayout;
        private final LinearLayout myLayout;
        private final TextView oppoMessage;
        private final TextView myMessage;
        private final TextView oppoTime;
        private final TextView myTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoTime = itemView.findViewById(R.id.oppoMsgTime);
            myTime = itemView.findViewById(R.id.myMsgTime);
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_menu, menu);
////        optionsMenu = menu;
////        MenuItem speedIs0_5 = optionsMenu.findItem(R.id.speedIs0_5x);
////        MenuItem speedIs0_75 = optionsMenu.findItem(R.id.speedIs0_75x);
////        MenuItem speedIs1 = optionsMenu.findItem(R.id.speedIs1x);
////        MenuItem speedIs1_5 = optionsMenu.findItem(R.id.speedIs1_5x);
////        MenuItem speedIs2 = optionsMenu.findItem(R.id.speedIs2_0x);
//        return true;
//    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.speedIs0_5x:
//
//                textToSpeech.setSpeechRate(0.5F);
//                // Code for About goes here
//                break;
//            case R.id.speedIs0_75x:
//
//                textToSpeech.setSpeechRate(0.75F);
//
//                // Code for Help goes here
//                break;
//            case R.id.speedIs1x:
//
//                textToSpeech.setSpeechRate(1.0F);
//
//                // SignOut method call goes here
//                break;
//            case R.id.speedIs1_5x:
//                textToSpeech.setSpeechRate(1.5F);
//
//                break;
//            case R.id.speedIs2_0x:
//                textToSpeech.setSpeechRate(2.0F);
//
//                break;
////            default:
////                return super.onOptionsItemSelected(item);
//        }
//        return false;
//    }
}

