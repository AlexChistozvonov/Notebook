package com.example.notebookactivity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebookactivity.databinding.ActivityMainBinding
import com.example.notebookactivity.db.MyDbManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val s = "ss"
    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()


        binding.fbNew.setOnClickListener{
            val i = Intent(this, EditActivity::class.java)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()

    }

    fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
        Log.e(s,"init")
    }

    // поиск
    fun initSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val list = myDbManager.readDbData(p0!!)
                myAdapter.updateAdapter(list)
                return true
            }
        })
    }

    fun fillAdapter(){
        Log.e(s,"filladapter")

        val list = myDbManager.readDbData("")
        myAdapter.updateAdapter(list)
        if (list.size > 0){
            binding.tvNoElement.visibility = View.GONE
        }
        else{
            binding.tvNoElement.visibility = View.VISIBLE
        }
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback
            (0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)

            }
        })
    }
}