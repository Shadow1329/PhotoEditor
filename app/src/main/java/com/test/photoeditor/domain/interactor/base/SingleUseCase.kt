package com.test.photoeditor.domain.interactor.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T,Param> : UseCase() {

    abstract fun buildUseCaseSingle(param: Param): Single<T>

    fun execute(observer: DisposableSingleObserver<T>, param: Param) {
        val single: Single<T> = buildUseCaseSingle(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        addDisposable(single.subscribeWith(observer))
    }
}