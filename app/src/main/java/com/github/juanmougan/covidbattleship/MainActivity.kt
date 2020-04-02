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
//        addCreateGameListener()
    }

    fun createGame(view: View) {
        //val editText = findViewById<EditText>(R.id.editText)
        //        val message = editText.text.toString()
        val playerOneText = this.findViewById<EditText>(R.id.player_1_name)
        val playerOneName = playerOneText.text.toString()
        // TODO submit Game (POST on Retrofit)

        // TODO if POST is 201, transition to a new Activity: example here
        // val intent = Intent(this, DisplayMessageActivity::class.java).apply {
        //            putExtra(EXTRA_MESSAGE, message)
        //        }
        //        startActivity(intent)

        Toast.makeText(this, "Got this: $playerOneName", Toast.LENGTH_LONG).show()
    }

//    private fun addCreateGameListener() {
//        playerOneText = findViewById(R.id.player_1_name)
//        submitButton = findViewById(R.id.create_game_submit)
//        submitButton.setOnClickListener {b ->
//            val playerOneName = playerOneText.text.toString()
//
//        }
//    }

    private fun addListenersToAllButtons() {
        val res: Resources = resources
        playerBoard = findViewById(R.id.player_board)
        for (rows in 0 until BOARD_ROW_NUMBER) {
            for (cols in 0 until BOARD_COL_NUMBER_PER_ROW) {
                val cellIdentifier = String.format(BOARD_ID_TEMPLATE, rows, cols)
                val cellId = res.getIdentifier(cellIdentifier, "id", this.packageName)
                val cellButton = findViewById<ImageButton>(cellId)
                cellButton.tag = boardStatus[rows][cols]
                addListenerToButton(cellButton)
            }
        }
    }

    private fun addListenerToButton(cellButton: ImageButton?) {
        cellButton?.setOnClickListener { b ->
            val button: ImageButton = b as ImageButton
            val currentStatus: CellStatus = button.tag as CellStatus
            val newStatus = currentStatus.toggle()
            // TODO this is deprecated, but I'm not using any theme :shrug
            button.setImageDrawable(resources.getDrawable(newStatus.imageId))
            button.tag = newStatus
        }
    }

}
