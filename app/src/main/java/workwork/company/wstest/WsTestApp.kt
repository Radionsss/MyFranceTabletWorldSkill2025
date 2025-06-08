package workwork.company.wstest

import android.app.Application
import workwork.company.wstest.core.Constants
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WsTestApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(Constants.YANDEX_TOKEN)
        MapKitFactory.initialize(this)
    }
}