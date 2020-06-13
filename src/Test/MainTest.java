package Test;

import Data.Factura;
import Data.Proveedor;
import JSONmanager.JSONreader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.DataBase;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void test1(){
        assertDoesNotThrow(() -> DataBase.eliminarFactura(-1));
    }

    @Test
    void test2(){
        assertDoesNotThrow(() -> DataBase.eliminarProveedor(""));
    }

    @Test
    void test3(){
        assertDoesNotThrow(DataBase::drop);
    }

    @Test
    void test4(){
        assertThrows(NullPointerException.class, () -> DataBase.insertFactura(null));
    }

    @Test
    void test5(){
        assertThrows(NullPointerException.class, () -> DataBase.insertProveedor(null));
    }

    @Test
    void test6(){
        fail("Factura con datos incorrectos");
        //new Factura(1, 1, 1, 1, 1, 1, 1, 1, 1);
    }

    @Test
    void test7(){
        fail("Proveedor con datos incorrectos");
        //new Proveedor(1, 1, 1, 1, 1, 1);
    }

    @Test
    void test8(){
        assertThrows(AssertionError.class, () -> JSONreader.readFactura(""));
    }
}