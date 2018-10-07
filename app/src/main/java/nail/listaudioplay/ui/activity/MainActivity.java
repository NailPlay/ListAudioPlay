package nail.listaudioplay.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nail.listaudioplay.R;
import nail.listaudioplay.adapter.ItemAdapter;
import nail.listaudioplay.models.Item;
import nail.listaudioplay.presentation.presenter.MainPresenter;
import nail.listaudioplay.presentation.view.MainView;

public class MainActivity extends MvpAppCompatActivity implements MainView, ItemClickListener {
    public static final String TAG = "MainActivity";
    @InjectPresenter
    MainPresenter mMainPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvList1)
    RecyclerView recyclerView;
    @BindView(R.id.blockPlayning)
    LinearLayout blockPlayning;
    @BindView(R.id.tvNameBlock)
    TextView tvNameBlock;
    @BindView(R.id.progressBarDuration)
    ProgressBar progressBarDuration;

    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private GridLayoutManager mLayoutManager;

    public final static String TAG_PLAY = "PLAY";
    public final static String TAG_PAUSE = "PAUSE";
    public final static String TAG_STOP = "STOP";
    public final static String TAG_PREPARED = "PREPARED";

    public static int position = -1;

    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter(this);
    }

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        getMvpDelegate().onAttach();
        mMainPresenter.getMoreData();
    }


    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_close);
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemList, this);
        mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public void setDataToRecyclerView(List<Item> arrayItem) {
        itemList.addAll(arrayItem);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailture(Throwable t) {
        Log.e(TAG, t.getMessage());
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgresBarPosition() {
        itemList.get(this.position).setStatus(TAG_PLAY);
        itemAdapter.notifyItemChanged(this.position, TAG_PLAY);
    }

    @Override
    public void endPositionMusic() {
        itemList.get(this.position).setStatus(TAG_STOP);
        itemAdapter.notifyItemChanged(this.position, TAG_STOP);
    }

    @Override
    public void onError(int what, int extra) {
        if(this.position != -1) {
            itemList.get(this.position).setStatus(TAG_STOP);
            itemAdapter.notifyItemChanged(this.position, TAG_STOP);
        }
        Toast.makeText(this, "Error: " + " what: " + what + " extra: " + extra, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideBlockMusic() {
        blockPlayning.setVisibility(View.GONE);
    }

    @Override
    public void showBlockMusic(int position) {
        Item item = itemList.get(position);
        tvNameBlock.setText(getResources().getText(R.string.preset) + item.getName());
        blockPlayning.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDurationProgressBar(int currentPosition, int duration) {
        progressBarDuration.setProgress((int) (((float) currentPosition / duration) * 100));
    }

    @OnClick(R.id.buttonStopPlayning)
    public void onClickStopPlayning() {
        // стоп музы и закрытие блока
        Log.d(TAG, "onClickStopPlayning");
        mMainPresenter.hideBlockPlayning();
        itemList.get(this.position).setStatus(TAG_STOP);
        itemAdapter.notifyItemChanged(this.position, TAG_STOP);
        mMainPresenter.stopAudio();
        this.position = -1;
    }

    @Override
    public void onItemClick(int position) {
        mMainPresenter.showBlockPlayning(position);
        if (this.position == position) {
            // play pause
            Log.d(TAG, "CLICK PLAY/PAUSE");
            if (mMainPresenter.isPlayningAudio()) {
                itemList.get(position).setStatus(TAG_PAUSE);
                itemAdapter.notifyItemChanged(position, TAG_PAUSE);
            } else {
                itemList.get(position).setStatus(TAG_PLAY);
                itemAdapter.notifyItemChanged(position, TAG_PLAY);
            }
            mMainPresenter.playPauseAudio();
        } else {
            Log.d(TAG, "CLICK PREPARE PLAY");
            // prepare and reset item
            // предыдущий item'а просто сбиваем.
            if (this.position != -1) {
                itemList.get(this.position).setStatus(TAG_STOP);
                itemAdapter.notifyItemChanged(this.position, TAG_STOP);
            }
            // устанавливаем item'a на загрузку
            this.position = position;
            itemList.get(position).setStatus(TAG_PREPARED);
            itemAdapter.notifyItemChanged(position, TAG_PREPARED);
            mMainPresenter.playPrepareAudio(itemList.get(position).getAudio());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.pauseAudio();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.position != -1) {
            itemList.get(position).setStatus(TAG_PLAY);
            itemAdapter.notifyItemChanged(position, TAG_PLAY);
        }
        mMainPresenter.playAudio();
    }
}
