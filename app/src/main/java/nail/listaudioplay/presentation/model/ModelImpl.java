package nail.listaudioplay.presentation.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import nail.listaudioplay.R;
import nail.listaudioplay.models.Item;

public class ModelImpl implements IModel {
    private final Context context;

    public ModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getDataList(OnFinishedListener onFinishedListener) {
        String json = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.config);
            int size = inputStream.available();
            Log.d("MainActivity", "size: " + size);
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Item>>() {
            }.getType();
            List<Item> items = gson.fromJson(json, listType);
            onFinishedListener.onFinished(items);
        } catch (IOException e) {
            onFinishedListener.onFailture(e);
            e.printStackTrace();
        }
    }

}
