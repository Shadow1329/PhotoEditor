package com.test.photoeditor.domain.interactor.base

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

abstract class CompletableUseCase<Param> : UseCase() {

    abstract fun buildUseCaseCompletable(param: Param): Completable

    fun execute(observer: DisposableCompletableObserver, param: Param) {
        val completable: Completable = buildUseCaseCompletable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        addDisposable(completable.subscribeWith(observer))
    }
}