package com.nexxtidea.sample.network.either

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class NetworkResponseAdapter<R: Any>(
  private val responseType: Type
) : CallAdapter<R, Call<R>> {

  override fun adapt(call: Call<R>): Call<R> {
    return NetworkResponseCall(call, responseType)
  }

  override fun responseType(): Type {
    return responseType
  }
}
