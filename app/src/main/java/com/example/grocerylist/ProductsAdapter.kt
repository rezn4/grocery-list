package com.example.grocerylist

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylist.model.Product

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private var productList: List<Product>? = null

    interface ClickListener {
        fun onItemClick(product: Product?)
    }

    private lateinit var clickListener: ClickListener

    fun setClickListener(clickListener: ClickListener){
        this.clickListener = clickListener
    }

    fun setProductsData(list: List<Product>){
        productList = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = productList?.size ?: 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = productList?.get(position)
        val context = holder.name.context
        holder.name.text = product?.name

        product?.let {
            if (it.checked){
                holder.colorIndicator.setBackgroundColor(context.getColor(R.color.unselected_item))
                holder.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.colorIndicator.setBackgroundColor(context.getColor(R.color.colorPrimary))
                holder.name.paintFlags = holder.name.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        if (position % 2 == 0){
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.alabaster))
        } else {
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.white))
        }

        holder.name.setOnClickListener {
            clickListener.onItemClick(product)
        }

    }

    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val linearLayout: LinearLayout = view.findViewById(R.id.layout)
        val name: TextView = view.findViewById(R.id.product_name)
        val colorIndicator: View = view.findViewById(R.id.color_indicator)
    }
}

