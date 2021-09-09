package com.example.checkersgame

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
    private var isGameOver = false
    private val redWillEat = mutableListOf<MutableList<Int>>()
    private val blackWillEat = mutableListOf<MutableList<Int>>()

    //TODO
    // king button +
    // king paint +
    // king moveButtonEat ?
    // king check +

    // initialize the game when fragment loads for the first time.
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

    // TODO might be done
    // check if the button clicked is king or not.
    fun clicked(i: Int, j:Int){
        if(!isGameOver){
            if(boardStates[i][j] == 0){
                clear()
            }
            else if(boardStates[i][j] == 1){
                clear()
                if(turnRed){
                    if(redHasToEat)
                        if(kingStates[i][j])
                            paintYellowEatKing(i, j)
                        else
                            paintYellowEat(i, j)
                    else
                        paintYellow(i, j)
                }
            }
            else if(boardStates[i][j] == 2){
                clear()
                if(!turnRed){
                    if (blackHasToEat)
                        if(kingStates[i][j])
                            paintBlueEatKing(i, j)
                        else
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
    }

    private fun paintYellowEatKing(i: Int, j:Int){
        //CLEAR!!
        redWillEat.clear()
        if(i < 6){
            // bottom left
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j - 2] = 3

                    redWillEat.add((mutableListOf(i+2, j-2, 0)))
                    paintRecursiveYellowKing(i + 2, j - 2,0)
                }
            }
            // bottom right
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j + 2] = 3

                    redWillEat.add((mutableListOf(i+2, j+2, 1)))
                    paintRecursiveYellowKing(i + 2, j + 2, redWillEat.size - 1)
                }
            }
        }
        if(i > 1){
            // top left
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 2){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i - 2][j - 2] = 3

                    redWillEat.add((mutableListOf(i-2, j-2, 2)))
                    paintRecursiveYellowKing(i - 2, j - 2, redWillEat.size - 1)
                }
            }
            // top right
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 2){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i - 2][j + 2] = 3

                    redWillEat.add((mutableListOf(i-2, j+2, 3)))
                    paintRecursiveYellowKing(i - 2, j + 2, redWillEat.size - 1)
                }
            }
        }
        prevClicked[0] = i
        prevClicked[1] = j
    }

    private fun paintBlueEatKing(i: Int, j:Int){
        //CLEAR!!
        blackWillEat.clear()
        if(i < 6){
            // bottom left
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 1){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i + 2][j - 2] = 3

                    blackWillEat.add((mutableListOf(i+2, j-2, 0)))
                    paintRecursiveBlueKing(i + 2, j - 2,0)
                }
            }
            // bottom right
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 1){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i + 2][j + 2] = 3

                    blackWillEat.add((mutableListOf(i+2, j+2, 1)))
                    paintRecursiveBlueKing(i + 2, j + 2, blackWillEat.size - 1)
                }
            }
        }
        if(i > 1){
            // top left
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j - 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j-2, 2)))
                    paintRecursiveBlueKing(i - 2, j - 2, blackWillEat.size - 1)
                }
            }
            // top right
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j + 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j+2, 3)))
                    paintRecursiveBlueKing(i - 2, j + 2, blackWillEat.size - 1)
                }
            }
        }
        prevClicked[0] = i
        prevClicked[1] = j
    }

    private fun paintRecursiveYellowKing(i: Int, j:Int, index: Int){
        if(redWillEat[index].size > 14)
            return

        if(i < 6){
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2 && redWillEat[index].last() != 3){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveBottomLeft = (mutableListOf(i + 2, j - 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveBottomLeft.add(0)
                    redWillEat.add(moveBottomLeft)
                    paintRecursiveYellowKing(i + 2,j - 2, redWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2 && redWillEat[index].last() != 2){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i + 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveBottomRight = (mutableListOf(i + 2, j + 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveBottomRight.add(1)
                    redWillEat.add(moveBottomRight)
                    paintRecursiveYellowKing(i + 2,j + 2, redWillEat.size - 1)
                }
            }
        }

        if(i > 1){
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 2 && redWillEat[index].last() != 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i - 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveTopLeft = (mutableListOf(i - 2, j - 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveTopLeft.add(2)
                    redWillEat.add(moveTopLeft)
                    paintRecursiveYellowKing(i - 2,j - 2, redWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 2 && redWillEat[index].last() != 0){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_yellow_button)
                    boardStates[i - 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveTopRight = (mutableListOf(i - 2, j + 2) + redWillEat[index].drop(2)) as MutableList<Int>
                    moveTopRight.add(3)
                    redWillEat.add(moveTopRight)
                    paintRecursiveYellowKing(i - 2,j + 2, redWillEat.size - 1)
                }
            }
        }
    }

    private fun paintRecursiveBlueKing(i: Int, j:Int, index: Int){
        if(blackWillEat[index].size > 14)
            return

        if(i < 6){
            if(j > 1){
                if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 1 && blackWillEat[index].last() != 3){
                    boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i + 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveBottomLeft = (mutableListOf(i + 2, j - 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveBottomLeft.add(0)
                    blackWillEat.add(moveBottomLeft)
                    paintRecursiveBlueKing(i + 2,j - 2, blackWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 1 && blackWillEat[index].last() != 2){
                    boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i + 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveBottomRight = (mutableListOf(i + 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveBottomRight.add(1)
                    blackWillEat.add(moveBottomRight)
                    paintRecursiveBlueKing(i + 2,j + 2, blackWillEat.size - 1)
                }
            }
        }

        if(i > 1){
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1 && blackWillEat[index].last() != 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j - 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveTopLeft = (mutableListOf(i - 2, j - 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveTopLeft.add(2)
                    blackWillEat.add(moveTopLeft)
                    paintRecursiveBlueKing(i - 2,j - 2, blackWillEat.size - 1)
                }
            }
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1 && blackWillEat[index].last() != 0){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j + 2] = 3
                    boardStates[i][j] = 0
                    boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

                    val moveTopRight = (mutableListOf(i - 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveTopRight.add(3)
                    blackWillEat.add(moveTopRight)
                    paintRecursiveBlueKing(i - 2,j + 2, blackWillEat.size - 1)
                }
            }
        }
    }

    // red players potential moves without capturing enemy.
    // TODO might be done
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

        if(kingStates[i][j]){
            if(i > 0){
                if(j > 0){
                    if(boardStates[i - 1][j - 1] == 0){
                        boardButtons[i - 1][j - 1].setImageResource(R.drawable.ic_yellow_button)
                        boardStates[i - 1][j - 1] = 3
                    }
                }
                if(j < 7){
                    if(boardStates[i - 1][j + 1] == 0){
                        boardButtons[i - 1][j + 1].setImageResource(R.drawable.ic_yellow_button)
                        boardStates[i - 1][j + 1] = 3
                    }
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
    // TODO might be done
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

        if(kingStates[i][j]){
            if(i < 7){
                if(j > 0){
                    if(boardStates[i + 1][j - 1] == 0){
                        boardButtons[i + 1][j - 1].setImageResource(R.drawable.ic_blue_button)
                        boardStates[i + 1][j - 1] = 3
                    }
                }
                if(j < 7){
                    if(boardStates[i + 1][j + 1] == 0){
                        boardButtons[i + 1][j + 1].setImageResource(R.drawable.ic_blue_button)
                        boardStates[i + 1][j + 1] = 3
                    }
                }
            }
        }

        prevClicked[0] = i
        prevClicked[1] = j
    }

    // black players potential moves with capturing enemy.
    // this function uses recursive function below to get multiple captures.
    private fun paintBlueEat(i: Int, j:Int){
        blackWillEat.clear()
        if(i > 1){
            if(j > 1){
                if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
                    boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j - 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j-2, 2)))
                    paintRecursiveBlue(i - 2, j - 2,0)
                }
            }
            if(j < 6){
                if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
                    boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
                    boardStates[i - 2][j + 2] = 3

                    blackWillEat.add((mutableListOf(i-2, j+2, 3)))
                    paintRecursiveBlue(i - 2, j + 2, blackWillEat.size - 1)
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
                    moveLeft.add(2)
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

                    val moveRight = (mutableListOf(i - 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
                    moveRight.add(3)
                    blackWillEat.add(moveRight)
                    paintRecursiveBlue(i - 2,j + 2, blackWillEat.size - 1)
                }
            }
        }
    }

    // move a button to the clicked location.
    // clear previous place and replace new place.
    // TODO might be done
    // change corresponding indexes at kings array.
    private fun moveButton(i: Int, j:Int){
        val x = prevClicked[0]
        val y = prevClicked[1]
        if(turnRed){
            //clear previous place
            boardButtons[x][y].setImageResource(R.drawable.ic_white_button)
            boardStates[x][y] = 0
            //put new button
            boardStates[i][j] = 1
            // carry on changes and check if it became a king.
            kingStates[i][j] = kingStates[x][y]
            kingStates[x][y] = false
            if (i == 7)
                kingStates[i][j] = true

            if(kingStates[i][j])
                boardButtons[i][j].setImageResource(R.drawable.ic_orange_button)
            else
                boardButtons[i][j].setImageResource(R.drawable.ic_red_button)

            if(redHasToEat){
                moveButtonEat(x,y,i,j)
            }

            blackHasToEat = checkBlackEat()
        }
        else{
            //clear previous place
            boardButtons[x][y].setImageResource(R.drawable.ic_white_button)
            boardStates[x][y] = 0
            //put new button
            boardStates[i][j] = 2
            // carry on changes and check if it became a king.
            kingStates[i][j] = kingStates[x][y]
            kingStates[x][y] = false
            if (i == 0)
                kingStates[i][j] = true
            if(kingStates[i][j])
                boardButtons[i][j].setImageResource(R.drawable.ic_green_button)
            else
                boardButtons[i][j].setImageResource(R.drawable.ic_black_button)


            if(blackHasToEat){
                moveButtonEat(x,y,i,j)
            }

            redHasToEat = checkRedEat()
        }
        turnRed = !turnRed

        if(blackButtonCount == 0){
            playerTurn!!.text = "Red Wins!"
            isGameOver = true
        }
        else if(redButtonCount == 0){
            playerTurn!!.text = "Black Wins!"
            isGameOver = true
        }
        else{
            if (turnRed)
                playerTurn!!.text = "Red Moves"
            else
                playerTurn!!.text = "Black Moves"
        }
    }

    // delete eaten buttons after a button moved.
    // TODO might be done NO
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
            // previous location x,y. target location i,j.
            var xx = x
            var yy = y
            for(i in 2 until redTarget.size){
                if(redTarget[i] == 0){
                    boardButtons[xx + 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy - 1] = 0
                    kingStates[xx + 1][yy - 1] = false
                    xx += 2
                    yy -= 2
                }
                else if (redTarget[i] == 1){
                    boardButtons[xx + 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy + 1] = 0
                    kingStates[xx + 1][yy + 1] = false
                    xx += 2
                    yy += 2
                }
                else if(redTarget[i] == 2){
                    boardButtons[xx - 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy - 1] = 0
                    kingStates[xx - 1][yy - 1] = false
                    xx -= 2
                    yy -= 2
                }
                else if(redTarget[i] == 3){
                    boardButtons[xx - 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy + 1] = 0
                    kingStates[xx - 1][yy + 1] = false
                    xx -= 2
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
            // previous location x,y. target location i,j.
            var xx = x
            var yy = y
            for(i in 2 until blackTarget.size){
                if(blackTarget[i] == 0){
                    boardButtons[xx + 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy - 1] = 0
                    kingStates[xx + 1][yy - 1] = false
                    xx += 2
                    yy -= 2
                }
                else if (blackTarget[i] == 1){
                    boardButtons[xx + 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx + 1][yy + 1] = 0
                    kingStates[xx + 1][yy + 1] = false
                    xx += 2
                    yy += 2
                }
                else if(blackTarget[i] == 2){
                    boardButtons[xx - 1][yy - 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy - 1] = 0
                    kingStates[xx - 1][yy - 1] = false
                    xx -= 2
                    yy -= 2
                }
                else if(blackTarget[i] == 3){
                    boardButtons[xx - 1][yy + 1].setImageResource(R.drawable.ic_white_button)
                    boardStates[xx - 1][yy + 1] = 0
                    kingStates[xx - 1][yy + 1] = false
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
    // TODO might be done
    // kings will have special check
    private fun checkRedEat(): Boolean{
        for(i in 0..7){
            for(j in 0..7){
                if(boardStates[i][j] == 1) {
                    // for everyone
                    if(i < 6){
                        if (j < 6){
                            if (boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 2)
                                return true
                        }
                        if (j > 1){
                            if (boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 2)
                                return true
                        }
                    }
                    // for kings
                    if(kingStates[i][j] && i > 1){
                        if (j < 6){
                            if (boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 2)
                                return true
                        }
                        if (j > 1){
                            if (boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 2)
                                return true
                        }
                    }
                }
            }
        }
        return false
    }

    // check whether black player has to eat or not.
    // TODO might be done
    // kings will have special check
    private fun checkBlackEat(): Boolean{
        for(i in 0..7){
            for(j in 0..7){
                if(boardStates[i][j] == 2) {
                    // for everyone
                    if(i > 1){
                        if (j < 6){
                            if (boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1)
                                return true
                        }
                        if (j > 1){
                            if (boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1)
                                return true
                        }
                    }
                    // for kings
                    if(kingStates[i][j] && i < 6){
                        if (j < 6){
                            if (boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 1)
                                return true
                        }
                        if (j > 1){
                            if (boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 1)
                                return true
                        }
                    }
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
        isGameOver = false
        redWillEat.clear()
        blackWillEat.clear()

    }

}

/**
 DEPO

 private fun paintBlueEatKing(i: Int, j:Int){
 //CLEAR!!
 blackWillEat.clear()
 if(i < 6){
 // bottom left
 if(j > 1){
 if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 1){
 boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i + 2][j - 2] = 3

 blackWillEat.add((mutableListOf(i+2, j-2, 2)))
 paintRecursiveBlueKing(i + 2, j - 2, blackWillEat.size - 1)
 }
 }
 // bottom right
 if(j < 6){
 if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 1){
 boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i + 2][j + 2] = 3

 blackWillEat.add((mutableListOf(i+2, j+2, 3)))
 paintRecursiveBlueKing(i + 2, j + 2, blackWillEat.size - 1)
 }
 }
 }
 if(i > 1){
 // top left
 if(j > 1){
 if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
 boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i - 2][j - 2] = 3

 blackWillEat.add((mutableListOf(i-2, j-2, 0)))
 paintRecursiveBlueKing(i - 2, j - 2, blackWillEat.size - 1)
 }
 }
 // top right
 if(j < 6){
 if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
 boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i - 2][j + 2] = 3

 blackWillEat.add((mutableListOf(i-2, j+2, 1)))
 paintRecursiveBlueKing(i - 2, j + 2, blackWillEat.size - 1)
 }
 }
 }
 prevClicked[0] = i
 prevClicked[1] = j
 }


 private fun paintRecursiveBlueKing(i: Int, j:Int, index: Int){
 if(blackWillEat[index].size > 14)
 return

 if(i > 1){
 if(j > 1){
 if(boardStates[i - 2][j - 2] == 0 && boardStates[i - 1][j - 1] == 1){
 boardButtons[i - 2][j - 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i - 2][j - 2] = 3
 boardStates[i][j] = 0
 boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

 val moveTopLeft = (mutableListOf(i - 2, j - 2) + blackWillEat[index].drop(2)) as MutableList<Int>
 moveTopLeft.add(0)
 blackWillEat.add(moveTopLeft)
 paintRecursiveBlueKing(i - 2,j - 2, blackWillEat.size - 1)
 }
 }
 if(j < 6){
 if(boardStates[i - 2][j + 2] == 0 && boardStates[i - 1][j + 1] == 1){
 boardButtons[i - 2][j + 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i - 2][j + 2] = 3
 boardStates[i][j] = 0
 boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

 val moveTopRight = (mutableListOf(i - 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
 moveTopRight.add(1)
 blackWillEat.add(moveTopRight)
 paintRecursiveBlueKing(i - 2,j + 2, blackWillEat.size - 1)
 }
 }
 }

 if(i < 6){
 if(j > 1){
 if(boardStates[i + 2][j - 2] == 0 && boardStates[i + 1][j - 1] == 1){
 boardButtons[i + 2][j - 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i + 2][j - 2] = 3
 boardStates[i][j] = 0
 boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

 val moveBottomLeft = (mutableListOf(i + 2, j - 2) + blackWillEat[index].drop(2)) as MutableList<Int>
 moveBottomLeft.add(2)
 blackWillEat.add(moveBottomLeft)
 paintRecursiveBlueKing(i + 2,j - 2, blackWillEat.size - 1)
 }
 }
 if(j < 6){
 if(boardStates[i + 2][j + 2] == 0 && boardStates[i + 1][j + 1] == 1){
 boardButtons[i + 2][j + 2].setImageResource(R.drawable.ic_blue_button)
 boardStates[i + 2][j + 2] = 3
 boardStates[i][j] = 0
 boardButtons[i][j].setImageResource(R.drawable.ic_white_button)

 val moveBottomRight = (mutableListOf(i + 2, j + 2) + blackWillEat[index].drop(2)) as MutableList<Int>
 moveBottomRight.add(3)
 blackWillEat.add(moveBottomRight)
 paintRecursiveBlueKing(i + 2,j + 2, blackWillEat.size - 1)
 }
 }
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
 kingStates[xx - 1][yy - 1] = false
 xx -= 2
 yy -= 2
 }
 else if (blackTarget[i] == 1){
 boardButtons[xx - 1][yy + 1].setImageResource(R.drawable.ic_white_button)
 boardStates[xx - 1][yy + 1] = 0
 kingStates[xx - 1][yy + 1] = false
 xx -= 2
 yy += 2
 }
 else if (blackTarget[i] == 2){
 boardButtons[xx + 1][yy - 1].setImageResource(R.drawable.ic_white_button)
 boardStates[xx + 1][yy - 1] = 0
 kingStates[xx + 1][yy - 1] = false
 xx += 2
 yy -= 2
 }
 else if (blackTarget[i] == 3){
 boardButtons[xx + 1][yy + 1].setImageResource(R.drawable.ic_white_button)
 boardStates[xx + 1][yy + 1] = 0
 kingStates[xx + 1][yy + 1] = false
 xx += 2
 yy += 2
 }
 redButtonCount--
 }
 }
 */