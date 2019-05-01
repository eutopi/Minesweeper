package us.ait.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_minesweeper_title_screen.*

class MinesweeperTitleScreen : AppCompatActivity() {

    companion object {
        public val KEY_MINE_NUM = "KEY_MINE_NUM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minesweeper_title_screen)

        startBtn.setOnClickListener {
            var intentMineNum = Intent()
            intentMineNum.setClass(this@MinesweeperTitleScreen, MainActivity::class.java)
            intentMineNum.putExtra(KEY_MINE_NUM, mineSlider.value.toString())
            startActivity(intentMineNum)
        }

    }
}
