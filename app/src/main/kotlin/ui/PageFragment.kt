package ui

import adapters.FlowerAdapter
import modules.Flower
import service.RestManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rodolfo.testeappalpha.DetailActivity
import com.example.rodolfo.testeappalpha.R
import io.realm.Realm
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class PageFragment : Fragment(), FlowerAdapter.FlowerClickListener {
    private lateinit var realmUI: Realm
    lateinit var mFlowerAdapter: FlowerAdapter
    private var sub: Subscription? = null
    val ARG_PAGE = "ARG_PAGE"
    var mPage: Int? = null

    fun newInstance(page: Int): PageFragment {
        val args: Bundle = Bundle()
        args.putInt(ARG_PAGE, page)
        val fragment: PageFragment = PageFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        val swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        val mRecycler = view.findViewById(R.id.my_recycler_view) as RecyclerView
        mRecycler.setHasFixedSize(true)
        mRecycler.layoutManager = LinearLayoutManager(context)
        mFlowerAdapter = FlowerAdapter(this, context)
        mRecycler.setAdapter(mFlowerAdapter)
        swipeContainer.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                fetchData()
                swipeContainer.setRefreshing(false)
            }
        })
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPage = getArguments().getInt(ARG_PAGE)
        realmUI = Realm.getDefaultInstance()
        configViews()
    }

    private fun configViews() {

        fetchData()
        /*val swipeContainer = findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer.setOnRefreshListener (object :SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                fetchData()

            }
        })*/
    }

    private fun fetchData() {
        val api = RestManager()
        val observable: Observable<List<Flower>> = api.flowerService.getAllFlowers()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::writeToRealm)
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::readFromRealm)
        /*.subscribe(object: Subscriber<List<Flower>>(){
            override fun onCompleted(){

            }
            override fun onError(t: Throwable){
                Log.d(TAG, "Error")
            }
            override fun onNext(response: List<Flower>){
                for (i in 0..response.size-1){
                    val flower: Flower = response[i]
                    mFlowerAdapter.addFlower(flower)
                }
            }
        })*/
        /*sub = observable.subscribe(object : Subscriber<List<Flower>>() {
            override fun onCompleted() {
                Log.d(TAG, "onCompleted")
            }

            override fun onError(t: Throwable) {
                Log.d(TAG, "Error")
            }

            override fun onNext(response: List<Flower>) {
                Log.d(TAG, "onNext")
                for (i in 0..response.size - 1) {
                    val flower: Flower = response[i]
                    mFlowerAdapter.addFlower(flower)
                }
            }
        })*/
        sub = observable.subscribe(this::display,this::processError)
    }

    private fun readFromRealm(productId: Any): List<Flower>{
        Log.d(TAG,"read from Realm")
        Log.d(TAG," "+realmUI.where(Flower::class.java).findAll().size)
        val result: List<Flower> = realmUI.where(Flower::class.java).findAll()
        return result
    }

    private fun writeToRealm(flowers: List<Flower>){
        Log.d(TAG,"write to Realm")
        val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransaction {  realm.copyToRealmOrUpdate(flowers) }
        Log.d(TAG,""+flowers.size)
        realm.close()
    }

    private fun display(response: List<Flower>){
        for (i in 0..response.size - 1) {
            val flower: Flower = response[i]
            mFlowerAdapter.addFlower(flower)
        }
    }
    private fun processError(e: Throwable){
        Log.e(TAG,e.localizedMessage,e)
        val noInternetResult: List<Flower> = realmUI.where(Flower::class.java).findAll()
        for (i in 0..noInternetResult.size - 1) {
            val flower: Flower = noInternetResult[i]
            mFlowerAdapter.addFlower(flower)
        }
    }

    override fun onClick(position: Int) {
        val selectedFlower: Flower = mFlowerAdapter.getSelectedFlower(position)
        val intent: Intent = Intent(this.context,DetailActivity::class.java)
        intent.putExtra("com.example.rodolfo.testeappalpha.flower", selectedFlower.productId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        sub!!.unsubscribe()
        realmUI.close()
    }
}