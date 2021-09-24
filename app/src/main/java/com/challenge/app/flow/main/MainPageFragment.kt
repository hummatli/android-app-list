package com.challenge.app.flow.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.app.base.BaseFragment
import com.challenge.app.databinding.FragmentMainPageBinding
import com.challenge.app.extensions.makeInvisible
import com.challenge.app.extensions.makeVisible
import com.challenge.app.flow.main.adapter.RVListAdapter
import com.challenge.app.utils.RecyclerViewDisabler
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainPageFragment : BaseFragment() {

    private val viewModel: MainPageViewModel by viewModel()
    private lateinit var binding: FragmentMainPageBinding

    private val rvDisabler = RecyclerViewDisabler()

    private val rvListAdapter by lazy {
        RVListAdapter(
            ArrayList(),
            onItemClicked = {
                findNavController().navigate(
                    MainPageFragmentDirections.actionMainPageFragmentToDetailsPageFragment(it)
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listStateLiveData.observe(viewLifecycleOwner, { handleState(it) })
        viewModel.effect.observe(viewLifecycleOwner, { handleEffect(it) })
    }

    override fun setupViews(rootView: View) {
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun handleState(listState: ListState) = when (listState) {
        is ListState.Loading -> {
            setLoading(true)
        }
        is ListState.Init -> {
            setLoading(false)
        }
        is ListState.Loaded -> {
            setLoading(false)
            rvListAdapter.setData(listState.result)
        }
        is ListState.Error -> {
            setLoading(false)
        }
    }

    private fun handleEffect(effect: Effect) = when(effect) {
        is Effect.ShowErrorMessage -> {
            showError(effect.ex, binding.root) {
                viewModel.getAirlines()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            pbLoading.makeVisible()
        } else {
            pbLoading.makeInvisible()
        }
        pbLoading.isIndeterminate = isLoading

        if (isLoading) {
            rvItems.addOnItemTouchListener(rvDisabler)
        } else {
            rvItems.removeOnItemTouchListener(rvDisabler)
        }
    }
}
