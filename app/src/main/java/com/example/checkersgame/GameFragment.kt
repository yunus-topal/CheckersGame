package com.example.checkersgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.example.checkersgame.databinding.FragmentGameBinding


private const val LAYOUT_TAG = "linear"

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGameBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 8 linear layouts
        val layouts: ArrayList<View> = ArrayList()
        binding.root.findViewsWithText(layouts, LAYOUT_TAG,
            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
        val linearLayouts: ArrayList<LinearLayout> = layouts as ArrayList<LinearLayout>


        viewModel.playerTurn = binding.textView

        // 8 buttons for 8 layout
        for(i in 0..7) {
            viewModel.boardButtons.add(mutableListOf())
            for(j in 0..7){
                viewModel.boardButtons[i].add(linearLayouts[i][j] as ImageButton)
                viewModel.boardButtons[i][j].setOnClickListener{
                    viewModel.clicked(i, j)
                }
            }
        }
        binding.newGameButton.setOnClickListener {
            viewModel.initializeGame()
        }
    }

}