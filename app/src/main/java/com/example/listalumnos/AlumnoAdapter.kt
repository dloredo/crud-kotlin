package com.example.listalumnos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listalumnos.databinding.SingleItemBinding

    class AlumnoAdapter(private val context: Context, private val listAlumno: List<Alumno>, private var optionsMenuClickListener: OptionsMenuClickListener):RecyclerView.Adapter<AlumnoAdapter.ViewHolder>() {

        interface OptionsMenuClickListener {
            fun onOptionsMenuClicked(position: Int)
        }

        inner class ViewHolder(val binding: SingleItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflates the single_item view
            // that is used to hold list item
            val binding =
                SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                with(listAlumno[position]) {
                    Glide.with(context).load(this.imagen).into(binding.imgAlumno)
                    // sets the text to the textview from our itemHolder class
                    binding.nombre.text = this.nombre
                    // sets the text to the textview from our itemHolder class
                    binding.cuenta.text = this.cuenta

                    binding.textViewOptions.setOnClickListener {
                        optionsMenuClickListener.onOptionsMenuClicked(position)
                    }

                }

            }

        }

        override fun getItemCount(): Int {
            return listAlumno.size
        }

        fun setOnItemClickListener(any: Any) {

        }

    }