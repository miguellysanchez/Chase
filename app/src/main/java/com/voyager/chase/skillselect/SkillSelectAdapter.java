package com.voyager.chase.skillselect;

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
 * Created by miguellysanchez on 7/13/16.
 */
public class SkillSelectAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<SkillSelect> mSkillSelectList;
    private OnClickListener mOnClickListener;
    private ArrayList<String> mSelectedSkillNames;

    public interface OnClickListener {
        void onClick(SkillSelect skillSelect);
    }

    public SkillSelectAdapter(Context context, OnClickListener onClickListener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mSkillSelectList = new ArrayList<>();
        mOnClickListener = onClickListener;
        mSelectedSkillNames = new ArrayList<>();
    }


    public void setSkillSelectList(ArrayList<SkillSelect> skillSelectList) {
        if (skillSelectList != null) {
            mSkillSelectList = skillSelectList;
        } else {
            Timber.e("Cannot set skill selection items to null list");
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSkillSelectList.size();
    }

    @Override
    public SkillSelect getItem(int position) {
        return mSkillSelectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.chase_listitem_skill_select, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SkillSelect skillSelect = mSkillSelectList.get(position);

        holder.linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mContext,
                skillSelect.isSelected() ? R.color.very_light_blue : R.color.white));
        holder.textViewSkillName.setText(skillSelect.getName());
        holder.textViewSkillCost.setText(skillSelect.getSkillCost() + " SP");
        String cooldownTurnsString = skillSelect.getCooldownTurns() <= 0 ? "NONE" : skillSelect.getCooldownTurns() + " Turns";
        holder.textViewCooldown.setText(cooldownTurnsString);
        holder.textViewDescription.setText(skillSelect.getDescription());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(skillSelect);
                if (skillSelect.isSelected()) {
                    holder.linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.very_light_blue));
                    mSelectedSkillNames.add(skillSelect.getName());
                } else {
                    holder.linearLayoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                    mSelectedSkillNames.remove(skillSelect.getName());
                }
            }

        });


        return convertView;
    }

    public ArrayList<String> getSkillNamesSelectedList() {
        return mSelectedSkillNames;
    }

    static class ViewHolder {
        @BindView(R.id.chase_listitem_skill_select_linearlayout_container)
        LinearLayout linearLayoutContainer;

        @BindView(R.id.chase_listitem_skill_select_textview_name)
        TextView textViewSkillName;

        @BindView(R.id.chase_listitem_skill_select_textview_cost)
        TextView textViewSkillCost;

        @BindView(R.id.chase_listitem_skill_select_textview_cooldown)
        TextView textViewCooldown;

        @BindView(R.id.chase_listitem_skill_select_textview_description)
        TextView textViewDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
