package me.instabattle.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.instabattle.app.Battle;
import me.instabattle.app.Entry;
import me.instabattle.app.R;

/**
 * Created by wackloner on 28.11.2016.
 */

public class BattleListAdapter extends BaseAdapter {
    private List<Battle> battles;
    private Context context;
    private LayoutInflater inflater;

    public BattleListAdapter(Context context, List<Battle> battles) {
        this.context = context;
        this.battles = battles;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return battles.size();
    }

    @Override
    public Object getItem(int position) {
        return battles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View res = convertView;
        if (res == null) {
            res = inflater.inflate(R.layout.battle_list_item, parent, false);
        }
        Battle battle = battles.get(position);

        ((ImageView) res.findViewById(R.id.battleListItemImage)).setImageBitmap(battle.getWinner().getPhoto());
        ((TextView) res.findViewById(R.id.battleListItemTitle)).setText(battle.getName());
        ((TextView) res.findViewById(R.id.battleListItemDate)).setText("Created on 28.11.16");
        ((TextView) res.findViewById(R.id.battleListItemCount)).setText(battle.getEntriesCount() + " photos");
        return res;
    }
}
