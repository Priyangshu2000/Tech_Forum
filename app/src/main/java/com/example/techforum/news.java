package com.example.techforum;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techforum.ChatBot.Message;
import com.example.techforum.ChatBot.MessageAdapter;
import com.example.techforum.apiPackage.Articles;
import com.example.techforum.apiPackage.NewsModal;
import com.example.techforum.apiPackage.RetrofitInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class news extends Fragment {
//    String apikey="863cd1f9eb124d799145f8b49faabd34";
//    ArrayList<Articles>articles;
//    String country="in";
//    newsAdapter adapter;

    EditText input;
    ImageButton send;
    RecyclerView recyclerView;
    List<Message> messages;
    MessageAdapter messageAdapter;
    TextView welcome;
    public  static final  String URL="https://api.openai.com/v1/chat/completions";
    public  static  String API_KEY="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";
    String GptMessage;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

//    private RecyclerView newsRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view= inflater.inflate(R.layout.fragment_news, container, false);
//        newsRecyclerView=view.findViewById(R.id.news_recyclerView);
//        articles=new ArrayList<>();
//        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter=new newsAdapter(getContext(), articles);
//        newsRecyclerView.setAdapter(adapter);
//        findNews();
//        return view;


        View view= inflater.inflate(R.layout.activity_main_ui, container, false);
        input=view.findViewById(R.id.message_edit_text);
        send=view.findViewById(R.id.send_btn);
        recyclerView=view.findViewById(R.id.recycler_view);

        messages=new ArrayList<>();
        messageAdapter=new MessageAdapter(messages);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        welcome=view.findViewById(R.id.welcome);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=input.getText().toString();
                welcome.setVisibility(View.GONE);
                addChat(message,true);
                input.setText("");
                getResponse(message);
            }
        });
        return view;
    }

//    private void findNews() {
//        RetrofitInstance.getApiInterface().get_tech_news(country,100,apikey,"technology").enqueue(new Callback<NewsModal>() {
//            @Override
//            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
//                if(response.isSuccessful()){
//                    articles.addAll(response.body().getArticles());
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewsModal> call, Throwable t) {
//                Toast.makeText(getContext(), "Error retrieving news", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }



    @SuppressLint("NotifyDataSetChanged")
    public void addChat(String message, Boolean sentByUser){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(new Message(sentByUser,message.trim()));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });


    }

    public void add(String message,Boolean user){
        addChat(message,user);
    }

    public void getResponse(String question) {
        try {
            JSONObject msgBody=new JSONObject();
            msgBody.put("role","user");
            msgBody.put("content",question);
            JSONArray array=new JSONArray();
            array.put(msgBody);
            JSONObject object = new JSONObject();
            object.put("model", "gpt-3.5-turbo");
            object.put("messages", array);
//            object.put("max_tokens", 4000);
//            object.put("temperature", 0);


            RequestBody body = RequestBody.create(JSON, object.toString());
            Request request = new Request.Builder()
                    .url(URL)
                    .header("Authorization", "Bearer " + API_KEY)
                    .post(body).build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    addChat("Failed"+e.getMessage(), false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            Log.d("response",jsonArray.toString());
                            JSONObject mssg = jsonArray.getJSONObject(0);
                            GptMessage=mssg.getJSONObject("message").getString("content");

//                            GptMessage="hello";
                            Log.d("response",mssg.getJSONObject("message").toString());
                            add(GptMessage, false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        addChat("Failed"+response, false);
                    }
                }
            });
        }catch (Exception e){
            Log.d("response",e.getMessage());
        }
    }


}