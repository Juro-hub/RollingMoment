package kr.co.rolling.moment.library.util

import android.util.Base64
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.DecoderException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.EncoderException
import kr.co.rolling.moment.BuildConfig
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES256 암/복호화 관리
 */
object EncryptManager {
    /** CBC Padding 방식 */
    private const val CipherCBCPadding = "AES/CBC/PKCS7PADDING"

    private val secretKeySpec = createSecretKeySpec(BuildConfig.AES_KEY)

    private fun createSecretKeySpec(key: String): SecretKeySpec? {
        return try {
            //Timber.d("AES ECB Key -> $ECB_KEY")
            SecretKeySpec(key.toByteArray(), "AES")
        } catch (e: Exception) {
            Timber.e("AES ECB Error while generating key: ${e.localizedMessage}")
            null
        }
    }

    /**
     * 앱에서 사용하는 secretKeySpec 을 획득한다.
     *
     * @param key Key를 만들 String
     * @return SecretKeySpec
     */
    private fun getSecretKeySpec(key: String? = null): SecretKeySpec? {
        return if (key.isNullOrEmpty()) {
            secretKeySpec
        } else {
            createSecretKeySpec(key)
        }
    }

    /**
     * CBC 방식으로 암호화 진행 함수
     *
     * @param context 암호화할 평문
     * @param key 암호화에 사용할 KeySpec
     * @return 암호화된 String
     */
    fun setCBCMsg(context: String, key: String? = null): String {
        return try {
            Timber.d("setCBCMsg() original -> $context")
            //The encrypted plaintext becomes ciphertext
            val result = encryptCBC(context, getSecretKeySpec(key))

            //The cipher text Base64 Encoding
            java.util.Base64.getEncoder().encodeToString(result)
//            Base64.encodeToString(result, Base64.NO_WRAP)
        } catch (error: EncoderException) {
            Timber.e("setCBCMsg() Encode Error -> ${error.localizedMessage}")
            ""
        }
    }

    /**
     * CBC 방식으로 암호화 진행 함수
     *
     * @param plainText 암호화할 평문
     * @param keySpec 암호화에 사용할 KeySpec
     * @return 암호화된 ByteArray
     */
    private fun encryptCBC(plainText: String, keySpec: SecretKeySpec?): ByteArray? = try {
        val iv = IvParameterSpec(BuildConfig.AES_VECTOR_KEY.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(CipherCBCPadding)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        cipher.doFinal(plainText.toByteArray())
    } catch (e: Exception) {
        Timber.e("encryptCBC() Error while encrypting: ${e.localizedMessage}")
        null
    }

    /**
     * CBC 방식으로 복호화 진행 함수
     *
     * @param encryptText 복호화할 암호문
     * @param key 복호화에 사용할 KeySpec
     * @return 복호화된 ByteArray
     */
    fun getCBCMsg(encryptText: String, key: String? = null): ByteArray {
        return try {
            val data = Base64.decode(encryptText, Base64.NO_WRAP)
            return decryptCBC(data, getSecretKeySpec(key)) ?: data
        } catch (error: DecoderException) {
            Timber.e("getCBCMsg() Decode Error -> ${error.localizedMessage}")
            Base64.decode(encryptText, Base64.NO_WRAP)
        }
    }

    /**
     * CBC 방식으로 복호화 진행 함수
     *
     * @param cipherText 복호화할 ByteArray
     * @param keySpec 복호화에 사용할 KeySpec
     * @return 복호화된 ByteArray
     */
    private fun decryptCBC(cipherText: ByteArray, keySpec: SecretKeySpec?): ByteArray? = try {
        val iv = IvParameterSpec(BuildConfig.AES_VECTOR_KEY.toByteArray(Charsets.UTF_8))
        val cipher = Cipher.getInstance(CipherCBCPadding)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        cipher.doFinal(cipherText)
    } catch (e: Exception) {
        Timber.e("decryptCBC() Error while decrypting: ${e.localizedMessage}")
        null
    }

    fun decrypt(encryptedText: String?): String {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = IvParameterSpec(BuildConfig.AES_VECTOR_KEY.toByteArray(Charsets.UTF_8))
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(), iv)
        val decrypted: ByteArray = cipher.doFinal(java.util.Base64.getDecoder().decode(encryptedText))
        return String(decrypted, StandardCharsets.UTF_8)
    }
}