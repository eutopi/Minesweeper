package us.ait.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import us.ait.minesweeper.model.MinesweeperModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.extras.containsKey(MinesweeperTitleScreen.KEY_MINE_NUM)) {
            val mines = intent.getStringExtra(MinesweeperTitleScreen.KEY_MINE_NUM).toInt()
            MinesweeperModel.NUM_MINES = mines
            minesweeperView.resetGame()
        }

        restartBtn.setOnClickListener {
            minesweeperView.resetGame()
        }

        toggleBtn.setOnCheckedChangeListener { _, isChecked ->
            minesweeperView.toggleChecked = isChecked
        }

        homeBtn.setOnClickListener {
            minesweeperView.resetGame()
            finish()
        }
    }

    override fun onBackPressed() {
        minesweeperView.resetGame()
        super.onBackPressed()
    }
}
