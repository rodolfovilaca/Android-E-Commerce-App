package ui

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by conra on 07/04/2017.
 */
class CustomApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}