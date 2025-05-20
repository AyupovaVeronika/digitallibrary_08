package com.example.digitallibrary15052025.ui.main.profile
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.digitallibrary15052025.R

class ProfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        // Вставка AcountFragment внутрь ProfilFragment
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerAcount, AcountFragment())
            .commit()

        return view
    }
}