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
import me.instabattle.app.models.Entry;
import me.instabattle.app.R;
import me.instabattle.app.settings.State;
import me.instabattle.app.activities.BattleActivity;
import me.instabattle.app.activities.MapActivity;
import me.instabattle.app.activities.MyProfileActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEntryListAdapter extends BaseAdapter {

    private static final String TAG = "UserEntryListManager";

    private List<Entry> entries;
    private Context context;
    private LayoutInflater inflater;

    public UserEntryListAdapter(Context context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View res = convertView != null ? convertView :
                inflater.inflate(R.layout.user_entry_list_item, parent, false);

        Entry entry = entries.get(position);

        entry.getPhotoAndDo(new BitmapCallback() {
            @Override
            public void onResponse(final Bitmap photo) {
                ((Activity)context).runOnUiThread(() -> ((ImageView) res.findViewById(R.id.userEntryListItemImage)).setImageBitmap(photo));
            }

            @Override
            public void onFailure(Exception e) {
                //TODO
                Log.e(TAG, "Can't get entry photo");
            }
        });

        ((TextView) res.findViewById(R.id.userEntryListItemDate)).setText(
                (new SimpleDateFormat("dd/MM/yyyy", Locale.US)).format(entry.getCreatedOn()));
        ((TextView) res.findViewById(R.id.userEntryListItemUpvotes)).setText(entry.getRating() + " points");

        entry.getBattleAndDo(new Callback<Battle>() {
            @Override
            public void onResponse(Call<Battle> call, Response<Battle> response) {
                final Battle battle = response.body();

                ((TextView) res.findViewById(R.id.userEntryListItemTitle)).setText(battle.getName());

                res.findViewById(R.id.userEntryListItemViewBtn).setOnClickListener(v -> {
                    State.chosenBattle = battle;
                    BattleActivity.Companion.setGotHereFrom(MyProfileActivity.class);
                    Intent viewBattle = new Intent(context, BattleActivity.class);
                    context.startActivity(viewBattle);
                });

                res.findViewById(R.id.userEntryListItemViewOnMapBtn).setOnClickListener(v -> {
                    MapActivity.viewPoint = battle.getLocation();
                    MapActivity.viewZoom = MapActivity.DEFAULT_ZOOM;
                    MapActivity.gotHereFrom = MyProfileActivity.class;
                    Intent viewMap = new Intent(context, MapActivity.class);
                    context.startActivity(viewMap);
                });
            }

            @Override
            public void onFailure(Call<Battle> call, Throwable t) {
                Log.e(TAG, "cant get entries battle: " + t);
                //TODO
            }
        });
        return res;
    }
}
