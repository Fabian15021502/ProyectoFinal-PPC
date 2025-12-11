package com.example.proyectofinal_ppc.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal_ppc.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProductScreen(
    navController: NavController,
    storeViewModel: StoreViewModel,
    productId: String?
) {
    val scope = rememberCoroutineScope()
    val state by storeViewModel.state.collectAsState()

    // Producto que estamos editando (si existe)
    val editingProduct = remember(state.products, productId) {
        state.products.find { it.id == productId }
    }

    // Campos del formulario (estado local)
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var catMenuExpanded by remember { mutableStateOf(false) }

    // Nueva categoría
    var showNewCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryDescription by remember { mutableStateOf("") }

    // Cargar categorías si aún no están
    LaunchedEffect(Unit) {
        if (state.categories.isEmpty()) {
            storeViewModel.loadCategoriesAndProducts()
        }
    }

    // Sincronizar los campos cuando cambie el producto a editar
    LaunchedEffect(editingProduct?.id) {
        if (editingProduct != null) {
            // MODO EDITAR
            name = editingProduct.name
            description = editingProduct.description
            priceText = editingProduct.price.toString()
            stockText = editingProduct.stock.toString()
            imageUrl = editingProduct.imageUrl
            categoryId = editingProduct.categoryId
        } else {
            // MODO NUEVO
            name = ""
            description = ""
            priceText = ""
            stockText = ""
            imageUrl = ""
            categoryId = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (editingProduct == null) "Nuevo producto"
                        else "Editar producto"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                val price = priceText.toDoubleOrNull() ?: 0.0
                                val stock = stockText.toIntOrNull() ?: 0

                                if (name.isBlank() || categoryId.isBlank()) return@launch

                                val idToUse = editingProduct?.id // null si es nuevo

                                storeViewModel.saveProduct(
                                    id = idToUse,
                                    name = name,
                                    description = description,
                                    price = price,
                                    stock = stock,
                                    imageUrl = imageUrl,
                                    categoryId = categoryId
                                )

                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceText,
                onValueChange = { priceText = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = stockText,
                onValueChange = { stockText = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // Como acordamos, por ahora no usamos imágenes en UI,
            // pero dejamos el campo por si quieres seguir guardando la URL.
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría", style = MaterialTheme.typography.labelLarge)

            ExposedDropdownMenuBox(
                expanded = catMenuExpanded,
                onExpandedChange = { catMenuExpanded = !catMenuExpanded }
            ) {
                val selectedName =
                    state.categories.find { it.id == categoryId }?.name
                        ?: "Selecciona una categoría"

                OutlinedTextField(
                    value = selectedName,
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = catMenuExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = catMenuExpanded,
                    onDismissRequest = { catMenuExpanded = false }
                ) {
                    state.categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                categoryId = cat.id
                                catMenuExpanded = false
                            }
                        )
                    }
                }
            }

            TextButton(
                onClick = {
                    newCategoryName = ""
                    newCategoryDescription = ""
                    showNewCategoryDialog = true
                }
            ) {
                Text("Nueva categoría")
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }

    if (showNewCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showNewCategoryDialog = false },
            title = { Text("Nueva categoría") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nombre") }
                    )
                    OutlinedTextField(
                        value = newCategoryDescription,
                        onValueChange = { newCategoryDescription = it },
                        label = { Text("Descripción") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newCategoryName.isNotBlank()) {
                            scope.launch {
                                val newCatId = storeViewModel.createCategory(
                                    name = newCategoryName,
                                    description = newCategoryDescription
                                )
                                storeViewModel.loadCategoriesAndProducts()
                                categoryId = newCatId
                                showNewCategoryDialog = false
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewCategoryDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
