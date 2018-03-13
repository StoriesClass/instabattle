package me.instabattle.app.managers;

import org.junit.Test;

import me.instabattle.app.models.Battle;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

public class BattleManagerTest {
    @Test
    public void getAllAndDo() throws Exception {

    }

    @Test
    public void getAndDo() throws Exception {
        final Integer id = 1;
        BattleManager.getAndDo(id, new TestCallback<Battle>() {
            @Override
            public void onResponse(Call<Battle> call, Response<Battle> response) {
                assertEquals((Integer)1, response.body().getId());
            }
        });
    }

    @Test
    public void getInRadiusAndDo() throws Exception {

    }

    @Test
    public void createAndDo() throws Exception {
        final Integer authorId = 1;
        final String name = "Nice battle";
        final String description = "Very nice indeed";
        final Double latitude = 13d;
        final Double longitude = 37d;
        final Integer radius = 100;
        BattleManager.createAndDo(authorId, name, latitude, longitude, description, radius,
                new TestCallback<Battle>() {
            @Override
            public void onResponse(Call<Battle> call, Response<Battle> response) {
                Battle battle = response.body();
                assertEquals(name, battle.getName());
                assertEquals(latitude, (Double)battle.getLocation().latitude);
            }
        });
    }

    @Test
    public void updateAndDo() throws Exception {

    }

}