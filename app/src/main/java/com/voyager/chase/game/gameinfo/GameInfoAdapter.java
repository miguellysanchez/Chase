package com.voyager.chase.game.gameinfo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.chase.R;
import com.voyager.chase.game.World;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguellysanchez on 8/4/16.
 */
public class GameInfoAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private ArrayList<GameInfo> mGameInfoArrayList;
    private String userPlayerRole;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;

    public GameInfoAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mGameInfoArrayList = new ArrayList<>();
        userPlayerRole = World.getUserPlayer().getRole();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        calendar = Calendar.getInstance();
    }

    @Override
    public int getCount() {
        return mGameInfoArrayList.size();
    }

    @Override
    public GameInfo getItem(int position) {
        return mGameInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.chase_listitem_game_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GameInfo gameInfo = getItem(mGameInfoArrayList.size()-position-1);
        String senderRole = gameInfo.getSenderRole();
        holder.textViewSenderRole.setText(senderRole);
        if (senderRole.equals(userPlayerRole)) {
            holder.linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.sky_blue));
        } else {
            holder.linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_red));
        }
        calendar.setTimeInMillis(gameInfo.getTimestamp());
        String formattedTime = simpleDateFormat.format(calendar.getTime());
        holder.textViewTimestamp.setText(formattedTime);
        holder.textViewContent.setText(gameInfo.getInfo());

        return convertView;
    }

    public void addToGameInfoArrayList(GameInfo gameInfo) {
        mGameInfoArrayList.add(gameInfo);
        notifyDataSetChanged();
    }

    public void removeAllGameInfo() {
        mGameInfoArrayList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.chase_listitem_game_info_linearlayout_container)
        LinearLayout linearLayoutContainer;
        @BindView(R.id.chase_listitem_game_info_textview_content)
        TextView textViewContent;
        @BindView(R.id.chase_listitem_skill_textview_timestamp)
        TextView textViewTimestamp;
        @BindView(R.id.chase_listitem_skill_textview_sender_role)
        TextView textViewSenderRole;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
