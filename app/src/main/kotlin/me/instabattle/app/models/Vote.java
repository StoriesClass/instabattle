package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Vote {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_on")
    @Expose
    private Date createdOn;
    @SerializedName("voter_id")
    @Expose
    private Integer voterId;
    @SerializedName("winner_id")
    @Expose
    private Integer winnerId;
    @SerializedName("loser_id")
    @Expose
    private Integer loserId;
    @SerializedName("battle_id")
    @Expose
    private Integer battleId;

    public Vote(Integer voterId, Integer winnerId, Integer loserId) {
        this.voterId = voterId;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public Integer getBattleId() {
        return battleId;
    }
}
