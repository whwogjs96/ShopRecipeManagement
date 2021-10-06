package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : CommonFragment<FragmentRecipeDetailBinding>(R.layout.fragment_recipe_detail), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.cancelButton -> {
                Navigation.findNavController(v).popBackStack()
            }
        }
    }

}