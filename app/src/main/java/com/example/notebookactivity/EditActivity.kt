package com.example.notebookactivity

import android.content.Intent
import android.net.Uri
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
        getMyIntents()
    }


    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.imMainImage.setImageURI(it)
        tempImageUri = it.toString()
        //contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)

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


            //val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)


            getContent.launch("image/*")

        }

        // сохранение в бд
        binding.fbSave.setOnClickListener {

            val myTitle = binding.edTitle.text.toString()
            val myDesc = binding.edDesc.text.toString()
            if (myTitle != "" && myDesc != "") {
                Log.e(s, "save")
                myDbManager.insertToDb(myTitle, myDesc, tempImageUri)
                finish()

            }
        }
    }


    fun getMyIntents(){
        val i = intent

        if (i != null){
            if(i.getStringExtra(MyIntentConstant.I_TITLE_KEY) != null){

                binding.fbAddImage.visibility = View.GONE
                binding.edTitle.setText(i.getStringExtra(MyIntentConstant.I_TITLE_KEY))
                binding.edDesc.setText(i.getStringExtra(MyIntentConstant.I_DESC_KEY))
                if(i.getStringExtra(MyIntentConstant.I_URI_KEY) != "empty"){
                    Log.d("Log","loggg")
                    binding.mainImageLayout.visibility = View.VISIBLE
                    binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstant.I_URI_KEY)))
                    binding.imButtonEditImage.visibility = View.GONE
                    binding.imButtonDeleteImage.visibility = View.GONE


                }
            }
        }
    }

    }

