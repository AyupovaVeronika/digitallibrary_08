package com.example.digitallibrary15052025.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.digitallibrary15052025.databinding.FragmentAcountBinding
import com.google.firebase.auth.FirebaseAuth


class AcountFragment : Fragment() {

    private var _binding: FragmentAcountBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAcountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            binding.emailTextView.text = currentUser.email
        } else {
            binding.emailTextView.text = "Пользователь не авторизован"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
