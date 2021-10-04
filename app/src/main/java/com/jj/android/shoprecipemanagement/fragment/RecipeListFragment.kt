package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentRecipeListBinding
import com.jj.android.shoprecipemanagement.viewmodel.RecipeListViewModel

class RecipeListFragment : CommonFragment<FragmentRecipeListBinding>(R.layout.fragment_recipe_list) {

    val recipeListViewModel : RecipeListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.recipeViewModel = recipeListViewModel
        return binding.root
    }

}