package me.instabattle.app.dialogs

import android.os.Bundle
import android.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.instabattle.app.R
import me.instabattle.app.settings.State
import me.instabattle.app.activities.BattleActivity
import me.instabattle.app.activities.VoteActivity
import org.jetbrains.anko.startActivity

class VotingEndDialog : DialogFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        dialog.setTitle("Nice vote, " + State.currentUser.username + "!")
        val v = inflater.inflate(R.layout.fragment_voting_end_dialog, container)
        v.findViewById<View>(R.id.voteAgainBtn).setOnClickListener(this)
        v.findViewById<View>(R.id.goToBattleBtn).setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        if (v.id == R.id.voteAgainBtn) {
            val parent = activity as VoteActivity
            parent.setPhotos()
            dismiss()
        } else {
            startActivity<BattleActivity>()
        }
    }
}
