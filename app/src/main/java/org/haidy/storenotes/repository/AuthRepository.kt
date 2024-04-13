package org.haidy.storenotes.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {
    suspend fun signUpUser(
        email: String,
        password: String
    ): Boolean {
        return try {
            val request = auth.createUserWithEmailAndPassword(email, password).await()
            request.user?.uid != null
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }

    suspend fun loginUser(email: String, password: String): String {
        return try {
            val request = auth.signInWithEmailAndPassword(email, password).await()
            request?.user?.uid ?: ""
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            throw e
        }
    }
}