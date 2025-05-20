package com.example.digitallibrary15052025.ui.register.singup

import android.os.Bundle
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

            if (email.isNotEmpty() && password.isNotEmpty() && passwordAgain.isNotEmpty()) {
                if (password == passwordAgain) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Navigate to VhodFragment on successful signup
                                findNavController().navigate(R.id.singup_to_singin)
                            } else {
                                // Show error message if signup fails
                                Toast.makeText(activity, task.exception?.message ?: "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Все поля обязательны для заполнения", Toast.LENGTH_SHORT).show()
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
}
