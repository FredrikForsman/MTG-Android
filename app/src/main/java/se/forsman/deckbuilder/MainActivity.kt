package se.forsman.deckbuilder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.forsman.deckbuilder.core.extension.makeStatusBarTransparent
import se.forsman.deckbuilder.features.decks.mydecks.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.makeStatusBarTransparent()

        supportFragmentManager.apply {
            this.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }
    }
}
