package com.politecnicomalaga.mymarketlist.controller.cFirebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.politecnicomalaga.mymarketlist.controller.cSQLite.CatalogueSQLite

class RequestData(private val fromActivity: Activity) {

    //    val db = CatalogueSQLite(fromActivity).getCatalogue()
    private val db = Firebase.firestore

    fun sendCatalogue() {
        val market = CatalogueSQLite(fromActivity).getCatalogue()


        // Crear la colección 'marketData' en Firestore
        val marketDataCollection = db.collection("marketData")

        // Datos de tipos
        val typesData = hashMapOf(
            "typesList" to listOf(
                hashMapOf("typeId" to 1, "typeName" to "Aceites_Grasas"),
                hashMapOf("typeId" to 2, "typeName" to "Bebidas"),
                hashMapOf("typeId" to 3, "typeName" to "Condimentos"),
                hashMapOf("typeId" to 4, "typeName" to "Frutas"),
                hashMapOf("typeId" to 5, "typeName" to "Granos"),
                hashMapOf("typeId" to 6, "typeName" to "Lacteos"),
                hashMapOf("typeId" to 7, "typeName" to "Proteinas"),
                hashMapOf("typeId" to 8, "typeName" to "Verduras")
            )
        )

        // Agregar tipos al documento 'types'
        marketDataCollection.document("types").set(typesData).addOnSuccessListener {
        // Éxito al agregar datos de tipos
        }.addOnSuccessListener {
            Toast.makeText(fromActivity, "Subida ok", Toast.LENGTH_SHORT).show()
        // Manejar error al agregar datos de tipos
        }.addOnFailureListener{
            Toast.makeText(fromActivity, "No furula", Toast.LENGTH_SHORT).show()
            Log.e("MyFire","MyFire", it)
        }

//        // Datos de productos
//        val productsData = hashMapOf(
//            "productsList" to listOf(
//                hashMapOf("productId" to 1, "productName" to "Aceite de coco", "productType" to 1),
//                hashMapOf(
//                    "productId" to 2, "productName" to "Aceite de girasol", "productType" to 1
//                ),
//                // ... Agregar más productos según sea necesario
//            )
//        )
//
//        // Agregar productos al documento 'products'
//        marketDataCollection.document("products").set(productsData).addOnSuccessListener {
//            // Éxito al agregar datos de productos
//        }.addOnFailureListener { exception ->
//            // Manejar error al agregar datos de productos
//        }
    }
}

