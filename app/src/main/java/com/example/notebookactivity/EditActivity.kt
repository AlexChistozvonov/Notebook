package com.example.notebookactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.notebookactivity.databinding.ActivityEditBinding
import com.example.notebookactivity.databinding.ActivityMainBinding
import com.example.notebookactivity.db.MyDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.net.URI

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    val s = "sae"
    var tempImageUri = "empty"
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imMainImage.setImageURI(it)
        Log.e(s, "sasasasas")
        tempImageUri = it.toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume(){
        super.onResume()
        myDbManager.openDb()


        binding.fbAddImage.setOnClickListener {
            binding.mainImageLayout.visibility = View.VISIBLE
            binding.fbAddImage.visibility = View.GONE
        }

        // удаление картинки и закрытие окна с картинкой
        binding.imButtonDeleteImage.setOnClickListener {
            binding.mainImageLayout.visibility = View.GONE
            binding.fbAddImage.visibility = View.VISIBLE
        }

        // изменение картинки
        binding.imButtonEditImage.setOnClickListener {
            getContent.launch("image/*")

        }

        // сохранение в бд
        binding.fbSave.setOnClickListener {

            val myTitle = binding.edTitle.text.toString()
            val myDesc = binding.edDesc.text.toString()
            Log.e(s, "save")
            if (myTitle != "" && myDesc != "") {
                Log.e(s, "save")
                myDbManager.insertToDb(myTitle, myDesc, tempImageUri)

            }
        }
    }


    }

