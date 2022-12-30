package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crownedjester.soft.findoutagebyname.databinding.ItemFavoriteBinding
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName

class NamesAdapter(private val adapterCallback: NamesAdapterCallback) :
    RecyclerView.Adapter<NamesAdapter.NamesViewHolder>() {

    private var hasLongClickListener = false

    private var isCheckboxesVisible = false
    private var _namesHolderCallback: NamesHolderCallback? = null
    val namesHolderCallback get() = _namesHolderCallback

    private val namesDiffCallback = object : DiffUtil.ItemCallback<FavoriteName>() {
        override fun areItemsTheSame(oldItem: FavoriteName, newItem: FavoriteName): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: FavoriteName, newItem: FavoriteName): Boolean =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this@NamesAdapter, namesDiffCallback)

    inner class NamesViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root), NamesHolderCallback {

        fun bind(name: FavoriteName) {
            binding.apply {
                textViewName.text = name.name

                checkboxItemFavorite.apply {

                    setOnCheckedChangeListener(null)

                    isChecked = name.isSelected

                    setOnClickListener {
                        name.isSelected = isChecked
                    }

                    if (!isCheckboxesVisible) {
                        isChecked = false
                        name.isSelected = false
                    }

                    visibility = if (isCheckboxesVisible) View.VISIBLE else View.INVISIBLE

                }

                itemView.apply {
                    if (!hasLongClickListener) {
                        setOnLongClickListener {

                            hasLongClickListener = true

                            adapterCallback.onLongClickCallback() {
                                changeCheckboxesVisibility()
                            }

                            true
                        }
                    }

                    if (!hasOnClickListeners()) {
                        setOnClickListener {
                            adapterCallback.onNameClickCallback(name)
                        }
                    }

                }
            }

        }

        override fun performAdapterLongClick() {
            itemView.performLongClick()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NamesViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size


    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        val name = differ.currentList[position]

        if (_namesHolderCallback == null) {
            _namesHolderCallback = holder
        }

        holder.bind(name)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeCheckboxesVisibility() {
        isCheckboxesVisible = !isCheckboxesVisible

        notifyDataSetChanged()
    }

}