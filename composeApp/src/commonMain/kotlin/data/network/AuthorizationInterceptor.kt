package data.network


//class AuthorizationInterceptor : Interceptor {
class AuthorizationInterceptor {

//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val authenticatedRequest = request.newBuilder()
//            .header("Authorization", "Bearer $accessToken")
//            .header("Content-Type", "application/vnd.api+json")
//            .header("Accept", "application/json")
//            .header("User-Agent", "com.udnahc.fireflydroid")
//            .build()
//        return chain.proceed(authenticatedRequest)
//    }

    companion object {
        var accessToken: String = ""
    }
}