package se.forsman.deckbuilder.core.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.forsman.deckbuilder.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    private val failure: MutableLiveData<Failure> = MutableLiveData()
    fun getErrorMessage(): LiveData<Failure> = failure

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}