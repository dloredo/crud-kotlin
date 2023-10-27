package com.example.listalumnos

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listalumnos.databinding.ActivityMainNuevoBinding

class MainActivityNuevo : AppCompatActivity() {
    private lateinit var binding: ActivityMainNuevoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincular vistas con MainActivityNuevo
        binding = ActivityMainNuevoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Creamos el Intent para pasarnos al MainActivity y mandamos por extras los valores
        val intento2 = Intent(this, MainActivity::class.java)
        // usamos la etiqueta mensaje para indicar que es nuevo alumno
        /*intento2.putExtra("mensaje", "nuevo")
        intento2.putExtra("nombre", "${txtNom}")
        intento2.putExtra("cuenta", "${txtCue}")
        intento2.putExtra("correo", "${txtCorr}")
        intento2.putExtra("image", "${txtImg}")
        startActivity(intento2)*/

        val dbalumnos = DBHelperAlumno(this)
        val valorExtra = intent.extras

        val idAlumno = valorExtra?.getInt("idAlumno")
        Toast.makeText(this, "idAlumno = $idAlumno",Toast.LENGTH_LONG).show()
        binding.txtDato.text="Nuevo alumno"

        if(idAlumno!=0){
            binding.txtDato.text="Editar alumno"
            val db = dbalumnos.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM alumnos WHERE id=$idAlumno" , null)

            if(cursor.moveToFirst()){
                var itemNom = cursor.getString(1)
                var itemCue = cursor.getString(cursor.getColumnIndexOrThrow("nocuenta"))
                var itemCorr = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                var itemImg = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
                binding.txtNombre?.setText("$itemNom")
                binding.txtCuenta?.setText("$itemCue")
                binding.txtCorreo?.setText("$itemCorr")
                binding.txtImage?.setText("$itemImg")
            }
            dbalumnos.close()
            cursor.close()
            db.close()
        }


        // Click en el botón Guardar
        binding.btnGuardar.setOnClickListener {

            val db = dbalumnos.writableDatabase

            val txtNom = binding.txtNombre.text.toString()
            val txtCue = binding.txtCuenta.text.toString()
            val txtCorr = binding.txtCorreo.text.toString()
            val txtImg = binding.txtImage.text.toString()

            val newReg = ContentValues()
            newReg.put("nombre",txtNom)
            newReg.put("nocuenta",txtCue)
            newReg.put("email",txtCorr)
            newReg.put("imagen",txtImg)

            if(idAlumno!=0) {
                val campollave: String = "id=?"
                val res = db.update("alumnos", newReg, campollave, arrayOf(idAlumno.toString()))


                if (res.toInt() == -1) {
                    Toast.makeText(this, "Error al modificar el registro", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Registro modificado con exito", Toast.LENGTH_LONG).show()
                    binding.txtNombre.text.clear()
                    binding.txtCuenta.text.clear()
                    binding.txtCorreo.text.clear()
                    binding.txtImage.text.clear()
                }
            }else{
                val res = db.insert("alumnos" , null, newReg)
                db.close()

                if (res.toInt() == -1) {
                    Toast.makeText(this, "No se inserto el registro", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Registro insertado con exito", Toast.LENGTH_LONG).show()
                    binding.txtNombre.text.clear()
                    binding.txtCuenta.text.clear()
                    binding.txtCorreo.text.clear()
                    binding.txtImage.text.clear()
                }
            }
            val intent01 = Intent(this, MainActivity::class.java)
            startActivity(intent01)
        }
    }
}