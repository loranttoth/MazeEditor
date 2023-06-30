package com.loranttoth.mazeeditor.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.loranttoth.mazeeditor.EditorActivity
import com.loranttoth.mazeeditor.LevelView
import com.loranttoth.mazeeditor.R

const val EXTRA_MESSAGE = "com.loranttoth.mazeeditor.MAZEDATA"
const val EXTRA_POSITION = "com.loranttoth.mazeeditor.MAZEPOS"

class Adapter(private val context: Context, private val list: ArrayList<Maze>) : RecyclerView.Adapter<Adapter.MyVH>() {



    //Creating View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val levelviewCustom: View = LayoutInflater.from(context).inflate(R.layout.level_view_custom_view, null, false)

        val levelView: View = levelviewCustom.findViewWithTag("levelImage1")

        val view = LayoutInflater.from(context).inflate(R.layout.level_view_item,parent,false)

        val layout: LinearLayout = view.findViewWithTag("levelViewLayout")

        if (levelView.parent != null) {
            (levelView.parent as ViewGroup).removeView(levelView)
        }

        layout.addView(levelView)

        return MyVH(view)
    }

    //Setting Data in View
    override fun onBindViewHolder(holder: MyVH, position: Int) {
        val data=list[position]
        holder.itemView.findViewById<TextView>(R.id.levelTitle).text = data.level.toString()+"/"+data.index.toString()
        holder.itemView.findViewById<TextView>(R.id.isEdit).text = "Nem"
        holder.itemView.findViewById<TextView>(R.id.isSaved).text = "Igen"

        val levelviewCustom = holder.itemView.findViewById<View>(R.id.layoutView)
        val levelView = levelviewCustom.findViewWithTag<LevelView>("levelImage1")
        levelView.setMazeSize(data.x, data.y, data.maze, false)

        //holder.itemView.findViewById<TextView>(R.id.sub_title).text = data.subTitle
      //  val levelView = holder.itemView.findViewById<LevelView>(R.id.levelView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditorActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, data)
                putExtra(EXTRA_POSITION, position)
            }
            ContextCompat.startActivity(context, intent, Bundle.EMPTY)


            /*val intent = Intent(context, ImageActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, imageLink)
            }
            startActivity(context, intent, Bundle.EMPTY)
            */
        }
    }

    //Returns Size Of Array List
    override fun getItemCount(): Int {
        return list.size
    }

    //ViewHolder For List Item
    class MyVH (itemView: View):
        RecyclerView.ViewHolder(itemView)

}
