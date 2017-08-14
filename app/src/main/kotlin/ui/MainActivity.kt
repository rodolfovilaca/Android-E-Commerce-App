package com.example.rodolfo.testeappalpha

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.mikepenz.actionitembadge.library.ActionItemBadge
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.util.Log
import android.view.MenuItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import android.support.v7.widget.SearchView
import android.content.Context

class MainActivity : AppCompatActivity() {
    companion object badgeCount {
        var num = 0
    }

    val TAG = "Main Activity"
    private var mDrawer: Drawer? = null
    //var badgeCount = 0
    private val tabs = listOf(R.drawable.ic_restaurant_menu_black_24dp, R.drawable.ic_whatshot_black_24dp, R.drawable.ic_favorite_black_24dp)
    lateinit var searchManager: SearchManager
    lateinit var searchMenuItem: MenuItem
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = MenuItemCompat.getActionView(menu.findItem(R.id.searchMenu)) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQueryHint("Search")
        /*searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })*/
        if (badgeCount.num > 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.checkout), ContextCompat.getDrawable(this, R.drawable.ic_shopping_cart_black_24dp), ActionItemBadge.BadgeStyles.BLUE, badgeCount.num)
        }/*
        //Badge desaparece quando badgecount == 0
        else {
            ActionItemBadge.hide(menu_main?.findItem(R.id.checkout))
        }*/

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.checkout -> consume { newCheckout() }
        else -> super.onOptionsItemSelected(item)
    }

    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun newCheckout() {
        val intent: Intent = Intent(this, CheckoutActivity::class.java)
        startActivity(intent)
        //Toast.makeText(this, "Checkout", Toast.LENGTH_LONG).show()
        badgeCount.num = 0
        invalidateOptionsMenu(this)
    }

    private fun configViews() {

        val item1 = PrimaryDrawerItem().withIdentifier(0).withName("Pratos").withIcon(R.drawable.ic_restaurant_menu_black_24dp)
        val item2 = PrimaryDrawerItem().withIdentifier(1).withName("Em Alta").withIcon(R.drawable.ic_whatshot_black_24dp)
        val item3 = PrimaryDrawerItem().withIdentifier(2).withName("Favoritos").withIcon(R.drawable.ic_favorite_black_24dp)
        val item4 = PrimaryDrawerItem().withIdentifier(3).withName("Checkout").withIcon(R.drawable.ic_shopping_cart_black_24dp)
        val item5 = PrimaryDrawerItem().withIdentifier(4).withName("Conta").withIcon(R.drawable.ic_account_circle_black_24dp)
        val item6 = PrimaryDrawerItem().withIdentifier(5).withName("Settings").withIcon(R.drawable.ic_settings_black_24dp)

        val mToolbar = findViewById(R.id.my_toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        mToolbar.setTitle("TesteAlpha")

        val mViewPager = findViewById(R.id.viewpager) as ViewPager
        val adapter: TabsFragmentPageAdapter = TabsFragmentPageAdapter(getSupportFragmentManager(), this)
        mViewPager.setAdapter(adapter)
        val limit = adapter.count
        mViewPager.setOffscreenPageLimit(limit)

        val accountHeader = AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.drawable.header).build()

        mDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(item1, item2, item3,
                        DividerDrawerItem(), item4, DividerDrawerItem(), item5, item6)
                .withTranslucentStatusBar(false)
                .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                    override fun onItemClick(view: View, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
                        when (drawerItem?.getIdentifier()) {
                            0L -> mViewPager.setCurrentItem(position - 1)
                            1L -> mViewPager.setCurrentItem(position - 1)
                            2L -> mViewPager.setCurrentItem(position - 1)
                            3L -> newCheckout()

                        }
                        mDrawer!!.closeDrawer()
                        return true
                    }
                })
                .build()

        val mTabs = findViewById(R.id.sliding_tabs) as TabLayout
        mTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position + 1
                Log.i(TAG, "position: " + position)
                mDrawer!!.setSelectionAtPosition(position, false)
                invalidateOptionsMenu()
            }
        })
        mTabs.setupWithViewPager(mViewPager)
        mTabs.getTabAt(0)?.setIcon(tabs[0])
        mTabs.getTabAt(1)?.setIcon(tabs[1])
        mTabs.getTabAt(2)?.setIcon(tabs[2])
    }
    /*fun addCart(view: View, pageFragment: PageFragment){
        badgeCount++
        invalidateOptionsMenu()
        /*val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm.copyToRealmOrUpdate(pageFragment.mFlowerAdapter.getSelectedFlower(pageFragment.mFlowerAdapter.)) }
        realm.close()*/


        /*val mDialog: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        val mView: View = layoutInflater.inflate(R.layout.spinner_dialog, null)
        mDialog.setTitle("Escolha a quantidade: ")
        val mSpiner: Spinner = mView.findViewById(R.id.dialogSpinner) as Spinner
        val nums = listOf<Int>(0,1,2,3,4)
        val adapter: ArrayAdapter<Int> = ArrayAdapter<Int>(this@MainActivity,android.R.layout.simple_spinner_item, nums)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpiner.setAdapter(adapter)
        mDialog.setPositiveButton("Adicionar ao Carrinho", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface, which: Int) {
                if(mSpiner.selectedItem == 0){
                    dialog.dismiss()
                }else {
                    badgeCount++
                    invalidateOptionsMenu()
                    dialog.dismiss()
                    Snackbar.make(findViewById(R.id.main),"Adicionado ao Carrinho",Snackbar.LENGTH_LONG).show()

                    /*val outputStream: FileOutputStream =  FileOutputStream("checkout", true)
                    val writeFile: ObjectOutputStream = ObjectOutputStream(outputStream)
                    try {
                        writeFile.writeObject(flowerName)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }*/
                }
            }
        })
        mDialog.setNegativeButton("Cancel",object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }

        })
        mDialog.setView(mView).create().show()*/
    }*/
}