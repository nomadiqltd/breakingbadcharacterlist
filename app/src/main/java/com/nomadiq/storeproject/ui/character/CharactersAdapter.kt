package com.nomadiq.storeproject.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nomadiq.storeproject.R
import com.nomadiq.storeproject.databinding.AdapterCharacterItemBinding

import com.nomadiq.storeproject.ui.character.CharactersAdapter.DataItem.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class CharactersAdapter(val clickListener: CharacterListener) :
    ListAdapter<CharactersAdapter.DataItem,
            RecyclerView.ViewHolder>(DiffCallback) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<BreakingBadCharacter>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(Header)
                else -> listOf(Header) + list.map { DataItem.CharacterItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val breakingBadCharacterItem = getItem(position) as DataItem.CharacterItem
                holder.bind(clickListener, breakingBadCharacterItem.character)
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(
            oldItem: DataItem,
            newItem: DataItem
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: DataItem,
            newItem: DataItem
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Decorator / Wrapper for each item type [Header]  or [BreakingBadCharacter] Item
     */
    sealed class DataItem {
        data class CharacterItem(val character: BreakingBadCharacter) : DataItem() {
            override val id = character.charId
        }

        object Header : DataItem() {
            override val id = Long.MIN_VALUE
        }

        abstract val id: Long
    }

    /** ViewHolder from the Header items */
    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.adapter_header_item, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    /** ViewHolder from the Character items */
    class ViewHolder(private var binding: AdapterCharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: CharacterListener, character: BreakingBadCharacter) {
            binding.movie = character
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterCharacterItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CharacterItem -> ITEM_VIEW_TYPE_ITEM
            null -> throw IllegalStateException("ViewType is Null")
            else -> throw IllegalStateException("Undefined default item view type")
        }
    }

    // Handle click events
    class CharacterListener(val clickListener: (characterId: Long) -> Unit) {
        fun onClick(character: BreakingBadCharacter) = clickListener(character.charId)
    }

}