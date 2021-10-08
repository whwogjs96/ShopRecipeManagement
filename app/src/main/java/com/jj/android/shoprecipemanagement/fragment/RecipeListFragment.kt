package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentRecipeListBinding
import com.jj.android.shoprecipemanagement.interfaceobj.DataRefresh
import com.jj.android.shoprecipemanagement.viewmodel.RecipeListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListFragment : CommonFragment<FragmentRecipeListBinding>(R.layout.fragment_recipe_list), View.OnClickListener, DataRefresh{

    private val recipeListViewModel : RecipeListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.recipeViewModel = recipeListViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recipeAddButton.setOnClickListener(this)
        dataRefresh()
    }

    override fun onResume() {
        super.onResume()
        if(recipeListViewModel.isDataUpdatable) {
            dataRefresh()
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.recipeAddButton -> {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_recipeDetailFragment)
            }
        }
    }

    override fun dataLoading() {
        CoroutineScope(Dispatchers.Default).launch {
            recipeListViewModel.getList()
            Log.e("데이터는 오나?", recipeListViewModel.recipeDataList.toString())
            withContext(Dispatchers.Main) {
                recipeListViewModel.isDataUpdatable = false
                binding.recipeListRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun dataRefresh() {
        recipeListViewModel.clear()
        binding.recipeListRecyclerView.adapter?.notifyDataSetChanged()
        super.dataRefresh()
    }

}