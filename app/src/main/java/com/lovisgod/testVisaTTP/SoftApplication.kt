package com.lovisgod.testVisaTTP

import android.content.Context
import android.content.ContextWrapper
import com.lovisgod.testVisaTTP.domain.SoftposAppContainer
import com.pixplicity.easyprefs.library.Prefs

object SoftApplication {

     fun onCreate(context: Context) {

        Prefs.Builder()
            .setContext(context)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName("com.lovisgod.isw_visa_ttp")
            .setUseDefaultSharedPreference(true)
            .build()
    }

    object container {
        var softposAppContainer = SoftposAppContainer()
        var horizonPayUseCase = softposAppContainer.getUseCases()
    }

}