package com.example.listalumnos

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listalumnos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var data = ArrayList<Alumno>()
    private lateinit var rvadapter: AlumnoAdapter
    var idAlumno = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // ArrayList of class ItemsViewModel
        /*data.add(
            Alumno(
                "José Nabor",
                "20102345",
                "jmorfin@ucol.mx",
                "https://imagenpng.com/wp-content/uploads/2017/02/pokemon-hulu-pikach.jpg"
            )
        )
        data.add(
            Alumno(
                "Luis Antonio",
                "20112345",
                "jmorfin@ucol.mx",
                "https://i.pinimg.com/236x/e0/b8/3e/e0b83e84afe193922892917ddea28109.jpg"
            )
        )
        data.add(
            Alumno(
                "Juan Pedro",
                "20122345",
                "jmorfin@ucol.mx",
                "https://i.pinimg.com/736x/9f/6e/fa/9f6efa277ddcc1e8cfd059f2c560ee53--clipart-gratis-vector-clipart.jpg"
            )
        )
*/




        binding.faButton.setOnClickListener {
            val intento1 = Intent(this, MainActivityNuevo::class.java)
            intento1.putExtra("idAlumno",0)
            startActivity(intento1)
        }


        // Variable para recibir extras
        val parExtra = intent.extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val image = parExtra?.getString("image")

        // Comprobar si el mensaje es para un nuevo alumno
        if (msje == "nuevo") {
            val insertIndex: Int = data.count()
            data.add(
                insertIndex,
                Alumno(
                    "${nombre}",
                    "${cuenta}",
                    "${correo}",
                    "${image}"
                )
            )
            rvadapter.notifyItemInserted(insertIndex)
        }
        val dbalumnos = DBHelperAlumno(this)
        val db = dbalumnos.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM alumnos", null)
        if (cursor.moveToFirst()) {
            do {
                idAlumno = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                var itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                var itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                data.add(
                    Alumno(
                        "${itemNom}",
                        "${itemCue}",
                        "${itemCorr}",
                        "${itemImg}"
                    )
                )
            } while (cursor.moveToNext())
            db.close()
            cursor.close()

            binding.recyclerview.layoutManager = LinearLayoutManager(this)

            rvadapter = AlumnoAdapter(this, data, object : AlumnoAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(position: Int) {
                    //Toast.makeText(this@MainActivity, "onItemClick ${position}", Toast.LENGTH_LONG).show()
                    itemOptionsMenu(position)
                }
            })
            binding.recyclerview.adapter = rvadapter

        }

    }

    private fun itemOptionsMenu(position: Int) {

        val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
        popupMenu.inflate(R.menu.options_menu)

        val intento2 = Intent(this, MainActivityNuevo::class.java)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.borrar -> {
                        val dbalumnos = DBHelperAlumno(this@MainActivity)
                        val db = dbalumnos.readableDatabase
                        val nombre = data[position].nombre
                        val cursor = db.rawQuery("SELECT * FROM alumnos WHERE nombre='$nombre'",null)
                        if (cursor.moveToFirst()){
                            idAlumno = cursor.getString(0).toInt()
                        }


                      /*  val tmpAlum = data[position]
                        data.remove(tmpAlum)
                        rvadapter.notifyDataSetChanged() */
                        val dialog = AlertDialog.Builder(this@MainActivity)
                            .setTitle("Borrar resgistro")
                            .setMessage("¿Estas seguro que deseas eliminar el registro?")
                            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                                Toast.makeText(this@MainActivity, "Presionaste cancelar", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                                //Toast.makeText(this@MainActivity,"Presionaste OK",Toast.LENGTH_SHORT).show()

                                val db = dbalumnos.writableDatabase

                                db.delete("alumnos","id=$idAlumno",null)
                                Toast.makeText(this@MainActivity,"Registro eliminado",Toast.LENGTH_SHORT).show()
                                db.close()
                                dbalumnos.close()
                                dialog.dismiss()
                                recreate()
                            }
                            .setCancelable(false)
                            .create()

                        dialog.show()

                        return true
                    }
                    R.id.editar -> {
                        val nombre  = data[position].nombre
                        /*val cuenta = data[position] .cuenta
                        val correo = data[position] .correo
                        val imagen = data[position] .imagen
                        val idAlum: Int = position
                        intento2.putExtra("mensaje", "edit")
                        intento2.putExtra("nombre", "${nombre}")
                        intento2.putExtra("cuenta", "${cuenta}")
                        intento2.putExtra("correo", "${correo}")
                        intento2.putExtra("image", "${imagen}")
                        intento2.putExtra("idA", idAlum)*/
                        val dbalumnos = DBHelperAlumno(this@MainActivity)
                        val db = dbalumnos.readableDatabase
                        val cursor = db.rawQuery("SELECT * FROM alumnos WHERE nombre='$nombre'",null)
                        if (cursor.moveToFirst()) {
                            idAlumno = cursor.getString(0).toInt()
                        }
                        cursor.close()
                        db.close()
                        dbalumnos.close()
                        intento2.putExtra("idAlumno" , idAlumno)
                        startActivity(intento2)

                        Toast.makeText(this@MainActivity , " ${idAlumno.toString()} clicked" , Toast.LENGTH_LONG).show()
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}

