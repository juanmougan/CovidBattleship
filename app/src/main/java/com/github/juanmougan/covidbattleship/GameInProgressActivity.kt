package com.github.juanmougan.covidbattleship

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class GameInProgressActivity : AppCompatActivity() {

    private var boardStatus: Array<Array<CellStatus>> = Array(MainActivity.BOARD_ROW_NUMBER) {
        Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
    }

    private var shotsStatus: Array<Array<CellStatus>> = Array(MainActivity.BOARD_ROW_NUMBER) {
        Array(MainActivity.BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
    }

    private lateinit var playerBoard: TableLayout
    private lateinit var shotsBoard: TableLayout
    private lateinit var shareableLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_in_progress)
        val extras = intent.extras
        val gson = Gson()
        val (id, name, board, shots) = gson.fromJson(
            extras.getString(MainActivity.PLAYER_ONE_EXTRA),
            Player::class.java
        )
        val shareableLinkUrl = extras.getString(MainActivity.SHAREABLE_LINK_EXTRA)!!
        boardStatus = board!!
        shotsStatus = shots!!
        fillShareableLink(shareableLinkUrl)
        addListeners()
        // TODO copy shareableLik to clipboard and show a Toast
        // reuse fun copyLinkToClipboard(view: View)
        Toast.makeText(this, "Game created by: $name", Toast.LENGTH_LONG).show()
    }

    fun shoot(view: View) {

    }

    private fun fillShareableLink(shareableLinkUrl: String) {
        shareableLink = findViewById(R.id.shareable_link)
        shareableLink.setText(shareableLinkUrl)
    }

    private fun addListeners() {
        // TODO I don't need these two lines
        playerBoard = findViewById(R.id.game_player_board)
        shotsBoard = findViewById(R.id.game_player_shots)
        addListenersToAllButtons(MainActivity.GAME_BOARD_ID_TEMPLATE)
        addListenersToAllButtons(MainActivity.GAME_SHOTS_ID_TEMPLATE)
    }

    // TODO this is duplicated in the two Activities
    private fun addListenersToAllButtons(idTemplate: String) {
        val res: Resources = resources
        for (rows in 0 until MainActivity.BOARD_ROW_NUMBER) {
            for (cols in 0 until MainActivity.BOARD_COL_NUMBER_PER_ROW) {
                val cellIdentifier = String.format(idTemplate, rows, cols)
                // TODO add ids in the XML
                val cellId = res.getIdentifier(cellIdentifier, "id", this.packageName)
                val cellButton = findViewById<ImageButton>(cellId)
                // TODO if selected, apply grey
                cellButton.tag = Cell(Pair(rows, cols), boardStatus[rows][cols])
                cellButton.setImageDrawable(resources.getDrawable(boardStatus[rows][cols].imageId))
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

    fun copyLinkToClipboard(view: View) {
        // TODO copy to clipboard
        // TODO and then show a Toast
    }

    fun refreshGameStatus(view: View) {
        // TODO get game status
        // TODO if status is READY hide views
    }
}
