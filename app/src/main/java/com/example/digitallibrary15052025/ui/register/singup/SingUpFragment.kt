package com.example.digitallibrary15052025.ui.register.singup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.digitallibrary15052025.R
import com.google.firebase.auth.FirebaseAuth
import com.example.digitallibrary15052025.databinding.FragmentSingUpBinding


class SingUpFragment : Fragment() {
    private lateinit var binding: FragmentSingUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_sing_up, container, false)
        binding = FragmentSingUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()
        binding.singupButton.setOnClickListener {
            val email = binding.emailSingUpText.text.toString().trim()
            val password = binding.passwordSingUpText.text.toString()
            val passwordAgain = binding.passwordAgainSingUpText.text.toString()

            when {
                email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() -> {
                    showToast("Все поля обязательны для заполнения")
                }
                !isEmailValid(email) -> {
                    showToast("Введите корректный email")
                }
                password.length < 6 -> {
                    showToast("Пароль должен содержать минимум 6 символов")
                }
                password != passwordAgain -> {
                    showToast("Пароли не совпадают")
                }
                else -> {
                    registerUser(email, password)
                }
            }
        }

        // Set up button to switch to login (Vhod) fragment
        /*binding.singup_to_singin.setOnClickListener {
            // Navigate to VhodFragment
            findNavController().navigate(R.id.action_singUpFragment_to_singInFragment)
        }
        */
        view.findViewById<Button>(R.id.singup_to_singin).setOnClickListener {
            findNavController().navigate(R.id.action_singUpFragment_to_singInFragment)
        }


    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_singUpFragment_to_singInFragment)
                } else {
                    Log.e("SignUpError", "Registration failed", task.exception)
                    showToast(task.exception?.message ?: "Ошибка регистрации")
                }
            }
            .addOnFailureListener { e ->
                Log.e("SignUpError", "Firebase error", e)
                showToast("Ошибка соединения с сервером")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
