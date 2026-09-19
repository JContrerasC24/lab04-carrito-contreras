package com.contreras.lab04carritotecsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                PantallaCarrito()
            }
        }
    }
}

@Composable
fun PantallaCarrito() {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productos = remember {
        mutableStateListOf<Producto>()
    }

    val subtotal = productos.sumOf {
        it.precio * it.cantidad
    }

    val igv = subtotal * 0.18
    val total = subtotal + igv

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mi Carrito TECSUP",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            label = {
                Text("Nombre del producto")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                value = precio,
                onValueChange = {
                    precio = it
                },
                label = {
                    Text("Precio (S/)")
                },
                modifier = Modifier.weight(1f)
            )

            TextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                },
                label = {
                    Text("Cantidad")
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            onClick = {

                val precioNum =
                    precio.toDoubleOrNull() ?: 0.0

                val cantidadNum =
                    cantidad.toIntOrNull() ?: 0

                if (
                    nombre.isNotBlank() &&
                    precioNum > 0 &&
                    cantidadNum > 0
                ) {

                    productos.add(
                        Producto(
                            nombre = nombre,
                            precio = precioNum,
                            cantidad = cantidadNum
                        )
                    )

                    nombre = ""
                    precio = ""
                    cantidad = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (productos.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Tu carrito está vacío",
                        color = Color.Gray
                    )

                    Text(
                        text = "Agrega tu primer producto",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                items(productos) { producto ->

                    TarjetaProducto(
                        producto = producto,
                        onEliminar = {
                            productos.remove(producto)
                        }
                    )
                }
            }
        }

        PanelTotales(
            cantidadProductos = productos.size,
            subtotal = subtotal,
            igv = igv,
            total = total
        )
    }
}


@Composable
fun TarjetaProducto(
    producto: Producto,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = producto.nombre,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "S/ %.2f x %d".format(
                        producto.precio,
                        producto.cantidad
                    ),
                    color = Color.Gray
                )
            }

            Text(
                text = "S/ %.2f".format(
                    producto.precio *
                            producto.cantidad
                ),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = onEliminar
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Delete,
                    contentDescription = "Eliminar"
                )
            }
        }
    }
}


@Composable
fun PanelTotales(
    cantidadProductos: Int,
    subtotal: Double,
    igv: Double,
    total: Double
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F0F8))
            .padding(16.dp)
    ) {

        Text(
            text = "Productos: $cantidadProductos"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("Subtotal")

            Text(
                "S/ %.2f".format(subtotal)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text("IGV (18%)")

            Text(
                "S/ %.2f".format(igv)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "S/ %.2f".format(total),
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}