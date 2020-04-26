package com.github.juanmougan.covidbattleship

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class Player2JoinedActivity : AppCompatActivity() {

    companion object {
        const val BOARD_ID_TEMPLATE = "player2_cell_%d_%d_btn"
    }

    private lateinit var playerBoard: TableLayout
    private var boardStatus: Array<Array<CellStatus>> = Array(MainActivity.BOARD_ROW_NUMBER) {
        Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
    }
    private lateinit var gameId: UUID

    // TODO this is almost the same as MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2_joined)
        val data: Uri? = intent?.data
        Log.d("Player2JoinedActivity", "data: $data")
        val paramAndValue = data?.path?.removePrefix("/")?.split("/")
        gameId = UUID.fromString(paramAndValue?.get(1))
        Toast.makeText(
            this,
            "Player 2 joined game with id: $gameId",
            Toast.LENGTH_LONG
        ).show()
        addListenersToAllButtons()
    }

    private fun addListenersToAllButtons() {
        val res: Resources = resources
        playerBoard = findViewById(R.id.player2_board)
        for (rows in 0 until MainActivity.BOARD_ROW_NUMBER) {
            for (cols in 0 until MainActivity.BOARD_COL_NUMBER_PER_ROW) {
                val cellIdentifier = String.format(BOARD_ID_TEMPLATE, rows, cols)
                val cellId = res.getIdentifier(cellIdentifier, "id", this.packageName)
                val cellButton = findViewById<ImageButton>(cellId)
                cellButton.tag = Cell(Pair(rows, cols), boardStatus[rows][cols])
                addListenerToButton(cellButton)
            }
        }
    }

    private fun addListenerToButton(cellButton: ImageButton?) {
        // TODO extract methods in this listener, this is a mess :(
        cellButton?.setOnClickListener { b ->
            val button: ImageButton = b as ImageButton
            val clickedCell = button.tag as Cell
            val currentStatus = clickedCell.status
            val newStatus = currentStatus.toggle()
            // TODO this is deprecated, but I'm not using any theme :shrug
            button.setImageDrawable(resources.getDrawable(newStatus.imageId))
            clickedCell.status = newStatus
            button.tag = clickedCell
            boardStatus[clickedCell.coordinates.first][clickedCell.coordinates.second] =
                clickedCell.status
        }
    }

    @SuppressLint("CheckResult")
    fun joinGame(view: View) {
        // TODO PATCH to games/id with player2_name and board
        val playerTwoText = this.findViewById<EditText>(R.id.player_2_name)
        val playerTwoName = playerTwoText.text.toString()
        val observable =
            GameApiService.create().joinGame(gameId, GameRequest(playerTwoName, boardStatus))
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
