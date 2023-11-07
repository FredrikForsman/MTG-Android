package se.forsman.deckbuilder.core.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import se.forsman.deckbuilder.MainActivity
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.app.BaseFragment

fun FragmentManager.navigateTo(fragment: Fragment, backStackTag: String?) {
    this.apply {
        beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(backStackTag)
            .commit()
    }
}

fun Fragment.close() {
    parentFragmentManager.popBackStack()
}

val BaseFragment.fragmentContainer: View get() = (activity as MainActivity).findViewById(R.id.fragmentContainer)

