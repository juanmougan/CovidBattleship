package com.github.juanmougan.covidbattleship

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft

class MainActivity : AppCompatActivity() {

    companion object {
        const val BOARD_ROW_NUMBER = 10
        const val BOARD_COL_NUMBER_PER_ROW = 10
    }

    private lateinit var playerBoard: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun fillBoardWithTheSea() {
        playerBoard = findViewById(R.id.player_board)
        for (rows in 0 until BOARD_ROW_NUMBER) {
            val rowId = "row_$rows"
            createBoardRow(rowId)
            for (cols in 0 until BOARD_COL_NUMBER_PER_ROW) {
                val colId = "col_$rows$cols"
            }
        }
    }

    private fun createBoardRow(rowId: String) {
        val row = TableRow(this)
//        row.id = rowId
        row.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT, 0
        )
        row.weightSum = 1F
    }
}
