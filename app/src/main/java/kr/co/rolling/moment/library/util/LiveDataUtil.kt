package kr.co.rolling.moment.library.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kr.co.rolling.moment.library.network.util.SingleEvent


/**
 * Created by JDY on 2021-08-26
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observeEvent(liveData: LiveData<SingleEvent<T>>, action: (t: SingleEvent<T>) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}
