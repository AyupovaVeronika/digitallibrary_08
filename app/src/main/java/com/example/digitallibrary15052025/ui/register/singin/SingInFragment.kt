package com.example.digitallibrary15052025.ui.register.singin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.digitallibrary15052025.R


class SingInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sing_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.singin_to_singup).setOnClickListener {
            findNavController().navigate(R.id.action_singInFragment_to_singUpFragment)
        }
        view.findViewById<Button>(R.id.singin_to_forgotpassword).setOnClickListener {
            findNavController().navigate(R.id.action_singInFragment_to_forgotPasswordFragment)
        }
    }
}