package com.example.grocerylist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylist.model.Product
import com.example.grocerylist.model.Suggestion
import kotlinx.android.synthetic.main.fragment_products_list.*


class ProductsFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAutoCompleteClickListener()
        setOkButtonClickListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_products_list, container, false)

        adapter = ProductsAdapter()
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        productViewModel.allProducts.observe(this, Observer { products ->
            products?.let {
                adapter.setProductsData(products)

                val itemTouchHelper = ItemTouchHelper(initializeSwipeCallback(products as ArrayList<Product>))
                itemTouchHelper.attachToRecyclerView(recyclerView)
            }
        })

        adapter.setClickListener(object: ProductsAdapter.ClickListener {
            override fun onItemClick(product: Product?) {
                if (product?.checked!!){
                    productViewModel.update(false, product.name)
                } else {
                    productViewModel.update(true, product.name)
                }
            }
        })

        productViewModel.allSuggestions.observe(this, Observer { suggestions ->
            suggestions?.let {
                val list = formatSuggestionData(it)
                val adapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, list)
                addProductTv.setAdapter(adapter)
            }
        })

        return root
    }

    private fun showKeyboard(context: Context) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.advanced_btns_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.removeAllProducts -> productViewModel.removeAllProducts()
            R.id.removeCheckedProducts -> productViewModel.removeCheckedProducts()
            R.id.checkAllProducts -> productViewModel.updateAllProductsState(true)
            R.id.uncheckAllProducts -> productViewModel.updateAllProductsState(false)
        }

        return true
    }

    private fun setAutoCompleteClickListener(){
        addProductTv.onItemClickListener = object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val name = formatString(parent?.getItemAtPosition(position).toString())
                val product = Product(name, false)
                productViewModel.insert(product)
                addProductTv.setText(getString(R.string.empty_value))
            }
        }
    }

    private fun initializeSwipeCallback(products: ArrayList<Product>): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val product = products[position]
                val productId: Int = product.id

                if (direction == ItemTouchHelper.RIGHT){
                    products.removeAt(position)
                    productViewModel.removeProductById(productId)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
    }

    private fun setOkButtonClickListener(){
        okBtn.setOnClickListener {
            if (addProductTv.text.isEmpty()){
                addProductTv.requestFocus()
                showKeyboard(activity!!.applicationContext)
            }
        }

        addProductTv.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            @SuppressLint("RestrictedApi")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (addProductTv.text.isNotEmpty()){
                    okBtn.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp)
                    okBtn.visibility = View.VISIBLE
                    okBtn.setOnClickListener {
                        val name = formatString(addProductTv.text.toString())
                        val product = Product(name, false)
                        productViewModel.insert(product)
                        addProductTv.setText("")
                    }
                } else {
                    okBtn.setImageResource(R.drawable.ic_shopping_cart_white_24dp)
                }
            }
        })
    }

    private fun formatString(name: String): String{
        val firstLetter = name.substring(0, 1).toUpperCase()

        return firstLetter + name.substring(1)
    }

    private fun formatSuggestionData(suggestions: List<Suggestion>): MutableList<String>{
        val list: MutableList<String> = mutableListOf()
        var position = 0

        for (suggestion in suggestions){
            list.add(position, suggestion.name_rus)
            position++
        }

        return list
    }
}
