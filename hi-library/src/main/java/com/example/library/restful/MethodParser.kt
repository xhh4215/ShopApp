package com.example.library.restful

import com.example.library.restful.annotation.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/****
 * @author 栾桂明
 * @desc 对方法的注解信息进行解析
 */
class MethodParser(val baseUrl: String, method: Method, args: Array<Any>) {
    //请求的域名
    private var domainUrl: String? = null

    //是否是表单提交
    private var formPost: Boolean = true

    //请求的方法
    private var httpMethod: Int = 0

    //请求的相对路径
    private lateinit var relativeUrl: String

    //方法的返回数据类型
    private var returnType: Type? = null

    //存储headers的集合
    private var headers: MutableMap<String, String> = mutableMapOf()

    //参数的集合
    private var parameters: MutableMap<String, String> = mutableMapOf()

    init {
        /****
         * 解析方法的注解
         */
        parseMethodAnnotation(method)
        /***
         * 解析方法的参数
         */
        parseMethodParameters(method, args)

        /***
         * 解析方法的返回值类型
         */
        parseMethodReturnType(method)
    }

    /****
     * 解析方法的返回值
     */
    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s must be type of  HiCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) {
                "method %s can only has one generic return type"
            }
            returnType = actualTypeArguments[0]
        } else {
            throw IllegalStateException(
                String.format(
                    "method %s must be have a generic  return type",
                    method.name
                )
            )
        }
    }

    /***
     * 解析方法的注解
     */
    private fun parseMethodAnnotation(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            when (annotation) {
                is GET -> {
                    relativeUrl = annotation.value
                    httpMethod = HiRequest.METHOD.GET
                }
                is POST -> {
                    relativeUrl = annotation.value
                    httpMethod = HiRequest.METHOD.POST
                    formPost = annotation.fromPost
                }
                is Headers -> {
                    val headerArray = annotation.value
                    for (header in headerArray) {
                        val location = header.indexOf(":")
                        check(!(location == 0 || location == -1)) {
                            String.format(
                                "@header value must be in the from [name:value],but found %s",
                                header
                            )
                        }
                        val name = header.substring(0, location)
                        val value = header.substring(location + 1).trim()
                        headers[name] = value

                    }
                }
                is BaseUrl -> {
                    domainUrl = annotation.value
                }
                else -> {
                    throw  IllegalStateException("can not handle method annotation" + annotation.javaClass.toString())
                }
            }

        }
        require((httpMethod == HiRequest.METHOD.GET) || (httpMethod == HiRequest.METHOD.POST)) {
            String.format("method %s must has one of GET POST", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }

    }

    /***
     * 解析方法的参数
     */
    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        val parameterAnnotations = method.parameterAnnotations
        val equals = method.parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotation count %s dont match expect count%s",
                parameterAnnotations.size,
                args.size
            )
        }
        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) {
                String.format(
                    "field can only has one annotation:index=$index",
                    parameterAnnotations.size,
                    args.size
                )
            }
            val value = args[index]
            require(isPrimitive(value)) {
                "8 basic  type support for now  index:$index"
            }

            val annotation = annotations[0]
            if (annotation is Field) {
                val key = annotation.value
                val value = args[index]
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.name
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    val newRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                    relativeUrl = newRelativeUrl
                }
            } else {
                throw IllegalStateException("can not handle param annotation" + annotation.javaClass.toString())
            }
        }

    }

    /***
     * 判断参数是不是八种基本数据类型
     */
    private fun isPrimitive(value: Any): Boolean {
        if (value.javaClass == String::class.java) {
            return true

        }
        try {
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false

    }

    /***
     * 通过解析到的数据创建request
     */
    fun newRequest(): HiRequest {
        var request = HiRequest()
        request.domainUrl = domainUrl
        request.headers = headers
        request.parameters = parameters
        request.httpMethod = httpMethod
        request.returnType = returnType
        request.relativeUrl = relativeUrl
        request.formPost = formPost
        return request
    }


    companion object {
        fun parse(baseUrl: String, method: Method, args: Array<Any>): MethodParser {
            return MethodParser(baseUrl, method, args)
        }
    }
}