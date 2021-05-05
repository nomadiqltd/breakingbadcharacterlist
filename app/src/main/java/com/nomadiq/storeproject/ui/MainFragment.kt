package com.nomadiq.storeproject.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomadiq.storeproject.databinding.FragmentMainBinding
import com.nomadiq.storeproject.ui.character.CharactersAdapter

class MainFragment : Fragment() {

    private val viewModel: FragmentMainViewModel by lazy {
        ViewModelProvider(this).get(FragmentMainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        val adapter = CharactersAdapter(CharactersAdapter.CharacterListener { characterId ->
            Toast.makeText(context, "${characterId}", Toast.LENGTH_LONG).show()
            viewModel.onCharacterClicked(characterId.toInt())
        })
        binding.recyclerView.adapter = adapter

        viewModel.navigateToSelectedCharacter.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                //Must find the NavController from the Fragment
                //   viewModel.displayPropertyDetailsComplete()
                view?.findNavController()
                    ?.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(it.toString()))
                viewModel.onCharacterNavigated()
            }
        })

        viewModel.characters.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        return binding.root
    }

}