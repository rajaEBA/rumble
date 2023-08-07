package com.example.rumble.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rumble.R
import com.example.rumble.databinding.ItemArticleBinding
import com.example.rumble.domain.model.Article

class ArticleAdapter(
    private val context: Context,
    private val listener: ActionClickListener
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val articles: ArrayList<Article> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val views = ItemArticleBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(views)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articles[position]
        holder.itemView.setOnClickListener {
            listener.showDetails(item)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun submitUpdate(update: List<Article>) {
        val callBack = AssetDiffCallback(articles, update)
        val diffResult = DiffUtil.calculateDiff(callBack)
        articles.clear()
        articles.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val views: ItemArticleBinding) :
        RecyclerView.ViewHolder(views.root) {
        fun bind(position: Int) = views.apply {
            articles[position].also {
                it.imageURL?.let { imageUrl ->
                    Glide.with(context)
                        .load(imageUrl)
                        .error(R.drawable.ic_launcher_background)
                        .into(articleCover)
                } ?: kotlin.run {
                    Glide.with(context)
                        .load(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(articleCover)
                }
                title1.text = it.title1
                title2.text = it.title2
                keyword1.text = it.keyword1
                keyword2.text = it.keyword2
                author.text = it.authorName
                if (!it.subscriptionType.contains("free")) {
                    imageCopyright.text = "${context.getString(R.string.image_copyright_text)} ${it.imageCopyright}"
                } else {
                    imageCopyright.text = it.imageCopyright
                }

            }
        }
    }

    class AssetDiffCallback(
        private val oldAssets: List<Article>,
        private val newAssets: List<Article>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldAssets.size
        }

        override fun getNewListSize(): Int {
            return newAssets.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAssets[oldItemPosition].id == newAssets[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAssets[oldItemPosition].id == newAssets[newItemPosition].id
        }

    }

    interface ActionClickListener {
        fun showDetails(item:Article)
    }
}