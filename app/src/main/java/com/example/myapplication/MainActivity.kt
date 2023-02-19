package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.PhotoCapture
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val photoAdapter = PhotoAdapter(this, DataManager.photos.toList())
        binding.photoDisplay.adapter = photoAdapter

        binding.imageButton.setOnClickListener {
            // start photo taking activity
            val intent = Intent(this, PhotoCapture::class.java)
            this.startActivity(intent)
        }
    }

}

class PhotoAdapter(val _context : Context, var photos : List<photo>) : BaseAdapter() {
    val height: Int
    get() {
        return _context.resources.displayMetrics.heightPixels
    }

    val width: Int
        get() {
            return _context.resources.displayMetrics.widthPixels
        }
    override fun getCount(): Int {
        return photos.count()
    }

    override fun getItem(p0: Int): Any {
        return photos[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val photo = photos[p0]

        var image = ImageView(_context)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        image.layoutParams = ViewGroup.LayoutParams(300, 300)
        image.load(photo.path)

        return image
    }

}