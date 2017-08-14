package adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.rodolfo.testeappalpha.CheckoutActivity
import com.example.rodolfo.testeappalpha.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import modules.Constants
import modules.Flower
import org.intellij.lang.annotations.JdkConstants
import java.util.ArrayList
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap



/**
 * Created by conra on 13/04/2017.
 */
class CheckoutAdapter(context: Context) : RecyclerView.Adapter<CheckoutAdapter.Holder>() {

    private val listFlowers: MutableList<Flower>
    private val TAG = "CheckoutAdapter"
    val aContext: Context

    init {
        listFlowers = ArrayList<Flower>()
        this.aContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_item_checkout, null, false)
        //val height = parent.measuredHeight/6
        //row.setMinimumHeight(height)
        return Holder(row)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.v(TAG, "onBindViewHolder")
        val currFlower: Flower = listFlowers.get(position)

        holder.mName.setText(currFlower.name)
        holder.mPrice.setText("R$" + currFlower.price)
        holder.deleteBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                removeItem(holder.adapterPosition)
            }
        })
        Picasso.with(holder.itemView.context).
                load(Constants.HTTP.BASE_URL + "/photos/" + currFlower.photo)
                .resize(200, 200)
                .centerCrop()
                .into(holder.mPhoto, object : Callback {
                    override fun onSuccess() {
                        val imageBitmap = (holder.mPhoto.getDrawable() as BitmapDrawable).bitmap
                        val imageDrawable = RoundedBitmapDrawableFactory.create(this@CheckoutAdapter.aContext.resources, imageBitmap)
                        imageDrawable.isCircular = true
                        imageDrawable.cornerRadius = Math.max(imageBitmap.width, imageBitmap.height) / 2.0f
                        holder.mPhoto.setImageDrawable(imageDrawable)
                    }

                    override fun onError() {
                        holder.mPhoto.setImageResource(R.mipmap.ic_launcher_round);
                    }
                })
    }

    private fun removeItem(position: Int) {
        listFlowers.removeAt(position)
        CheckoutActivity.checkoutList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listFlowers.size)
    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount() called")
        return listFlowers.size
    }

    fun addFlower(flower: Flower) {
        Log.v(TAG, "" + flower.photo)
        listFlowers.add(flower)
        Log.v(TAG, "" + listFlowers.size)
        notifyDataSetChanged()
    }

    fun getSelectedFlower(position: Int): Flower {
        return listFlowers[position]
    }

    fun reset() {
        listFlowers.clear()
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mPhoto: ImageView
        val mName: TextView
        val mPrice: TextView
        val deleteBtn: ImageButton

        init {
            Log.v(TAG, "Holder")
            mPhoto = itemView.findViewById(R.id.flowerPhotoCheckout) as ImageView
            mName = itemView.findViewById(R.id.flowerNameCheckout) as TextView
            mPrice = itemView.findViewById(R.id.flowerPriceCheckout) as TextView
            deleteBtn = itemView.findViewById(R.id.deleteButton) as ImageButton

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }
    }
}

