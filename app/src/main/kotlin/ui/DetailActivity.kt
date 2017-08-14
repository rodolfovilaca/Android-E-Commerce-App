package com.example.rodolfo.testeappalpha

import modules.Constants
import modules.Flower
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.realm.Realm

class DetailActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var instructions: TextView
    private lateinit var photo: ImageView
    lateinit var flower: Flower
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent: Intent = intent
        val id = intent.getIntExtra("com.example.rodolfo.testeappalpha.flower",0)
        Log.d("DetailActivity", ""+id)
        val realm: Realm = Realm.getDefaultInstance()
        flower = realm.where(Flower::class.java).equalTo("productId",id).findFirst()
        realm.close()

        configViews()

        name.setText(flower.name)
        instructions.setText(flower.instructions)
        Picasso.with(applicationContext).load(Constants.HTTP.BASE_URL + "/photos/" + flower.photo).error(R.mipmap.ic_launcher).into(photo)


    }

    private fun configViews() {
        val collapsingToolbar =  findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.setTitle(flower.name)

        val toolbar = findViewById(R.id.toolbar_detail) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.setTitle(flower.name)

        photo = findViewById(R.id.flowerPhotoDetail) as ImageView
        name = findViewById(R.id.flowerNameDetail) as TextView
        instructions = findViewById(R.id.flowerPriceDetail) as TextView
    }
}
