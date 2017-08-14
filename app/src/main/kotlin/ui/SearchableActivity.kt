package com.example.rodolfo.testeappalpha

import adapters.FlowerAdapter
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import modules.Flower

class SearchableActivity : AppCompatActivity(),FlowerAdapter.FlowerClickListener {
    private lateinit var listFlower: List<Flower>
    private  var auxListFlower: MutableList<Flower>? =  null
    lateinit var toolbar: Toolbar
    lateinit var recycler: RecyclerView
    lateinit var flowerAdapter: FlowerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        toolbar = findViewById(R.id.searchable_toolbar) as Toolbar
        toolbar.setTitle("")
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val realm: Realm = Realm.getDefaultInstance()
        listFlower = realm.where(Flower::class.java).findAll()
        realm.close()

        recycler = findViewById(R.id.searchable_recycler) as RecyclerView
        recycler.setHasFixedSize(true)
        recycler.layoutManager =  LinearLayoutManager(this)
        flowerAdapter =  FlowerAdapter(this,this@SearchableActivity)
        recycler.setAdapter(flowerAdapter)

        handleSearch(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleSearch(intent)
    }

    fun handleSearch(intent: Intent){
        if (Intent.ACTION_SEARCH == intent.action){
            val q: String =  intent.getStringExtra(SearchManager.QUERY)
            toolbar.setTitle(q)
            filterFlowers(q)
        }
    }
    fun filterFlowers(q: String){
        auxListFlower?.clear()
        for (i in 0..listFlower.size-1){
            if (listFlower.get(i).name.toLowerCase().startsWith(q.toLowerCase())){
                auxListFlower?.add(listFlower.get(i))
                flowerAdapter.addFlower(listFlower.get(i))
            }
        }
        flowerAdapter.notifyDataSetChanged()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.searchable_menu, menu)
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = MenuItemCompat.getActionView(menu.findItem(R.id.searchMenu)) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQueryHint("Search")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(position: Int) {
        val selectedFlower: Flower = flowerAdapter.getSelectedFlower(position)
        val intent: Intent = Intent(this,DetailActivity::class.java)
        intent.putExtra("com.example.rodolfo.testeappalpha.flower", selectedFlower.productId)
        startActivity(intent)
    }
}