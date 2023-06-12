package com.politecnicomalaga.mymarketlist

import com.politecnicomalaga.mymarketlist.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //Activación -> constructores y setters


        //Funcionamientos -> se llama a toString, getters, etc...


        //SE hacen Assert

        assertEquals(4, 2 + 2)
    }

    // HAcer una fun nueva, con la anotación @test, donde se instancie un Product, se usen los
    //getters y se hagan asserts.
    @Test
    fun pruebaProduct() {
        //Activación -> constructores y setters
//        val myProduct = Product("prueba")
//        val myProduct2 = Product("prueba")
        //Funcionamientos -> se llama a toString, getters, etc...
//        assertTrue( myProduct.compareTo(myProduct2)==0)

        //SE hacen Assert

        assertEquals(4, 2 + 2)
    }

    // Hacer una fun nueva...... pero con List
}