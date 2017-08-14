package com.example.rodolfo.testeappalpha

import adapters.CheckoutAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import modules.Checkout
import modules.Flower
import android.support.v7.widget.DividerItemDecoration




class CheckoutActivity : AppCompatActivity() {
    lateinit var flower: Flower
    companion object checkout {
        var checkoutList: MutableList<Int> = ArrayList<Int>()
    }

    var flowerList: MutableList<Flower> = ArrayList<Flower>()
    lateinit var checkoutAdapter: CheckoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val intent: Intent = intent
        Log.d("CheckoutActivity", "checkoutList: "+checkoutList.size)
        configViews()
        val realm: Realm = Realm.getDefaultInstance()
        setFlowers(realm)
        realm.close()
        Log.d("CheckoutActivity", "flowerList: "+flowerList.size)
    }

    private fun setFlowers(realm: Realm) {
        for(i in 0..checkoutList.size-1){
            flower = realm.where(Flower::class.java).equalTo("productId", checkout.checkoutList[i]).findFirst()
            checkoutAdapter.addFlower(flower)
        }
    }

    private fun configViews() {

        val toolbar: Toolbar = findViewById(R.id.toolbar_checkout) as Toolbar
        toolbar.setTitle("Checkout")
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val recycler: RecyclerView = findViewById(R.id.checkout_recycler_view) as RecyclerView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(recycler.context, (recycler.layoutManager as LinearLayoutManager).orientation)
        recycler.addItemDecoration(dividerItemDecoration)
        checkoutAdapter = CheckoutAdapter(this)
        recycler.setAdapter(checkoutAdapter)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_checkout,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
