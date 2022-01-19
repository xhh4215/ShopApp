package com.example.library.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

open class HiRestful constructor(
    private val baseUrl: String,
    private val callFactory: HiCall.Factory
) {
    //存储拦截器的集合
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()

    //存储method和method的解析器
    private var methodService: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()

    //对call的代理操纵
    private var scheduler: Scheduler = Scheduler(callFactory, interceptors)

    /***
     * 添加拦截器
     */
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    /***
     * 根据接口对象返回接口的代理对象
     */
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service), object : InvocationHandler {
                override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
                    var methodParser = methodService[method]
                    if (methodParser == null) {
                        //方法的注解，参数 返回类型等的解析
                        methodParser = MethodParser.parse(baseUrl, method)
                        methodService[method] = methodParser
                    }
                    //通过解析到的数据创建request
                    val request = methodParser.newRequest(method, args)
                    //通过代理call进行对请求的拦截处理
                    return scheduler.newCall(request)
                }
            }
        ) as T
    }
}