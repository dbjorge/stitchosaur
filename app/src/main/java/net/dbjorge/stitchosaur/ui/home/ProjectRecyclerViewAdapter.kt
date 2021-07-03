package net.dbjorge.stitchosaur.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.dbjorge.stitchosaur.R
import net.dbjorge.stitchosaur.data.Project

typealias ClickListener = (Project) -> Unit

class ProjectRecyclerViewAdapter(
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {
    private var projectList = emptyList<Project>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_list_item, parent, false) as ViewGroup
        val viewHolder = ViewHolder(itemContainer)
        itemContainer.setOnClickListener {
            clickListener(projectList[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projectList[position]
        holder.label.text = project.label
    }

    override fun getItemCount() = projectList.size

    fun updateProjects(projectList: List<Project>) {
        this.projectList = projectList
        notifyDataSetChanged()
    }

    class ViewHolder(itemViewGroup: ViewGroup) : RecyclerView.ViewHolder(itemViewGroup) {
        val label: TextView = itemViewGroup.findViewById(R.id.projectLabelTextView)
    }
}