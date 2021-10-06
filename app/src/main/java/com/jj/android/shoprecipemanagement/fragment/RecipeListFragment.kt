package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentRecipeListBinding
import com.jj.android.shoprecipemanagement.viewmodel.RecipeListViewModel

class RecipeListFragment : CommonFragment<FragmentRecipeListBinding>(R.layout.fragment_recipe_list), View.OnClickListener {

    val recipeListViewModel : RecipeListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.recipeViewModel = recipeListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recipeAddButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.recipeAddButton -> {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_recipeDetailFragment)
            }
        }
    }

}