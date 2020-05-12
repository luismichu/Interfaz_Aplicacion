package JSONmanager;

import Data.Dato;
import Data.Factura;
import Data.Proveedor;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JSONparser {
    private JSONparser(){}

    public static void parseFactura(Factura factura){
        JSONObject facturaJSON = new JSONObject();
        facturaJSON.put(Dato.CIF_PROVEEDOR, factura.getCIF_PROVEEDOR());
        facturaJSON.put(Dato.RAZ_PROVEEDOR, factura.getRAZ_PROVEEDOR());
        facturaJSON.put(Dato.NUM_FACTURA, factura.getNUM_FACTURA());
        facturaJSON.put(Dato.DES_FACTURA, factura.getDES_FACTURA());
        facturaJSON.put(Dato.BAS_IMPONIBLE, factura.getBAS_IMPONIBLE());
        facturaJSON.put(Dato.IVA_IMPORTE, factura.getIVA_IMPORTE());
        facturaJSON.put(Dato.TOT_IMPORTE, factura.getTOT_IMPORTE());
        facturaJSON.put(Dato.FEC_FACTURA, factura.getFEC_FACTURA().toString());
        facturaJSON.put(Dato.FEC_VENCIMIENTO, factura.getFEC_VENCIMIENTO().toString());

        try (FileWriter file = new FileWriter("Facturas/factura" + factura.getNUM_FACTURA() + ".json")) {
            file.write(facturaJSON.toJSONString());
            file.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void parseProveedor(Proveedor proveedor){
        JSONObject proveedorJSON = new JSONObject();
        proveedorJSON.put(Dato.CIF_PROVEEDOR , proveedor.getCIF_PROVEEDOR());
        proveedorJSON.put(Dato.RAZ_PROVEEDOR , proveedor.getRAZ_PROVEEDOR());
        proveedorJSON.put(Dato.REG_NOTARIAL , proveedor.getREG_NOTARIAL());
        proveedorJSON.put(Dato.SEG_RESPONSABILIDAD , proveedor.getSEG_RESPONSABILIDAD());
        proveedorJSON.put(Dato.SEG_IMPORTE  , proveedor.getSEG_IMPORTE());
        proveedorJSON.put(Dato.FEC_HOMOLOGACION , proveedor.getFEC_HOMOLOGACION().toString());

        try (FileWriter file = new FileWriter("Proveedores/proveedor" + proveedor.getCIF_PROVEEDOR() + ".json")) {
            file.write(proveedorJSON.toJSONString());
            file.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
