package com.example.digitallibrary15052025.ui.main.reading

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digitallibrary15052025.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


class ListReadFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter
    private lateinit var addButton: FloatingActionButton

    private val savedFiles = mutableListOf<File>() // Список файлов для сохранения состояния

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_read, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        addButton = view.findViewById(R.id.addButton)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fileAdapter = FileAdapter { file -> openReadFragment(file) }
        recyclerView.adapter = fileAdapter

        val verticalSpaceHeight = resources.getDimensionPixelSize(R.dimen.item_spacing)
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(verticalSpaceHeight))

        addButton.setOnClickListener {
            selectFile()
        }

        // Восстанавливаем состояние списка файлов
        savedInstanceState?.getStringArrayList(KEY_FILES)?.let { filePaths ->
            val files = filePaths.map { File(it) }
            fileAdapter.addFiles(files)
            savedFiles.addAll(files)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val fileName = getFileName(uri)
                if (fileName != null) {
                    // Создаём временный файл и копируем туда данные из выбранного
                    val tempFile = File(requireContext().cacheDir, fileName)
                    requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                        tempFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    fileAdapter.addFile(tempFile)
                    savedFiles.add(tempFile) // Сохраняем файл в список
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val filePaths = savedFiles.map { it.absolutePath }
        outState.putStringArrayList(KEY_FILES, ArrayList(filePaths))
    }

    private fun openReadFragment(file: File) {
        val fragment = if (file.extension == "txt") {
            ReadFragment.newInstance(file.absolutePath)
        } else {
            Toast.makeText(requireContext(), "Неподдерживаемый формат файла", Toast.LENGTH_SHORT).show()
            return
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_register, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 1
        private const val KEY_FILES = "key_files"
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        } else if (uri.scheme == "file") {
            fileName = File(uri.path ?: "").name
        }
        return fileName
    }
    private fun selectFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
    }
}
