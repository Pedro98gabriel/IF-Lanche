package com.cantina.iflanche.screen.fragments.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cantina.iflanche.BuildConfig
import com.cantina.iflanche.R
import com.cantina.iflanche.baseclasses.Item
import com.cantina.iflanche.databinding.FragmentRegisterProductBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.screen.HomeActivity
import com.cantina.iflanche.utils.CommonFunctions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

private const val urlFirebase: String = BuildConfig.URL_FIREBASE

class ProductFragment : Fragment() {
    private var _binding: FragmentRegisterProductBinding? = null
    private val binding get() = _binding!!
    private var itemID: String? = null // ID da categoria a ser editada

    private var btnAddProduct: Button? = null
    private var selectedCategory: String? = null
    private var selectedSubCategory: String? = null
    private lateinit var inputFields: List<View>
    private lateinit var productImage: ImageView
    private var imageUri: Uri? = null
    private lateinit var productReference: StorageReference
    private var isEditing: Boolean = false


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemID = arguments?.getString("productId")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterProductBinding.inflate(inflater, container, false)
        val view = binding.root
        btnAddProduct = binding.btnAddProduct
        inputFields = listOf(
            binding.tfProductNameContent,
            binding.tfProductDescriptionContent,
            binding.tfProductPriceContent,
            binding.tfOptionsProductCategory,
            binding.tfOptionsProductSubCategory,
        )

        (activity as? HomeActivity)?.setAppBarTitle("Cadastrar Produto")
        
        if (itemID != null) {
            isEditing = true
            binding.btnAddProduct.text = "Atualizar"
            (activity as? HomeActivity)?.setAppBarTitle("Editar Produto")

            loadProductData(itemID!!)
        }


        try {
            productReference = FirebaseStorage.getInstance().reference.child("product_images")
        } catch (e: Exception) {
            Log.e("FirebaseStorage", "Erro ao obter referência para o Firebase Storage", e)
            // Trate o erro conforme necessário, por exemplo:
            Toast.makeText(context, "Erro ao acessar o Firebase Storage", Toast.LENGTH_SHORT).show()
        }

        setupListeners()
        loadCategoriesAndSubCategories()

        productImage = binding.productImage

        productImage.setOnClickListener {
            openFileChooser()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCategoriesAndSubCategories() {
        loadItems(
            isCategory = true,
            onSuccess = { categories ->
                setupAdapter(
                    binding.tfOptionsProductCategory,
                    categories
                )
            },
            onError = { showError(it) }
        )

        loadItems(
            isCategory = false,
            onSuccess = { subCategories ->
                setupAdapter(
                    binding.tfOptionsProductSubCategory,
                    subCategories
                )
            },
            onError = { showError(it) }
        )
    }

    private fun loadItems(
        isCategory: Boolean,
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (isCategory) {
            LoadCategories.loadCategories(
                callback = { loadedCategories ->
                    if (isAdded && isResumed) onSuccess(loadedCategories)
                },
                onError = { errorMessage ->
                    if (isAdded && isResumed) onError(errorMessage)
                }
            )
        } else {
            LoadCategories.loadSubCategories(
                callback = { loadedSubCategories ->
                    if (isAdded && isResumed) onSuccess(loadedSubCategories)
                },
                onError = { errorMessage ->
                    if (isAdded && isResumed) onError(errorMessage)
                }
            )
        }
    }

    private fun setupAdapter(view: AutoCompleteTextView, items: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        view.setAdapter(adapter)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        binding.btnAddProduct.setOnClickListener {
            CommonFunctions.clearFocusFromAllFields(inputFields, requireContext())
            if (isEditing) {
                binding.progressBarRegisterProduct.visibility = ProgressBar.VISIBLE
                if (imageUri != null) {
                    uploadImageToFirebaseUpdate { newImageUrl ->
                        updateProductImage(itemID!!, newImageUrl)
                        updateProductInDB(newImageUrl)
                        binding.progressBarRegisterProduct.visibility = ProgressBar.GONE
                    }
                } else {
                    updateProductInDB()
                    binding.progressBarRegisterProduct.visibility = ProgressBar.GONE
                }
            } else {
                addProductToDB()
            }

        }

        binding.root.setOnTouchListener { _, _ ->
            CommonFunctions.clearFocusFromAllFields(inputFields, requireContext())
            false
        }

        // Adds TextWatcher and FocusChangeListener to multiple input fields to clear error messages
        // when the text changes or the field gains focus.
        CommonFunctions.addTextWatcherAndFocusListener(
            listOf(
                binding.tfProductNameContent to binding.tfProductName,
                binding.tfProductDescriptionContent to binding.tfProductDescription,
                binding.tfProductPriceContent to binding.tfProductPrice,
            )
        )

        //Hide keyboard and clear error message when gaining focus
        binding.tfOptionsProductCategory.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    CommonFunctions.hideKeyboard(view, requireContext())
                }
            }

        binding.tfOptionsProductCategory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                selectedCategory = adapterView.getItemAtPosition(position).toString()
            }
        binding.tfOptionsProductSubCategory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                selectedSubCategory = adapterView.getItemAtPosition(position).toString()
            }

        binding.tfOptionsProductCategory.setOnClickListener {
            binding.tfDropdownProductCategory.error = null
        }

        binding.tfOptionsProductSubCategory.setOnClickListener {
            binding.tfDropdownProductSubCategory.error = null
        }

    }

    private fun addProductToDB() {
        val name = binding.tfProductNameContent.text.toString().trim()
        val description = binding.tfProductDescriptionContent.text.toString().trim()
        val price = binding.tfProductPriceContent.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            binding.tfProductName.error = "Por favor, preencha o nome do produto"
            isValid = false
        }

        if (description.isEmpty()) {
            binding.tfProductDescription.error = "Por favor, preencha a descriçao do produto"
            isValid = false
        }
        if (price.isEmpty()) {
            binding.tfProductPrice.error = "Por favor, preencha o preço do produto"
            isValid = false
        }

        if (selectedCategory == null) {
            binding.tfDropdownProductCategory.error = "Selecione a categoria do produto"
            isValid = false
        }

        if (selectedSubCategory == null) {
            binding.tfDropdownProductSubCategory.error = "Selecione a Subcategoria do produto"
            isValid = false
        }

        if (imageUri == null) {
            Toast.makeText(
                context,
                "Por favor, selecione uma imagem para o produto",
                Toast.LENGTH_SHORT
            ).show()
            isValid = false
        }

        if (!isValid) {
            return
        }

        binding.progressBarRegisterProduct.visibility = ProgressBar.VISIBLE

        if (isValid) {
            uploadImageToFirebase()
        }
        binding.progressBarRegisterProduct.visibility = ProgressBar.VISIBLE


    }

    private fun clearInputFields() {
        binding.tfProductNameContent.text?.clear()
        binding.tfProductDescriptionContent.text?.clear()
        binding.tfProductPriceContent.text?.clear()
        binding.tfOptionsProductCategory.text?.clear()
        binding.tfOptionsProductSubCategory.text?.clear()
        selectedCategory = null
        selectedSubCategory = null
        imageUri = null
        binding.productImage.setImageResource(R.drawable.ic_image) // Set a placeholder image or clear the ImageView
        binding.progressBarRegisterProduct.visibility = ProgressBar.GONE

    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(productImage)
        }
    }

    private fun uploadImageToFirebase() {
        if (imageUri != null) {
            val fileReference = productReference.child(UUID.randomUUID().toString())
            fileReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        registerProduct(imageUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Falha ao fazer upload da imagem", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun uploadImageToFirebaseUpdate(onSuccess: (String) -> Unit) {
        if (imageUri != null) {
            val database: DatabaseReference =
                FirebaseDatabase.getInstance(urlFirebase).getReference("products").child(itemID!!)
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(Item::class.java)
                    product?.let {
                        val oldImageUrl = it.imageUrl
                        if (oldImageUrl.isNotEmpty()) {
                            val oldImageRef =
                                FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl)
                            oldImageRef.delete().addOnSuccessListener {
                                Log.d("FirebaseStorage", "Old image deleted successfully")
                            }.addOnFailureListener {
                                Log.e("FirebaseStorage", "Failed to delete old image", it)
                            }
                        }
                    }

                    val fileReference = productReference.child(UUID.randomUUID().toString())
                    fileReference.putFile(imageUri!!)
                        .addOnSuccessListener {
                            fileReference.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                onSuccess(imageUrl)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Falha ao fazer upload da imagem",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Erro ao carregar produto", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun registerProduct(imageUrl: String) {
        val name = binding.tfProductNameContent.text.toString()
        val description = binding.tfProductDescriptionContent.text.toString()
        val price = binding.tfProductPriceContent.text.toString()
        val selectedCategory = binding.tfOptionsProductCategory.text.toString()
        val selectedSubCategory = binding.tfOptionsProductSubCategory.text.toString()

        val product = Item(
            id = null,
            imageUrl = imageUrl,
            name = name,
            description = description,
            price = price,
            category = selectedCategory,
            subCategory = selectedSubCategory,
        )

        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance(urlFirebase)
        val storesReference: DatabaseReference = database.getReference("products")

        // Verifica se a referência "stores" existe
        storesReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // Se o nó "products" não existe, cria a referência
                    storesReference.setValue("initial_value") // Você pode definir um valor inicial se necessário
                        .addOnSuccessListener {
                            saveProductToDatabase(product, storesReference)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Falha ao criar referência 'products'",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    saveProductToDatabase(product, storesReference)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao verificar referência 'stores'", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun saveProductToDatabase(product: Item, productReference: DatabaseReference) {
        // Cria uma chave única para o novo produto
        val productID = productReference.push().key
        if (productID != null) {
            productReference.child(productID).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().supportFragmentManager.popBackStack()
                    clearInputFields()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Falha ao cadastrar o produto", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Toast.makeText(context, "Erro ao gerar o ID do produto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProductData(productId: String) {
        val database: DatabaseReference =
            FirebaseDatabase.getInstance(urlFirebase).getReference("products").child(productId)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(Item::class.java)
                product?.let {
                    binding.tfProductNameContent.setText(it.name)
                    binding.tfProductDescriptionContent.setText(it.description)
                    binding.tfProductPriceContent.setText(it.price)
                    binding.tfOptionsProductCategory.setText(it.category, false)
                    binding.tfOptionsProductSubCategory.setText(it.subCategory, false)
                    Glide.with(this@ProductFragment).load(it.imageUrl).into(binding.productImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Erro ao carregar produto", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProductInDB(newImageUrl: String? = null) {
        val name = binding.tfProductNameContent.text.toString().trim()
        val description = binding.tfProductDescriptionContent.text.toString().trim()
        val price = binding.tfProductPriceContent.text.toString().trim()
        val category = binding.tfOptionsProductCategory.text.toString().trim()
        val subCategory = binding.tfOptionsProductSubCategory.text.toString().trim()

        val updates = mutableMapOf<String, Any>(
            "name" to name,
            "description" to description,
            "price" to price,
            "category" to category,
            "subCategory" to subCategory
        )

        newImageUrl?.let {
            updates["imageUrl"] = it
        }

        val database: DatabaseReference =
            FirebaseDatabase.getInstance(urlFirebase).getReference("products").child(itemID!!)
        database.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Falha ao atualizar o produto", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProductImage(productId: String, newImageUrl: String) {
        val database: DatabaseReference =
            FirebaseDatabase.getInstance(urlFirebase).getReference("products").child(productId)
        database.child("imageUrl").setValue(newImageUrl)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Toast.makeText(context, "Falha ao atualizar a imagem", Toast.LENGTH_SHORT).show()
            }
    }

}
