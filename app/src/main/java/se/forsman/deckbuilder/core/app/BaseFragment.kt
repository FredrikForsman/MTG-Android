package se.forsman.deckbuilder.core.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import se.forsman.deckbuilder.R
import se.forsman.deckbuilder.core.exception.Failure

abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    internal fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.DatabaseError -> { showError(R.string.failure_database) }
        }
    }

    internal fun showError(message: Int) =
        Snackbar.make(fragmentContainer, message, Snackbar.LENGTH_SHORT).show()
}