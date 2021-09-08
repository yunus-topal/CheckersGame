package com.example.checkersgame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.checkersgame.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentMainMenuBinding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        return binding.root
    }

}