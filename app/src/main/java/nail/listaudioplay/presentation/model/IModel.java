package nail.listaudioplay.presentation.model;

import android.content.Context;

import java.util.List;

import nail.listaudioplay.models.Item;

public interface IModel {
    interface OnFinishedListener {
        void onFinished(List<Item> arrayList);

        void onFailture(Throwable t);
    }

    void getDataList(OnFinishedListener onFinishedListener);

}
