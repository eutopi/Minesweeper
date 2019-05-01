package us.ait.minesweeper.view

import android.content.Context
import android.graphics.*
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import us.ait.minesweeper.model.MinesweeperModel
import us.ait.minesweeper.R

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val CONTINUE : Short = 0
    private val END : Short = 1
    private val DIM = 5
    private val XOFFSET = 50
    private val YOFFSET = 100
    private val BITOFFSET = 30


    private val paintBackground = Paint()
    private val redBackground = Paint()
    private val whiteLine = Paint()
    private val paintText = Paint()
    private var bitmapBomb = BitmapFactory.decodeResource(resources, R.drawable.bomb)
    private var bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
    private var gameStatus = CONTINUE

    public var toggleChecked = false

    init {
        paintBackground.color = Color.LTGRAY
        paintBackground.style = Paint.Style.FILL
        redBackground.color = Color.RED
        redBackground.style = Paint.Style.FILL

        whiteLine.color = Color.WHITE
        whiteLine.style = Paint.Style.STROKE
        whiteLine.strokeWidth = 8f

        paintText.color = Color.BLACK
        paintText.textSize = 80f
        resetGame()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapBomb = Bitmap.createScaledBitmap(
            bitmapBomb, width/DIM-BITOFFSET, height/DIM-BITOFFSET, false
        )
        bitmapFlag = Bitmap.createScaledBitmap(
            bitmapFlag, width/DIM-BITOFFSET, height/DIM-BITOFFSET, false
        )
    }

    override fun onDraw (canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        drawGameBoard(canvas)
        drawPlays(canvas)
    }

    private fun drawGameBoard(canvas: Canvas?) {
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), whiteLine)
        for (i in 1 until DIM) {
            canvas?.drawLine(0f, (i * height / DIM).toFloat(),
                width.toFloat(), (i * height / DIM).toFloat(), whiteLine)
            canvas?.drawLine((i * width / DIM).toFloat(), 0f,
                (i * width / DIM).toFloat(), height.toFloat(), whiteLine)
        }
    }

    private fun drawPlays(canvas: Canvas?) {
        for (i in 0 until DIM) {
            for (j in 0 until DIM) {
                val field = MinesweeperModel.getField(i, j)
                when {
                    !field.wasClicked and field.isFlagged -> drawFlag(canvas, i, j)
                    field.wasClicked and (field.type == 0) -> drawMinesAround(canvas, field.minesAround, i, j)
                    field.wasClicked and (field.type == 1) -> drawMine(canvas, i, j)
                }
            }
        }
    }

    private fun drawFlag(canvas: Canvas?, i: Int, j: Int) {
        if (MinesweeperModel.getField(i, j).type == 0) {
            canvas?.drawRect(i*width/DIM.toFloat(), j*height/DIM.toFloat(),
                (i+1)*width/DIM.toFloat(), (j+1)*height/DIM.toFloat(), redBackground)
            val msg = context.getString(R.string.game_msg_flag)
            Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
            gameStatus = END
        }
        canvas?.drawBitmap(bitmapFlag,
            i*width/DIM.toFloat() + BITOFFSET/2,
            j*height/DIM.toFloat() + BITOFFSET/2,
            null)
    }

    private fun drawMinesAround(canvas: Canvas?, bombs: Int, i: Int, j: Int) {
        canvas?.drawText(bombs.toString(),
            i * width / DIM.toFloat() + XOFFSET,
            j * height / DIM.toFloat() + YOFFSET,
            paintText)
    }

    private fun drawMine(canvas: Canvas?, i: Int, j: Int) {
        canvas?.drawRect(i*width/DIM.toFloat(), j*height/DIM.toFloat(),
            (i+1)*width/DIM.toFloat(), (j+1)*height/DIM.toFloat(), redBackground)
        canvas?.drawBitmap(bitmapBomb,
            i*width/DIM.toFloat() + BITOFFSET/2,
            j*height/DIM.toFloat() + BITOFFSET/2,
            null)
        val msg = context.getString(R.string.game_msg_mine)
        Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
        gameStatus = END
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / DIM)
            val tY = event.y.toInt() / (height / DIM)
            if (tX < DIM && tY < DIM && gameStatus == CONTINUE) {
                if (toggleChecked) {
                    MinesweeperModel.flag(tX, tY)
                }
                else if (!toggleChecked and !MinesweeperModel.getField(tX, tY).isFlagged) {
                    MinesweeperModel.click(tX, tY)
                }
                checkGameStatus()
                invalidate()
            }
        }
        return true
    }

    public fun checkGameStatus() {
        MinesweeperModel.checkWin()
        if (MinesweeperModel.won) {
            val msg = context.getString(R.string.game_msg_win)
            Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
            gameStatus = END
        }
    }

    public fun resetGame() {
        MinesweeperModel.resetModel()
        gameStatus = CONTINUE
        invalidate()
    }
}