package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.Data.network.DogApi
import com.example.myapplication.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

data class photo(val path: String) {}

data class photo2(var path: String, val keyWords: List<String>, var description: String)

object DataManager {
    private val _photos = MutableLiveData<List<photo>>(emptyList())
    val photos: LiveData<List<photo>> = _photos
    private val _photos2 = MutableLiveData<List<photo2>>(emptyList())
    val photos2: LiveData<List<photo2>> = _photos2
    val keyWords: List<String> = listOf("Udstyr", "DMOE21", "AB589", "face", "person", "1banan9", "crai", "sdfpij", "45398", "adiu453fgd", "abab", "legitrc")
    init {
        SeedData()
    }

    private fun SeedData() {
        val sampleText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."

        GlobalScope.launch {
            val response = DogApi.retrofitService.GetPhotos(50)
            val photoSet = response.message.map { photo(it) }
            val photo2Set = response.message.map {
                val start = Random.nextInt(0, keyWords.size)
                val end = Random.nextInt(start, keyWords.size)

                val sstart = Random.nextInt(0,256)
                val send = Random.nextInt(sstart, Math.min(sstart+256, sampleText.length))
                photo2(it, keyWords.subList(start,end),sampleText.substring(sstart,send))
            }
            _photos.postValue(photoSet)
            _photos2.postValue(photo2Set)
        }
    }
}


class PhotoAdapter(var photos: List<photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {
    var onItemClick: ((photo, position : Int) -> Unit)? = null
    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentPosition: Int = -1
        var currentPhoto: photo? = null
        var imageView: ImageView = itemView.findViewById(R.id.imvPhoto)

        fun setPhoto(position: Int) {
            this.currentPosition = position
            this.currentPhoto = photos[position]

            imageView.load(this.currentPhoto!!.path)

          imageView.setOnClickListener {
              onItemClick?.invoke(this.currentPhoto!!, this.currentPosition)
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.photo_list_item, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.setPhoto(position)
    }

    override fun getItemCount(): Int = photos.size
}

class Photo2Adapter(var photos: List<photo2>) : RecyclerView.Adapter<Photo2Adapter.PhotoHolder>() {
    var onItemClick: ((photo : photo2, position : Int) -> Unit)? = null
    inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentPosition: Int = -1
        var currentPhoto: photo2? = null
        var imageView: ImageView = itemView.findViewById(R.id.imv_photo)
        var descriptionText : TextView = itemView.findViewById(R.id.txv_description)
        var keywordsChipGroup: ChipGroup = itemView.findViewById(R.id.cg_keywords)

        fun setPhoto(position: Int) {
            this.currentPosition = position
            this.currentPhoto = photos[position]

            imageView.load(this.currentPhoto!!.path)
            descriptionText.setText(currentPhoto!!.description)
            keywordsChipGroup.removeAllViews()
            for(keyword in currentPhoto!!.keyWords) {
                val chip = LayoutInflater.from(itemView.context).inflate(R.layout.view_chip_item, keywordsChipGroup, false)
                val chipBind = chip.findViewById<Chip>(R.id.displayChip)
                chipBind.text = keyword

                keywordsChipGroup.addView(chip)
            }

            imageView.setOnClickListener {
                onItemClick?.invoke(this.currentPhoto!!, this.currentPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.photo2_list_item, parent, false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.setPhoto(position)
    }

    override fun getItemCount(): Int = photos.size
}