package com.example.digitallibrary15052025.ui.main.reading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digitallibrary15052025.R
import java.io.File

class FileAdapter(private val onFileClick: (File) -> Unit) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private val files = mutableListOf<File>()

    fun addFile(file: File) {
        files.add(file)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount(): Int = files.size

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileNameTextView: TextView = itemView.findViewById(R.id.fileName)
        private val filePreviewTextView: TextView = itemView.findViewById(R.id.filePreview)

        fun bind(file: File) {
            fileNameTextView.text = file.name

            // Загрузка первых строк текста файла
            val previewText = try {
                file.bufferedReader().useLines { lines ->
                    lines.take(2).joinToString("\n")
                }
            } catch (e: Exception) {
                "Ошибка чтения файла"
            }
            filePreviewTextView.text = previewText

            itemView.setOnClickListener {
                onFileClick(file)
            }
        }
    }
    fun addFiles(newFiles: List<File>) {
        files.addAll(newFiles)
        notifyDataSetChanged()
    }

}
