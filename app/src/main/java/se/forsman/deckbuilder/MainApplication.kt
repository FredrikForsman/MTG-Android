package se.forsman.deckbuilder

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import se.forsman.deckbuilder.core.di.applicationModule
import se.forsman.deckbuilder.core.di.editDeckModule
import se.forsman.deckbuilder.core.di.myDecksModule
import se.forsman.deckbuilder.core.di.searchModule

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(listOf(applicationModule, myDecksModule, editDeckModule, searchModule))
        }
    }
}