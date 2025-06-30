package kr.co.rolling.moment.library.data

import kr.co.rolling.moment.BuildConfig

object Deploy {
    var SERVER: ServerType = BuildConfig.SERVER_TYPE

    enum class ServerType {
        DEVELOP,

        RELEASE
    }
}
