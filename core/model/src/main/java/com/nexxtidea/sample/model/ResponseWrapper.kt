package com.nexxtidea.sample.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseWrapper<T>(
  val errors: List<ErrorDetail>,
  val data: T? = null,
)

data class ErrorDetail(
  @JsonProperty("detail")
  val detail: String? = null,

  @JsonProperty("source")
  val source: String? = null,

  @JsonProperty("code")
  val code: String = "400"
)
