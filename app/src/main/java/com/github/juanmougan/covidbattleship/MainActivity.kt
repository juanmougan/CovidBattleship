package com.github.juanmougan.covidbattleship

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val BOARD_ROW_NUMBER = 10
        const val BOARD_COL_NUMBER_PER_ROW = 10
        const val BOARD_ID_TEMPLATE = "cell_%d_%d_btn"
    }

    private lateinit var playerBoard: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addListenersToAllButtons()
    }

    fun addListenersToAllButtons() {
        val res: Resources = resources
        playerBoard = findViewById(R.id.player_board)
        for (rows in 0 until BOARD_ROW_NUMBER) {
            val rowId = "row_$rows"
            for (cols in 0 until BOARD_COL_NUMBER_PER_ROW) {
                val cellIdentifier = String.format(BOARD_ID_TEMPLATE, rows, cols)
                val cellId = res.getIdentifier(cellIdentifier, "id", this.packageName)
                val cellButton = findViewById<ImageButton>(cellId)
                addListenerToButton(cellButton)
            }
        }
    }

    // TODO logic should be
    // 1. onClick, toggle status in the matrix (Sea to Ship or vice versa)
    // 2. If status == Sea set blue drawable, else set grey drawable
    private fun addListenerToButton(cellButton: ImageButton?) {
        cellButton?.setOnClickListener(View.OnClickListener { b ->
            val button: ImageButton = b as ImageButton
            // TODO this is deprecated, but I'm not using any theme :shrug
            button.setImageDrawable(resources.getDrawable(R.drawable.grey_square_small))
        })
    }

}
