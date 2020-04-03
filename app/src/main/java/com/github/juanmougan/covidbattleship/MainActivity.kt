package com.github.juanmougan.covidbattleship

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val BOARD_ROW_NUMBER = 10
        const val BOARD_COL_NUMBER_PER_ROW = 10
        const val BOARD_ID_TEMPLATE = "cell_%d_%d_btn"
    }

    private lateinit var playerBoard: TableLayout
    private var boardStatus: Array<Array<CellStatus>> = Array(BOARD_ROW_NUMBER) {
        Array(BOARD_COL_NUMBER_PER_ROW) { CellStatus.SEA }
    }
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addListenersToAllButtons()
    }

    fun createGame(view: View) {
        val playerOneText = this.findViewById<EditText>(R.id.player_1_name)
        val playerOneName = playerOneText.text.toString()
        // TODO submit Game (POST on Retrofit)

        // TODO if POST is 201, transition to a new Activity: example here
        // TODO think about serializing the board to re-draw it in the next Activity
        // TODO the other possibility is to retrieve it with another network call
        // val intent = Intent(this, DisplayMessageActivity::class.java).apply {
        //            putExtra(EXTRA_MESSAGE, message)
        //        }
        //        startActivity(intent)

        Toast.makeText(this, "Got this: $playerOneName", Toast.LENGTH_LONG).show()
    }

    private fun addListenersToAllButtons() {
        val res: Resources = resources
        playerBoard = findViewById(R.id.player_board)
        for (rows in 0 until BOARD_ROW_NUMBER) {
            for (cols in 0 until BOARD_COL_NUMBER_PER_ROW) {
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
            boardStatus[clickedCell.coordinates.first][clickedCell.coordinates.second] = clickedCell.status
        }
    }

}
