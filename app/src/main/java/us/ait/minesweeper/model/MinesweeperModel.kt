package us.ait.minesweeper.model

data class Field(var type: Int, var minesAround: Int,
                 var isFlagged: Boolean, var wasClicked: Boolean)

object MinesweeperModel {

    private val DIM = 5
    public var NUM_MINES = 3
    private var model = Array(DIM) { Array(DIM) { Field(0,0,false,false) } }
    public var won = false

    init {
        setMines()
    }

    public fun flag(x: Int, y: Int) {
        model[x][y].isFlagged = true
    }

    public fun click(x: Int, y: Int) {
        model[x][y].wasClicked = true
    }

    public fun getField(x: Int, y: Int) = model[x][y]

    public fun resetModel() {
        won = false
        for (i in 0 until DIM) {
            for (j in 0 until DIM) {
                model[i][j] = Field(0,0,false,false)
            }
        }
        setMines()
    }

    private fun setMines() {
        for (i in 1..NUM_MINES) {
            var x = (0 until DIM).random()
            var y = (0 until DIM).random()
            while (model[x][y].type == 1) {
                x = (0 until DIM).random()
                y = (0 until DIM).random()
            }
            model[x][y].type = 1
            setMineNumAround(x, y)
        }
    }

    private fun setMineNumAround(x: Int, y: Int) {
        if (x-1 in 0..4 && y-1 in 0..4) model[x-1][y-1].minesAround += 1
        if (x in 0..4 && y-1 in 0..4) model[x][y-1].minesAround += 1
        if (x+1 in 0..4 && y-1 in 0..4) model[x+1][y-1].minesAround += 1
        if (x-1 in 0..4 && y in 0..4) model[x-1][y].minesAround += 1
        if (x+1 in 0..4 && y in 0..4) model[x+1][y].minesAround += 1
        if (x-1 in 0..4 && y+1 in 0..4) model[x-1][y+1].minesAround += 1
        if (x in 0..4 && y+1 in 0..4) model[x][y+1].minesAround += 1
        if (x+1 in 0..4 && y+1 in 0..4) model[x+1][y+1].minesAround += 1
    }

    public fun checkWin() {
        var count = 0
        for (i in 0 until DIM) {
            for (j in 0 until DIM) {
                if ((model[i][j].type == 1) and model[i][j].isFlagged) count += 1
            }
        }
        if (count == NUM_MINES) won = true
    }

}