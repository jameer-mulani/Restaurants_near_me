package com.deserve.restaurantsnearme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deserve.restaurantsnearme.databinding.RestaurantListItemBinding
import com.deserve.restaurantsnearme.model.entity.Restaurant

class RestaurantListAdapter : RecyclerView.Adapter<RestaurantListAdapter.ListItemViewHolder>() {

    var items: List<Restaurant> = emptyList()
    var itemClickListener : ((Restaurant)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_item, parent, false)
        return ListItemViewHolder(RestaurantListItemBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val restaurant = items[position]
        holder.bind(restaurant, itemClickListener)
    }

    class ListItemViewHolder(private val binding: RestaurantListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant, listener : ((Restaurant)->Unit)?) {
            binding.apply {
                listener?.let {
                    binding.root.setOnClickListener {
                        listener.invoke(restaurant)
                    }
                }
                restaurantName.text = restaurant.name
                restaurantOpen.text = restaurant.openStatus
                executePendingBindings()
            }
        }


    }

}