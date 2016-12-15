package me.instabattle.app.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.instabattle.app.R;
import me.instabattle.app.settings.State;
import me.instabattle.app.activities.BattleActivity;
import me.instabattle.app.activities.VoteActivity;

public class VotingEndDialog extends DialogFragment implements View.OnClickListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Nice vote, " + State.currentUser.getUsername() + "!");
        View v = inflater.inflate(R.layout.fragment_voting_end_dialog, container);
        v.findViewById(R.id.voteAgainBtn).setOnClickListener(this);
        v.findViewById(R.id.goToBattleBtn).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.voteAgainBtn) {
            VoteActivity parent = (VoteActivity) getActivity();
            parent.initNewVoting();
            dismiss();
        } else {
            Intent battle = new Intent(getActivity(), BattleActivity.class);
            startActivity(battle);
        }
    }
}
