package com.cantina.iflanche.screen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cantina.iflanche.ProdutoAdapter
import com.cantina.iflanche.R
import com.cantina.iflanche.baseclasses.Item
import com.cantina.iflanche.databinding.FragmentCategoryClickItemBinding
import com.cantina.iflanche.firebase.LoadProducts
import com.cantina.iflanche.screen.HomeActivity
import com.cantina.iflanche.screen.fragments.admin.ClickProductItemFragment

class CategoryClickItemFragment : Fragment() {
    private var _binding: FragmentCategoryClickItemBinding? = null
    private val binding get() = _binding!!
    private val produtosList = mutableListOf<Item>()
    private lateinit var produtoAdapter: ProdutoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryClickItemBinding.inflate(inflater, container, false)
        val view = binding.root

        val categoryName = arguments?.getString("categoryName")
        (activity as? HomeActivity)?.setAppBarTitle(categoryName.toString())

        produtoAdapter = ProdutoAdapter(produtosList) { item ->
            onProductClick(item)
        }
        binding.recyclerViewProductsItemClicked.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewProductsItemClicked.adapter = produtoAdapter

        carregarProdutos(categoryName)

        return view
    }

    private fun carregarProdutos(categoryName: String?) {
        if (categoryName == null) return

        LoadProducts.loadProdutos(
            callback = { subcategoryMap ->
                val produtos =
                    subcategoryMap.values.flatten().filter { it.category == categoryName }
                produtosList.clear()
                produtosList.addAll(produtos)
                produtoAdapter.notifyDataSetChanged()

                if (produtosList.isEmpty()) {
                    binding.tvNoProducts.visibility = View.VISIBLE
                } else {
                    binding.tvNoProducts.visibility = View.GONE
                }
            },
            onError = { errorMessage ->
                Log.e("CategoryClickItemFragment", errorMessage)
            }
        )
    }

    private fun onProductClick(item: Item) {
        val fragment = ClickProductItemFragment()
        val bundle = Bundle()
        bundle.putString("productId", item.id)
        bundle.putString("productName", item.name)
        bundle.putString("productPrice", item.price)
        bundle.putString("productImageUrl", item.imageUrl)
        bundle.putString("productCategory", item.category)
        bundle.putString("productSubCategory", item.subCategory)
        bundle.putString("productDescription", item.description)
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}