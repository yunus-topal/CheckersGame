package com.example.checkersgame

import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel

class GameViewModel (): ViewModel() {
    private var turnRed = true
    val boardButtons = mutableListOf<MutableList<ImageButton>>()
    var playerTurn: TextView? = null
    // 0 means empty, 1 means red, 2 means black button, 3 means yellow or blue block.
    private val boardStates = mutableListOf<MutableList<Int>>()
    private val kingStates = mutableListOf<MutableList<Boolean>>()
    private var prevClicked = mutableListOf(0,0)
    private var redButtonCount = 12
    private var blackButtonCount = 12
    private var redHasToEat = false
    private var blackHasToEat = false

    private val redWillEat = mutableListOf<MutableList<Int>>()
    private val blackWillEat = mutableListOf<MutableList<Int>>()

    //TODO
    // king button

    init {
        // initialize board states.
        boardStates.clear()
        boardStates.add(mutableListOf(0, 1, 0, 1, 0, 1, 0, 1))
        boardStates.add(mutableListOf(1, 0, 1, 0, 1, 0, 1, 0))
        boardStates.add(mutableListOf(0, 1, 0, 1, 0, 1, 0, 1))
        boardStates.add(MutableList(8) { _ ->  0})
        boardStates.add(MutableList(8) { _ ->  0})
        boardStates.add(mutableListOf(2, 0, 2, 0, 2, 0, 2, 0))
        boardStates.add(mutableListOf(0, 2, 0, 2, 0, 2, 0, 2))
        boardStates.add(mutableListOf(2, 0, 2, 0, 2, 0, 2, 0))

        // initialize king states.
        kingStates.clear()
        repeat(8){
            kingStates.add(MutableList(8) {_ -> false})
        }
    }

    fun clicked(i: Int, j:Int){
        if(boardStates[i][j] == 0){
            clear()
        }
        else if(boardStates[i][j] == 1){
            clear()
            if(turnRed){
                if(redHasToEat)
                    paintYellowEat(i, j)
                else
                    paintYellow(i, j)
            }
        }
        else if(boardStates[i][j] == 2){
            clear()
            if(!turnRed){
                if (blackHasToEat)
                    paintBlueEat(i, j)
                else
                    paintBlue(i, j)
            }
        }
        else if(boardStates[i][j] == 3){
            clear()
            moveButton(i, j)
        }

    }

    // red players potential moves without capturing enemy.
    // TODO
    // kings will move in 4 directions instead of 2.
    private fun paintYellow(i: Int, j:Int){
        // left, right and bottom. + - j, + i
        if(i < 7){
            if(j > 0){
                if(boardStates[i + 1][j - 1] == 0){
                    boardButtons[i + 1][j - 1].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 1][j - 1] = 3
                }
            }
            if(j < 7){
                if(boardStates[i + 1][j + 1] == 0){
                    boardButtons[i + 1][j + 1].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 1][j + 1] = 3
                }
            }
        }

        prevClicked[0] = i
        prevClicked[1] = j
    }

    // red players potential moves with capturing enemy.
    // this function uses recursive function below to get multiple captures.
    private fun paintYellowEat(i: Int, j:Int){
        redWillEat.clear()
        if(i < 6){
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j - 2] = 3

                    redWillEat.add((mutableListOf(i+2, j-2, 0)))
                    paintRecursiveYellow(i + 2, j - 2,0)
                }
            }
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j + 2] = 3

                    redWillEat.add((mutableListOf(i+2, j+2, 1)))
                    paintRecursiveYellow(i + 2, j + 2, redWillEat.size - 1)
                }
            }
        }
        prevClicked[0] = i
        prevClicked[1] = j
    }

    private fun paintRecursiveYellow(i: Int, j:Int, index:Int){
        if(i < 6){
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveLeft = (mutableListOf(i + 2, j - 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveLeft.add(0)
                    redWillEat.add(moveLeft)
                    paintRecursiveYellow(i + 2,j - 2, redWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveRight = (mutableListOf(i + 2, j + 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveRight.add(1)
                    redWillEat.add(moveRight)
                    paintRecursiveYellow(i + 2,j + 2, redWillEat.size - 1)
                }
            }
        }
    }

    // black players potential moves without capturing enemy.
    // TODO
    // kings will move in 4 directions instead of 2.
    private fun paintBlue(i: Int, j:Int){
        if(i > 0){
            if(j > 0){
                if(boardStates[i - 1][j - 1] == 0){
                    boardButtons[i - 1][j - 1].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 1][j - 1] = 3
                }
            }
            if(j < 7){
                if(boardStates[i - 1][j + 1] == 0){
                    boardButtons[i - 1][j + 1].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 1][j + 1] = 3
                }
            }
        }
        prevClicked[0] = i
        prevClicked[1] = j
    }

    // black players potential moves with capturing enemy.
    // this function uses recursive function below to get multiple captures.
    private fun paintBlueEat(i: Int, j:Int){
        if(i > 1){
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j - 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j-2, 0)))
                    paintRecursiveBlue(i - 2, j - 2,0)
                }
            }
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j + 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j+2, 1)))
                    paintRecursiveYellow(i - 2, j + 2, blackWillEat.size - 1)
                }
            }
        }
        prevClicked[0] = i
        prevClicked[1] = j
    }

    private fun paintRecursiveBlue(i: Int, j:Int, index:Int){
        if(i > 1){
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveLeft = (mutableListOf(i - 2, j - 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveLeft.add(0)
                    blackWillEat.add(moveLeft)
                    paintRecursiveBlue(i - 2,j - 2, blackWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveRight = (mutableListOf(i + 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveRight.add(1)
                    blackWillEat.add(moveRight)
                    paintRecursiveBlue(i - 2,j + 2, blackWillEat.size - 1)
                }
            }
        }
    }

    // move a button to the clicked location.
    // clear previous place and replace new place.
    // TODO
    // change corresponding indexes at kings array.
    private fun moveButton(i: Int, j:Int){
        val x = prevClicked[0]
        val y = prevClicked[1]
        if(turnRed){
            //clear previous place
            boardButtons[x][y].setImageResource(R.drawable.ic_white_button)
            boardStates[x][y] = 0
            //put new button
            boardButtons[i][j].setImageResource(R.drawable.ic_red_button)
            boardStates[i][j] = 1
            if(redHasToEat)
                moveButtonEat(x,y,i,j)

            blackHasToEat = checkBlackEat()
        }
        else{
            //clear previous place
            boardButtons[x][y].setImageResource(R.drawable.ic_white_button)
            boardStates[x][y] = 0
            //put new button
            boardButtons[i][j].setImageResource(R.drawable.ic_black_button)
            boardStates[i][j] = 2
            if(blackHasToEat)
                moveButtonEat(x,y,i,j)

            redHasToEat = checkRedEat()
        }
        turnRed = !turnRed
        if (turnRed)
            playerTurn!!.text = "Red Moves"
        else
            playerTurn!!.text = "Black Moves"
    }

    // delete eaten buttons after a button moved.
    // TODO
    // make corresponding index false in kings array.
    private fun moveButtonEat(x: Int, y: Int, i:Int, j:Int){

        if(turnRed){
            var redTarget = listOf(0,0)
            for(item in redWillEat){
                if(item[0] == i && item[1] == j){
                    redTarget = item
                    break
                }
            }
            var xx = x
            var yy = y
            for(i in 2 until redTarget.size){
                if(redTarget[i] == 0){
                    boardButtons[xx + 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy - 1] = 0
                    xx += 2
                    yy -= 2
                }
                else{
                    boardButtons[xx + 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy + 1] = 0
                    xx += 2
                    yy += 2
                }
                blackButtonCount--
            }

        }
        else{
            var blackTarget = listOf(0,0)
            for(item in blackWillEat){
                if(item[0] == i && item[1] == j){
                    blackTarget = item
                    break
                }
            }
            var xx = x
            var yy = y
            for(i in 2 until blackTarget.size){
                if(blackTarget[i] == 0){
                    boardButtons[xx - 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy - 1] = 0
                    xx -= 2
                    yy -= 2
                }
                else{
                    boardButtons[xx - 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy + 1] = 0
                    xx -= 2
                    yy += 2
                }
                redButtonCount--
            }
        }

    }

    // clear yellow or blue buttons
    private fun clear(){
        for(i in 0..7){
            for(j in 0..7){
                if(boardStates[i][j] == 3){
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)
                }
            }
        }

    }

    // check whether red player has to eat or not.
    // TODO
    // kings will have special check
    private fun checkRedEat(): Boolean{
        for(i in 0..5){
            if(boardStates[i][0] == 1) {
                if (boardStates[i + 2][2] == 0 && boardStates[i + 1][1] == 2) {
                    return true
                }
            }
            if(boardStates[i][1] == 1) {
                if (boardStates[i + 2][3] == 0 && boardStates[i + 1][2] == 2) {
                    return true
                }
            }
            for(j in 2..5){
                if(boardStates[i][j] == 1) {
                    if (boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2) {
                        return true
                    }
                    if (boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2) {
                        return true
                    }
                }
            }
            if(boardStates[i][6] == 1) {
                if (boardStates[i + 2][4] == 0 && boardStates[i + 1][3] == 2) {
                    return true
                }
            }
            if(boardStates[i][7] == 1) {
                if (boardStates[i + 2][5] == 0 && boardStates[i + 1][6] == 2) {
                    return true
                }
            }
        }
        return false
    }

    // check whether black player has to eat or not.
    // TODO
    // kings will have special check
    private fun checkBlackEat(): Boolean{
        for(i in 2..7){
            if(boardStates[i][0] == 2) {
                if (boardStates[i - 2][2] == 0 && boardStates[i - 1][1] == 1) {
                    return true
                }
            }
            if(boardStates[i][1] == 2) {
                if (boardStates[i - 2][3] == 0 && boardStates[i - 1][2] == 1) {
                    return true
                }
            }
            for(j in 2..5){
                if(boardStates[i][j] == 2) {
                    if (boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1) {
                        return true
                    }
                    if (boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1) {
                        return true
                    }
                }
            }
            if(boardStates[i][6] == 2) {
                if (boardStates[i - 2][4] == 0 && boardStates[i - 1][5] == 1) {
                    return true
                }
            }
            if(boardStates[i][7] == 2) {
                if (boardStates[i - 2][5] == 0 && boardStates[i - 1][6] == 1) {
                    return true
                }
            }
        }
        return false
    }

    // reinitialize the game.
    fun initializeGame(){
        // initialize board states.
        boardStates.clear()
        boardStates.add(mutableListOf(0, 1, 0, 1, 0, 1, 0, 1))
        boardStates.add(mutableListOf(1, 0, 1, 0, 1, 0, 1, 0))
        boardStates.add(mutableListOf(0, 1, 0, 1, 0, 1, 0, 1))
        boardStates.add(MutableList(8) { _ ->  0})
        boardStates.add(MutableList(8) { _ ->  0})
        boardStates.add(mutableListOf(2, 0, 2, 0, 2, 0, 2, 0))
        boardStates.add(mutableListOf(0, 2, 0, 2, 0, 2, 0, 2))
        boardStates.add(mutableListOf(2, 0, 2, 0, 2, 0, 2, 0))

        // initialize king states.
        kingStates.clear()
        repeat(8){
            kingStates.add(MutableList(8) {_ -> false})
        }

        // initialize button colors.
        for(i in 0..7){
            for(j in 0..7){
                if(boardStates[i][j] == 0)
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)
                else if(boardStates[i][j] == 1)
                    boardButtons[i][j].setImageResource(R.drawable.ic_red_button)
                else if(boardStates[i][j] == 2)
                    boardButtons[i][j].setImageResource(R.drawable.ic_black_button)
            }
        }

        // other variables.
        turnRed = true
        prevClicked = mutableListOf(0,0)
        redButtonCount = 12
        blackButtonCount = 12
        redHasToEat = false
        blackHasToEat = false
        redWillEat.clear()
        blackWillEat.clear()

    }

}