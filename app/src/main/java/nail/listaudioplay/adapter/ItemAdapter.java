package nail.listaudioplay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nail.listaudioplay.models.Item;
import nail.listaudioplay.R;
import nail.listaudioplay.ui.activity.MainActivity;

import static nail.listaudioplay.ui.activity.MainActivity.TAG;
import static nail.listaudioplay.ui.activity.MainActivity.TAG_PAUSE;
import static nail.listaudioplay.ui.activity.MainActivity.TAG_PLAY;
import static nail.listaudioplay.ui.activity.MainActivity.TAG_PREPARED;
import static nail.listaudioplay.ui.activity.MainActivity.TAG_STOP;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> itemList;
    private final MainActivity mainActivity;

    public ItemAdapter(Context mContext, List<Item> itemList, MainActivity mainActivity) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder 2 argument");
        Item item = itemList.get(position);
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Play / Pause buttons should be displayed only for files that have audio in the config.///
        if (item.getAudio() == null) holder.btnAudio.setVisibility(View.INVISIBLE);              ///
        else if (item.getAudio().equals("")) holder.btnAudio.setVisibility(View.INVISIBLE);      ///
        else                                                                                     ///
            holder.btnAudio.setVisibility(View.VISIBLE);                                         ///
        ////////////////////////////////////////////////////////////////////////////////////////////
        holder.tvAudio.setText(item.getName().toString());
        Glide.with(mContext)
                .load(item.getImage().toString())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_action_no_image))
                .into(holder.ivAudio);
        holder.btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.onItemClick(position);
            }
        });
        if (item.getStatus() != null) {
            checkTag(holder, item);
        } else {
            holder.btnAudio.setImageResource(R.mipmap.ic_action_play);
            holder.progressBarAudio.setIndeterminate(false);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payload) {
        Item item = itemList.get(position);
        if (!payload.isEmpty()) {
            checkTag(holder, item);
            Log.d(TAG, "Click: " + position + " - " + itemList.get(position).getStatus());
        } else {
            super.onBindViewHolder(holder, position, payload);
        }
    }

    private void checkTag(MyViewHolder holder, Item item) {
        if (item.getStatus().equals(TAG_PLAY)) {
            holder.btnAudio.setImageResource(R.mipmap.ic_action_pause);
            holder.progressBarAudio.setIndeterminate(false);
        } else if (item.getStatus().equals(TAG_STOP)) {
            holder.btnAudio.setImageResource(R.mipmap.ic_action_play);
            holder.progressBarAudio.setIndeterminate(false);
        }
        if (item.getStatus().equals(TAG_PAUSE)) {
            holder.btnAudio.setImageResource(R.mipmap.ic_action_play);
            holder.progressBarAudio.setIndeterminate(false);
        }
        if (item.getStatus().equals(TAG_PREPARED)) {
            holder.btnAudio.setImageResource(R.mipmap.ic_action_pause);
            holder.progressBarAudio.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textAudio)
        TextView tvAudio;
        @BindView(R.id.imageAudio)
        ImageView ivAudio;
        @BindView(R.id.buttonAudio)
        ImageButton btnAudio;
        @BindView(R.id.progressAudio)
        ProgressBar progressBarAudio;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}