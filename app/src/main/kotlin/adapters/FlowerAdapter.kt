package adapters

import com.example.rodolfo.testeappalpha.MainActivity
import modules.Constants
import modules.Flower
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.rodolfo.testeappalpha.CheckoutActivity
import com.example.rodolfo.testeappalpha.R
import com.squareup.picasso.Picasso
import java.util.ArrayList


class FlowerAdapter(private val mListener: FlowerClickListener,context: Context) : RecyclerView.Adapter<FlowerAdapter.Holder>() {
    private val mFlowers: MutableList<Flower>
    private val TAG = "FlowerAdapter"
    val aContext: Context

    init {
        mFlowers = ArrayList<Flower>()
        this.aContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_item, null, false)
        return Holder(row)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.v(TAG, "onBindViewHolder")
        val currFlower: Flower = mFlowers.get(position)

        holder.mName.setText(currFlower.name)
        holder.mPrice.setText("R$" + currFlower.price)
        holder.mButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View){
                MainActivity.badgeCount.num++
                invalidateOptionsMenu(aContext as MainActivity)
                CheckoutActivity.checkout.checkoutList.add(currFlower.productId)
            }
        })
        //var url: CloudinaryTransform =  CloudinaryTransform()
        //if (currFlower.isFromDatabase) {
        // holder.mPhoto.setImageBitmap(currFlower.picture)
        //} else {
        //url.transform()
        //Constants.HTTP.BASE_URL + "/photos/" + currFlower.photo
        Picasso.with(holder.itemView.context).load(Constants.HTTP.BASE_URL + "/photos/" + currFlower.photo).error(R.mipmap.ic_launcher).into(holder.mPhoto)
        //}
    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount() called")
        return mFlowers.size
    }

    fun addFlower(flower: Flower) {
        Log.v(TAG, "" + flower.photo)
        mFlowers.add(flower)
        Log.v(TAG, "" + mFlowers.size)
        notifyDataSetChanged()
    }

    fun getSelectedFlower(position: Int): Flower {
        return mFlowers[position]
    }

    fun reset() {
        mFlowers.clear()
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mPhoto: ImageView
        val mName: TextView
        val mPrice: TextView
        val mButton: ImageButton

        init {
            Log.v(TAG, "Holder")
            mPhoto = itemView.findViewById(R.id.flowerPhoto) as ImageView
            mName = itemView.findViewById(R.id.flowerName) as TextView
            mPrice = itemView.findViewById(R.id.flowerPrice) as TextView
            mButton = itemView.findViewById(R.id.button)as ImageButton

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mListener.onClick(layoutPosition)
        }
    }


    interface FlowerClickListener {

        fun onClick(position: Int)
    }
}
