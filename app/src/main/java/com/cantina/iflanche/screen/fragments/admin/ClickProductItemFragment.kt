package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cantina.iflanche.BuildConfig
import com.cantina.iflanche.R
import com.cantina.iflanche.baseclasses.Item
import com.cantina.iflanche.databinding.FragmentClickProductItemBinding
import com.cantina.iflanche.firebase.DeleteProduct
import com.cantina.iflanche.screen.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val urlFirebase: String = BuildConfig.URL_FIREBASE

class ClickProductItemFragment : Fragment() {
    private var _binding: FragmentClickProductItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClickProductItemBinding.inflate(inflater, container, false)
        val view = binding.root

        val userType = (activity as? HomeActivity)?.getUserType()
        if (userType != "Administrador") {
            binding.btnEditProduct.visibility = View.GONE
            binding.btnRemoveProduct.visibility = View.GONE
        }
        loadProductData()

        binding.btnRemoveProduct.setOnClickListener {
            val productId = arguments?.getString("productId")
            if (productId != null) {
                showDeleteAlertDialog(productId)
            } else {
                Toast.makeText(requireContext(), "Product ID not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEditProduct.setOnClickListener {
            val productId = arguments?.getString("productId")
            val productName = arguments?.getString("productName")
            val productPrice = arguments?.getString("productPrice")
            val productImageUrl = arguments?.getString("productImageUrl")
            val productCategory = arguments?.getString("productCategory")
            val productSubCategory = arguments?.getString("productSubCategory")
            val productDescription = arguments?.getString("productDescription")
            if (productId != null && productName != null && productPrice != null && productImageUrl != null && productCategory != null && productSubCategory != null && productDescription != null) {
                navigateToEditProductFragment(productId)
            } else {
                Toast.makeText(requireContext(), "Product details not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return view
    }

    private fun loadProductData() {
        val productId = arguments?.getString("productId")
        if (productId != null) {
            val database: DatabaseReference =
                FirebaseDatabase.getInstance(urlFirebase).getReference("products").child(productId)
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(Item::class.java)
                    product?.let {
                        binding.textViewProductItemClickName.text = it.name
                        binding.textViewProductItemClickPrice.text = "R$ ${it.price}"
                        binding.textViewProductItemClickDescription.text = it.description
                        binding.textViewProductItemClickCategoryText.text = it.category
                        binding.textViewProductItemClickSubCategoryText.text = it.subCategory
                        Glide.with(this@ClickProductItemFragment).load(it.imageUrl)
                            .into(binding.imageViewProductItemClick)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Erro ao carregar produto", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Product ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteAlertDialog(productId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Apagar")
            .setMessage("O produto será apagado, deseja continuar?")
            .setNegativeButton("Não") { dialog, which ->
            }
            .setPositiveButton("Sim") { dialog, which ->
                deleteProduct(productId)
            }
            .show()
    }

    private fun deleteProduct(productId: String) {
        DeleteProduct.deleteProduct(
            productId,
            onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Produto removido com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStack()
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToEditProductFragment(
        productId: String,
    ) {
        val fragment = ProductFragment()
        val bundle = Bundle()
        bundle.putString("productId", productId)
        fragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        loadProductData()
    }
}