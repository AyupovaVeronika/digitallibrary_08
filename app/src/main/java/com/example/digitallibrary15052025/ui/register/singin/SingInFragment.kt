package com.example.digitallibrary15052025.ui.register.singin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.digitallibrary15052025.ui.main.MainActivity
import com.example.digitallibrary15052025.R
import com.example.digitallibrary15052025.databinding.FragmentSingInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SingInFragment : Fragment() {
    private var _binding: FragmentSingInBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.apply {
            singinButton.setOnClickListener { handleSignIn() }
            singinToSingup.setOnClickListener { navigateToSingUp() }
            singinToForgotpassword.setOnClickListener { navigateToForgotPassword() }
        }
    }

    private fun handleSignIn() {
        val email = binding.emailSingInText.text.toString().trim()
        val password = binding.passwordSingInText.text.toString().trim()

        when {
            email.isEmpty() || password.isEmpty() -> {
                showToast("Поля не могут быть пустыми")
            }
            !isEmailValid(email) -> {
                showToast("Введите корректный email")
            }
            else -> {
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    Log.e("SignInError", "Authentication failed", task.exception)
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException ->
                            showToast("Пользователь не найден")
                        is FirebaseAuthInvalidCredentialsException ->
                            showToast("Неверный email или пароль")
                        else ->
                            showToast(task.exception?.message ?: "Ошибка входа")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("SignInError", "Firebase error", e)
                showToast("Ошибка соединения с сервером")
            }
    }

    private fun navigateToSingUp() {
        findNavController().navigate(R.id.action_singInFragment_to_singUpFragment)
    }

    private fun navigateToForgotPassword() {
        findNavController().navigate(R.id.action_singInFragment_to_forgotPasswordFragment)
    }

    private fun navigateToMainActivity() {
        Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.also { startActivity(it) }
        requireActivity().finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}