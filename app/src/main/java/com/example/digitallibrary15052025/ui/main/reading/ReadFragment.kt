package com.example.digitallibrary15052025.ui.main.reading

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.digitallibrary15052025.databinding.FragmentReadBinding
import java.io.File

class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filePath = arguments?.getString(ARG_FILE_PATH) ?: return
        val file = File(filePath)

        if (file.exists() && file.extension == "txt") {
            binding.textView.text = file.readText()
        } else {
            Toast.makeText(requireContext(), "Файл не найден", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack() // Возвращение к списку файлов
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Возврат назад
        }
    }

    companion object {
        private const val ARG_FILE_PATH = "arg_file_path"

        fun newInstance(filePath: String): ReadFragment {
            val fragment = ReadFragment()
            val args = Bundle()
            args.putString(ARG_FILE_PATH, filePath)
            fragment.arguments = args
            return fragment
        }
    }
}
