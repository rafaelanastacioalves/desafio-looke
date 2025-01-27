package com.example.rafaelanastacioalves.moby.entitymainlisting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.rafaelanastacioalves.moby.R;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.listeners.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainEntityAdapter extends RecyclerView.Adapter<MainEntityViewHolder> {
    private RecyclerViewClickListener recyclerViewClickListener;
    private List<MainEntity.Objects> items = new ArrayList<>();

    private Context mContext;

    public MainEntityAdapter(Context context) {
        mContext = context;
    }


    public void setRecyclerViewClickListener(RecyclerViewClickListener aRVC) {
        this.recyclerViewClickListener = aRVC;
    }

    public List<MainEntity.Objects> getItems() {
        return this.items;
    }

    public void setItems(List<MainEntity.Objects> items) {
        this.items = items;
        notifyDataSetChanged();


    }

    @Override
    public MainEntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainEntityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_entity_viewholder, parent, false), recyclerViewClickListener);
    }



    @Override
    public void onBindViewHolder(MainEntityViewHolder holder, int position) {
        MainEntity.Objects aRepoW = getItems().get(position);
        ((MainEntityViewHolder) holder).bind(aRepoW, mContext);
    }

    @Override
    public int getItemCount() {
        if (getItems() != null){
            return getItems().size();
        }else{
            return 0;
        }
    }
}

