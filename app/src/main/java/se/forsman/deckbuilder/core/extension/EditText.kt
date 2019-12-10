package se.forsman.deckbuilder.core.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable

fun EditText.afterTextChanged(): Observable<String> {
    return Observable.create {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                it.onNext(text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
}