package com.lovisgod.testVisaTTP.handlers.network.simplecalladapter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class Subscription {

    private var disposed = false

    fun isDisposed() = disposed

    fun dispose() {
        disposed = true
    }

    fun bind(owner: LifecycleOwner) = bind(owner, ON_DESTROY)

    fun bind(owner: LifecycleOwner, event: Lifecycle.Event) {

        owner.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(ON_PAUSE)
            fun onPause() {
                if (event == ON_PAUSE) {
                    removeObserverAndDispose(owner)
                }
            }

            @OnLifecycleEvent(ON_STOP)
            fun onStop(owner: LifecycleOwner) {
                if (event == ON_STOP) {
                    removeObserverAndDispose(owner)
                }
            }

            @OnLifecycleEvent(ON_DESTROY)
            fun onDestroy(owner: LifecycleOwner) {
                if (event == ON_DESTROY) {
                    removeObserverAndDispose(owner)
                }
            }

            fun removeObserverAndDispose(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                dispose()
            }
        })
    }

}