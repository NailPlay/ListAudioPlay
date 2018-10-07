package nail.listaudioplay.presentation.model;

import java.io.IOException;

public interface IMedia {
    boolean isPlayning();

    void playPrepare(final OnMediaListener onPreparedListener, String url) throws IOException;

    void playPause();

    void Play();

    void Pause();

    void Stop();

    interface OnMediaListener {
        void onComplete();

        void onEnd();

        void onError(int what, int extra);

        void onSetDuration(int currentPosition, int duration);
    }
}
