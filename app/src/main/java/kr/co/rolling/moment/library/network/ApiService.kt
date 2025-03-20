package kr.co.rolling.moment.library.network

import com.skydoves.sandwich.ApiResponse
import kr.co.rolling.moment.library.network.data.request.RequestSignUp
import kr.co.rolling.moment.library.network.data.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *
 */
interface ApiService {
    @POST(NetworkConstants.API_PATH_SIGN_UP)
    suspend fun requestSignUp(
        @Body requestSignUp: RequestSignUp
    ): ApiResponse<SignUpResponse>
}