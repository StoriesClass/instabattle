package me.instabattle.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.instabattle.app.managers.BitmapCallback;
import me.instabattle.app.models.Battle;
import me.instabattle.app.R;
import me.instabattle.app.models.User;
import me.instabattle.app.settings.State;
import me.instabattle.app.activities.BattleActivity;
import me.instabattle.app.activities.BattleListActivity;
import me.instabattle.app.activities.MapActivity;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BattleListAdapter extends BaseAdapter {

    private static final String TAG = "BattleListAdapter";

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
        final View res = convertView != null ? convertView :
                inflater.inflate(R.layout.battle_list_item, parent, false);

        final ImageView battleListItemImage = res.findViewById(R.id.battleListItemImage);
        final Battle battle = battles.get(position);

        if (battleListItemImage.getDrawable() == null) {
            battle.getWinnerAndDo(new Callback<List<Entry>>() {
                @Override
                public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                    //FIXME pls
                    if (response.isSuccessful()) {
                        List<Entry> top = response.body();
                        Entry winner = top.get(0);
                        if (winner != null) {
                            winner.getPhotoAndDo(new BitmapCallback() {
                                @Override
                                public void onResponse(final Bitmap photo) {
                                    ((Activity) context).runOnUiThread(() -> battleListItemImage.setImageBitmap(photo));
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    // FIXME
                                    Log.e(TAG, "Can't get winner's photo");
                                }
                            });
                        }

                    } else {
                        Log.e(TAG, "winner entry is null");
                    }
                }

                @Override
                public void onFailure(Call<List<Entry>> call, Throwable t) {
                    //TODO
                    Log.e(TAG, "cant get entry: " + t);
                }
            });
        }

        ((TextView) res.findViewById(R.id.battleListItemTitle)).setText(battle.getName());
        ((TextView) res.findViewById(R.id.battleListItemDate)).setText(
                (new SimpleDateFormat("dd/MM/yyyy", Locale.US)).format(battle.getCreatedOn()));
        ((TextView) res.findViewById(R.id.battleListItemCount)).setText(battle.getEntriesCount() + " photos");

        res.findViewById(R.id.battleListItemViewBtn).setOnClickListener(v -> {
            State.chosenBattle = battle;
            BattleActivity.Companion.setGotHereFrom(BattleListActivity.class);
            Intent viewBattle = new Intent(context, BattleActivity.class);
            context.startActivity(viewBattle);
        });
        res.findViewById(R.id.battleListItemViewOnMapBtn).setOnClickListener(v -> {
            MapActivity.viewPoint = battle.getLocation();
            MapActivity.viewZoom = MapActivity.DEFAULT_ZOOM;
            MapActivity.gotHereFrom = BattleListActivity.class;
            Intent viewMap = new Intent(context, MapActivity.class);
            context.startActivity(viewMap);
        });

        battle.getAuthorAndDo(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ((TextView) res.findViewById(R.id.battleListItemAuthor)).setText("by " + response.body().getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get battle author", t);
            }
        });
        return res;
    }
}
