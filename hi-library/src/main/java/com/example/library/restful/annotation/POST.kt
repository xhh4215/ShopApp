package com.example.library.restful.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String, val fromPost: Boolean = true) {
}