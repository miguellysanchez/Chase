package com.voyager.chase.game.skill;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voyager.chase.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by miguellysanchez on 7/17/16.
 */
public class SkillsAdapter extends BaseAdapter {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private ArrayList<Skill> mSkillsList;
    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onClick(Skill skill);
    }

    public SkillsAdapter(Context context, OnClickListener onClickListener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mOnClickListener = onClickListener;
        mSkillsList = new ArrayList<>();
    }

    public void setSkillList(ArrayList<Skill> skillsList) {
        if (skillsList != null) {
            mSkillsList = skillsList;
        } else {
            Timber.e("Cannot set skill items to null list");
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSkillsList.size();
    }

    @Override
    public Skill getItem(int position) {
        return mSkillsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.chase_listitem_skill, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Skill skill = mSkillsList.get(position);
        holder.textViewName.setText(skill.getSkillName());
        if (skill.getCurrentCooldown() > 0) { //skill is not ready to be used/on cooldown
            holder.textViewCooldown.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            holder.textViewCooldown.setText(skill.getCurrentCooldown());
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            holder.textViewCooldown.setTextColor(ContextCompat.getColor(mContext, R.color.very_light_blue));
            if (skill.getCooldown() > 0) {
                holder.textViewCooldown.setText("OK | " + skill.getCooldown() + " turns");
            } else {
                holder.textViewCooldown.setText("OK");
            }
            holder.linearLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(skill);
                }
            });
        }
        holder.textViewDescription.setText(skill.getDescription());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.chase_listitem_skill_linearlayout_container)
        LinearLayout linearLayoutContainer;
        @BindView(R.id.chase_listitem_skill_textview_name)
        TextView textViewName;
        @BindView(R.id.chase_listitem_skill_textview_cooldown)
        TextView textViewCooldown;
        @BindView(R.id.chase_listitem_skill_textview_description)
        TextView textViewDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
