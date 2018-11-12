package com.test.photoeditor.domain.interactor.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase {
    private val mDisposables = CompositeDisposable()

    fun dispose() {
        if (!mDisposables.isDisposed) {
            mDisposables.dispose()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        mDisposables.add(disposable)
    }
}