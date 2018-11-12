package com.test.photoeditor.domain.interactor.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T,Param> : UseCase() {

    abstract fun buildUseCaseObservable(param: Param): Observable<T>

    fun execute(observer: DisposableObserver<T>, param: Param) {
        val observable: Observable<T> = buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        addDisposable(observable.subscribeWith(observer))
    }
}