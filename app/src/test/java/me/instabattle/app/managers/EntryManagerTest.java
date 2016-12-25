package me.instabattle.app.managers;

import org.junit.Test;

import java.util.List;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
import me.instabattle.app.models.Vote;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

public class EntryManagerTest {
    @Test
    public void getByBattleAndDo() throws Exception {

    }

    @Test
    public void getByUserAndDo() throws Exception {

    }

    @Test
    public void getAndDo() throws Exception {
        final Integer id = 1;
        EntryManager.getAndDo(id, new TestCallback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                assertEquals((Integer)1, response.body().getId());
            }
        });
    }

    /*
    @Test
    public void createAndDo() throws Exception {
        final Integer battleId = 1;
        final Integer authorId = 1;

        EntryManager.createAndDo(battleId, authorId,
                new TestCallback<Entry>() {
                    @Override
                    public void onResponse(Call<Entry> call, Response<Entry> response) {
                        Entry entry = response.body();
                        assertEquals(battleId, entry.getBattleId());
                        assertEquals(authorId, entry.getAuhtorId());
                    }
                });

    }
    */

    @Test
    public void getEntriesAndDo() throws Exception {

    }

    @Test
    public void getVotingAndDo() throws Exception {
        final Integer battleId = 1;
        EntryManager.getVoteAndDo(battleId, new TestCallback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                assertEquals((Integer)1, response.body().get(0).getBattleId());
            }
        });
    }

    @Test
    public void voteAndDo() throws Exception {
        // FIXME rating change
        final Integer battleId = 1;
        final Integer voterId = 1;
        EntryManager.getVoteAndDo(battleId, new TestCallback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                List<Entry> entries = response.body();
                final Integer entry1_id = entries.get(0).getId();
                Integer entry2_id = entries.get(1).getId();

                EntryManager.voteAndDo(battleId, voterId, entry1_id, entry2_id, new TestCallback<Vote>() {
                    @Override
                    public void onResponse(Call<Vote> call, Response<Vote> response) {
                        Vote vote = response.body();
                        assertEquals(battleId, vote.getBattleId());
                    }
                });
            }
        });
    }
}