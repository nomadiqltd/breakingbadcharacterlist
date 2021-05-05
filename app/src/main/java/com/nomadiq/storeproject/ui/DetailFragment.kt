package com.nomadiq.storeproject.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nomadiq.storeproject.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application

        val binding: FragmentDetailBinding = FragmentDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        var args = MainFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(
            context,
            "Item name args: ${args.itemname},}",
            Toast.LENGTH_LONG
        ).show()

        return binding.root
    }

}