package com.github.juanmougan.covidbattleship

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

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
    private lateinit var currentGameId: UUID
    private val clipboardHandler: ClipboardHandler = ClipboardHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_in_progress)
        val extras = intent.extras
        val gson = Gson()
        val playerExtraContent = resolveCurrentPlayer(extras)
        val (id, name, board, shots) = gson.fromJson(
            playerExtraContent,
            Player::class.java
        )
        currentGameId = UUID.fromString(extras.getString(MainActivity.CURRENT_GAME_ID))
        maybeCreateShareableLink(extras)
        boardStatus = board!!
        shotsStatus = shots!!
        val playerBoardBanner = findViewById<TextView>(R.id.player_board_banner)
        playerBoardBanner.text = getString(R.string.player_banner_text, name)
        addListeners()
    }

    private fun maybeCreateShareableLink(extras: Bundle?) {
        val shareableLinkUrl: String? = extras?.getString(MainActivity.SHAREABLE_LINK_EXTRA)
        if (shareableLinkUrl != null) {
            shareableLinkUrl?.let {
                fillShareableLink(shareableLinkUrl)
                copyToClipboardAndNotify(shareableLinkUrl)
            }
        } else {
            destroyShareLayout()
        }
    }

    private fun resolveCurrentPlayer(extras: Bundle?): String {
        val playerOneExtra = extras?.getString(MainActivity.PLAYER_ONE_EXTRA)
        val playerTwoExtra = extras?.getString(MainActivity.PLAYER_TWO_EXTRA)
        if (playerOneExtra != null) {
            return playerOneExtra
        }
        if (playerTwoExtra != null) {
            return playerTwoExtra
        }
        throw RuntimeException("Didn't get info for player 1 and player 2")
    }

    private fun copyToClipboardAndNotify(shareableLinkUrl: CharSequence) {
        clipboardHandler.copy(shareableLinkUrl)
        Toast.makeText(this, R.string.copied_shareable_link_to_clipboard, Toast.LENGTH_LONG).show()
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
        val link = findViewById<EditText>(R.id.shareable_link) as TextView
        copyToClipboardAndNotify(link.text)
    }

    @SuppressLint("CheckResult")
    fun refreshGameStatus(view: View) {
        val observable = GameApiService.create().getGameStatus(currentGameId)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ gameResponse ->
                destroyShareLayoutIfNotNeeded(gameResponse)
            }, { error ->
                Toast.makeText(
                    this,
                    "Error getting game status: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            })
    }

    private fun destroyShareLayoutIfNotNeeded(gameResponse: GameStatusResponse) {
        if (gameResponse.status == GameStatus.READY) {
            destroyShareLayout()
        }
    }

    private fun destroyShareLayout() {
        val shareLayout = findViewById<RelativeLayout>(R.id.share_game_layout)
        (shareLayout.parent as ViewGroup).removeView(shareLayout)
    }
}
