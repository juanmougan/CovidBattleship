package com.github.juanmougan.covidbattleship

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Player2JoinedActivity : AppCompatActivity() {

    // TODO this is almost the same as MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2_joined)
        val data: Uri? = intent?.data
        Log.d("Player2JoinedActivity", "data: $data")
        val paramAndValue = data?.path?.removePrefix("/")?.split("/")
        Toast.makeText(
            this,
            "Player 2 joined game with id: ${paramAndValue?.get(1)}",
            Toast.LENGTH_LONG
        ).show()
    }
}
