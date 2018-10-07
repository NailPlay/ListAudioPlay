package nail.listaudioplay.presentation.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import nail.listaudioplay.models.Item;

public interface MainView extends MvpView {
    void setDataToRecyclerView(List<Item> arrayItem);

    void onResponseFailture(Throwable t);

    void hideProgresBarPosition();

    void endPositionMusic();

    void onError(int what, int extra);

    void hideBlockMusic();

    void showBlockMusic(int position);

    void setDurationProgressBar(int currentPosition, int duration);
}
