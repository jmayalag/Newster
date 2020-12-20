package com.hodinkee.hodinnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

val <T> MutableLiveData<T>.readOnly: LiveData<T>
    get() = this

val <T : Any?> MutableLiveData<T>.readOnlyOptional: LiveData<T>
    get() = this