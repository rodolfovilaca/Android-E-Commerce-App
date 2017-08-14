package modules

import android.os.Parcelable
import com.google.gson.annotations.Expose
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Flower(@Expose open var category: String = "",
                  @Expose open var price: Double = 0.0,
                  @Expose open var instructions: String = "",
                  @Expose open var photo: String = "",
                  @Expose open var name: String = "",
                  @Expose @PrimaryKey open var productId: Int = 0):  RealmObject()