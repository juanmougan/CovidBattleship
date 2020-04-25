package com.github.juanmougan.covidbattleship

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Player2JoinedActivity : AppCompatActivity() {

    private lateinit var playerBoard: TableLayout
    private var boardStatus: Array<Array<CellStatus>> = Array(MainActivity.BOARD_ROW_NUMBER) {
        Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
    }

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

    @SuppressLint("CheckResult")
    fun joinGame(view: View) {
        // TODO PATCH to games/id with player2_name and board
        val playerTwoText = this.findViewById<EditText>(R.id.player_2_name)
        val playerTwoName = playerTwoText.text.toString()
        val observable = GameApiService.create().joinGame(GameRequest(playerTwoName, boardStatus))
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ gameResponse ->
                // TODO hide loading bar
                // TODO maybe not the best approach, what about https://square.github.io/otto/
                val gson = Gson()
                val playerTwo = gson.toJson(gameResponse.player2)
                val intent = Intent(this, GameInProgressActivity::class.java).apply {
                    putExtra(MainActivity.PLAYER_TWO_EXTRA, playerTwo)
                    putExtra(MainActivity.CURRENT_GAME_ID, gameResponse.id.toString())
                }
                startActivity(intent)
            }, { error ->
                // TODO hide loading bar
                Toast.makeText(
                    this,
                    "Error creating game: ${error.message.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
            )
    }
}
