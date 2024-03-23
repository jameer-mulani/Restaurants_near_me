package com.deserve.restaurantsnearme

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deserve.restaurantsnearme.databinding.RestaurantListItemBinding
import com.deserve.restaurantsnearme.model.entity.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

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


    override fun onViewRecycled(holder: ListItemViewHolder) {
        super.onViewRecycled(holder)
        holder.job?.cancel()
    }

    inner class ListItemViewHolder(private val binding: RestaurantListItemBinding) :
        RecyclerView.ViewHolder(binding.root), CoroutineScope by MainScope() {

            var job : Job? = null

        fun bind(restaurant: Restaurant, listener : ((Restaurant)->Unit)?) {
            binding.apply {

                listener?.let {
                    binding.root.setOnClickListener {
                        listener.invoke(restaurant)
                    }
                }
                if (!restaurant.imageUrl.isNullOrEmpty()){
                    job = launch {
                        val bitmap = loadImage(restaurant.imageUrl!!)
                        restaurantThumbnail.setImageBitmap(bitmap)
                    }
                }
                restaurantName.text = restaurant.name
                restaurantOpen.text = restaurant.openStatus
                executePendingBindings()
            }
        }



        private suspend fun loadImage(url : String) : Bitmap{
            return withContext(Dispatchers.IO){
                return@withContext BitmapFactory.decodeStream(URL(url).openStream())
            }
        }


    }

}