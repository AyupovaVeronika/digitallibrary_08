package com.example.digitallibrary15052025.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.digitallibrary15052025.databinding.FragmentManualBinding


class ManualFragment : Fragment() {
    private lateinit var binding: FragmentManualBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Создание экземпляра класса ManualFragmentBinding и связывание его с разметкой фрагмента
        binding = FragmentManualBinding.inflate(inflater, container, false)

        // Возвращение корневого View разметки фрагмента
        return binding.root
        }


}