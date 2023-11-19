package com.nexxtidea.sample.network.either

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nexxtidea.sample.model.ErrorDetail
import com.nexxtidea.sample.model.ResponseWrapper
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

internal class NetworkResponseCall<R : Any>(
  private val call: Call<R>,
  private val responseType: Type
) : Call<R> {

  companion object {
    private val JACKSON = ObjectMapper().apply {
      registerModule(KotlinModule())
      configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
      configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    internal fun <T : Any> from(json: String?, statusCode: Int): ResponseWrapper<T> {
      return if (json == null) {
        ResponseWrapper(listOf(ErrorDetail(code = statusCode.toString(), source = "Something went wrong!")))
      } else {
        try {
          JACKSON.readValue(json, object : TypeReference<ResponseWrapper<T>>() {})
        } catch (e: Exception) {
          ResponseWrapper(listOf(ErrorDetail(code = statusCode.toString(), detail = json)))
        }
      }
    }

  }

  override fun enqueue(callback: Callback<R>) {
    call.enqueue(
      object : Callback<R> {
        override fun onResponse(call: Call<R>, response: Response<R>) {
          val either = if (!response.isSuccessful) {
            response.errorBody().use { e ->
              from<R>(e?.string(), statusCode = response.code())
            }
          } else {
            response.body() ?: ResponseWrapper(emptyList(), null)
          }
          callback.onResponse(this@NetworkResponseCall, Response.success(either as R))
        }

        override fun onFailure(call: Call<R>, throwable: Throwable) {
          callback.onFailure(this@NetworkResponseCall, throwable)
        }
      }
    )
  }

  override fun isExecuted(): Boolean {
    return call.isExecuted
  }

  override fun clone(): Call<R> {
    return NetworkResponseCall(call.clone(), responseType)
  }

  override fun isCanceled(): Boolean {
    return call.isCanceled
  }

  override fun cancel() {
    call.cancel()
  }

  override fun execute(): Response<R> {
    return call.execute()
  }

  override fun request(): Request {
    return call.request()
  }

  override fun timeout(): Timeout {
    return call.timeout()
  }
}
