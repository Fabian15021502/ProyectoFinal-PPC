package com.example.proyectofinal_ppc.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.test.espresso.base.Default
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

    val editingProduct = remember(state.products, productId) {
        state.products.find { it.id == productId }
    }

    var name by remember { mutableStateOf(editingProduct?.name ?: "") }
    var description by remember { mutableStateOf(editingProduct?.description ?: "") }
    var priceText by remember { mutableStateOf(editingProduct?.price?.toString() ?: "") }
    var stockText by remember { mutableStateOf(editingProduct?.stock?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(editingProduct?.imageUrl ?: "") }

    var categoryId by remember { mutableStateOf(editingProduct?.categoryId ?: "") }
    var catMenuExpanded by remember { mutableStateOf(false) }

    var showNewCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var newCategoryDescription by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (state.categories.isEmpty()) {
            storeViewModel.loadCategoriesAndProducts()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (productId == null) "Nuevo producto" else "Editar producto",
                        style = MaterialTheme.typography.titleLarge
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

                                storeViewModel.saveProduct(
                                    id = productId,
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Sección: Información básica
            Text(
                "Información básica",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del producto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }

            // Sección: Inventario y precio
            Text(
                "Inventario y precio",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("Precio") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it },
                        label = { Text("Stock") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            // Sección: Clasificación
            Text(
                "Clasificación",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("URL de imagen (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
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
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
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
                        Text("Crear nueva categoría")
                    }
                }
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
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCategoryDescription,
                        onValueChange = { newCategoryDescription = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
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
