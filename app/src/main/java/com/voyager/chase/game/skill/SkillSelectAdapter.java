package com.voyager.chase.game.skill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public SkillSelectAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mSkillSelectList = new ArrayList<>();
    }

    public void setSkillSelectList(ArrayList<SkillSelect> skillSelectList) {
        if (skillSelectList != null) {
            mSkillSelectList = skillSelectList;
        } else {
            Timber.e("Cannot set skill selection items to null list");
        }
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

        holder.textViewSkillName.setText(skillSelect.getName());
        holder.textViewSkillCost.setText(skillSelect.getSkillCost() + " SP");
        String cooldownTurnsString = skillSelect.getCooldownTurns() <= 0 ? "NONE" : skillSelect.getCooldownTurns() + " Turns";
        holder.textViewCooldown.setText(cooldownTurnsString);
        holder.textViewDescription.setText(skillSelect.getDescription());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toggleF(view);
            }

            private void toggleSkillSelectHighlight(View view) {
                if (skillSelect.isSelected()) {
                    skillSelect.setSelected(false);
                } else {
                    skillSelect.set
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.chase_listitem_skill_select_name)
        TextView textViewSkillName;

        @BindView(R.id.chase_listitem_skill_select_cost)
        TextView textViewSkillCost;

        @BindView(R.id.chase_listitem_skill_select_cooldown)
        TextView textViewCooldown;

        @BindView(R.id.chase_listitem_skill_select_description)
        TextView textViewDescription;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
