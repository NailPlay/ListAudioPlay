package nail.listaudioplay.presentation.presenter;


import android.content.Context;
import android.util.Log;

import nail.listaudioplay.models.Item;
import nail.listaudioplay.presentation.model.IMedia;
import nail.listaudioplay.presentation.model.IModel;
import nail.listaudioplay.presentation.model.MediaManagerImpl;
import nail.listaudioplay.presentation.model.ModelImpl;
import nail.listaudioplay.presentation.view.MainView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements IModel.OnFinishedListener, IMedia.OnMediaListener {

    private ModelImpl model;
    private MediaManagerImpl mediaManager;

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
    }

    public void playPrepareAudio(String url) {
        mediaManager.playPrepare(this, url);
    }

    public boolean isPlayningAudio() {
        return mediaManager.isPlayning();
    }

    public void playPauseAudio() {
        mediaManager.playPause();
    }

    public void stopAudio() {
        mediaManager.Stop();
    }

    public void pauseAudio(){
        mediaManager.Pause();
    }

    public void playAudio(){
        mediaManager.Play();
    }

    public MainPresenter(Context context) {
        model = new ModelImpl(context);
        mediaManager = new MediaManagerImpl(context);
    }

    public void getMoreData() {
        model.getDataList(this);
    }

    public void hideBlockPlayning() {
        getViewState().hideBlockMusic();
    }

    public void showBlockPlayning(int position) {
        getViewState().showBlockMusic(position);
    }

    @Override
    public void onFinished(List<Item> arrayList) {
        getViewState().setDataToRecyclerView(arrayList);
    }

    @Override
    public void onFailture(Throwable t) {
        getViewState().onResponseFailture(t);
    }

    @Override
    public void onComplete() {
        Log.d("MainActitivy", "HideProgressBar");
        getViewState().hideProgresBarPosition();
    }

    @Override
    public void onEnd() {
        Log.d("MainActitivy", "End Music HideProgressBar");
        getViewState().endPositionMusic();
    }

    @Override
    public void onError(int what, int extra) {
        getViewState().onError(what, extra);
    }

    @Override
    public void onSetDuration(int currentPosition, int duration) {
        getViewState().setDurationProgressBar(currentPosition,duration);
    }
}
