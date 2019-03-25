package sayrunjah.atmlocator.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CHARLES on 09/02/2017.
 */
public class Connection {

    public void makeRequest(String url,HashMap<String,String> map, final GetResponse getResponse){

        OkHttpClient client = new OkHttpClient();
       /* FormBody.Builder formBuilder = new FormBody.Builder();

        for(Map.Entry<String, String> entry : map.entrySet()){
            formBuilder.add(entry.getKey(),entry.getValue());
        }
        RequestBody formBody = formBuilder.build();*/

        /*Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();*/
        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e){
                getResponse.response(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getResponse.response(response.body().string());

            }
        });

    }

    public interface GetResponse{
        void response(String response);
    }

}
