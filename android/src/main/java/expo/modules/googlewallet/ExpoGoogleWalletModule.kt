package expo.modules.googlewallet

import android.content.Context
import android.app.Activity
import android.util.Log

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.Promise
import expo.modules.kotlin.AppContext
import expo.modules.core.interfaces.ActivityProvider
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.exception.CodedException

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

import com.google.android.gms.pay.Pay
import com.google.android.gms.pay.PayClient
import com.google.android.gms.pay.PayApiAvailabilityStatus

class ExpoGoogleWalletModule: Module() {
  private lateinit var walletClient: PayClient
  private val RC_GOOGLE_WALLET: Int = 1000

  private val context: Context
    get() = appContext.reactContext ?: throw Exceptions.ReactContextLost()
  private val activity: Activity 
    get() = appContext.activityProvider?.currentActivity ?: throw Exceptions.MissingActivity()
  private var savePassPromise: Promise? = null

  override fun definition() = ModuleDefinition {
    Name("ExpoGoogleWallet")

    OnCreate {
      walletClient = Pay.getClient(activity)
    }

    AsyncFunction("isWalletAvailable") { promise: Promise ->
      runBlocking {
        try {
          val available = checkWalletAvailability()

          promise.resolve(available)
        } catch (e: Exception) {
          promise.reject("ERR_GOOGLE_WALLET", "Failed to check if wallet is available", e)
        }
      }
    }

    AsyncFunction("savePass") { jwt: String, promise: Promise ->
      walletClient.savePasses(jwt, activity, RC_GOOGLE_WALLET)
      savePassPromise = promise
    }

    OnActivityResult {_, payload ->
      if (payload.requestCode == RC_GOOGLE_WALLET) {
        when (payload.resultCode) {
          Activity.RESULT_OK -> {
            savePassPromise?.resolve(true)
          }

          Activity.RESULT_CANCELED -> {
            savePassPromise?.resolve(false)
          }

          PayClient.SavePassesResult.SAVE_ERROR -> payload.data?.let { intentData -> 
            val errorMessage = intentData.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE)

            savePassPromise?.reject("ERR_SAVE_ERROR", "Failed to save the pass: $errorMessage", CodedException(errorMessage))
          }

          else -> {
            savePassPromise?.reject("ERR_UNKNOWN", "An unknown error has occurred. Please try again later", null)
          }
        }
      }

      savePassPromise = null
    }
  }

  private suspend fun checkWalletAvailability(): Boolean {
    return try {
        val status = walletClient
            .getPayApiAvailabilityStatus(PayClient.RequestType.SAVE_PASSES)
            .await()

      status == PayApiAvailabilityStatus.AVAILABLE
    } catch (e: Exception) {
      false
    }
  }
}
