package sample;

import Data.Factura;
import Data.Proveedor;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static Connection connection;

    private static final String CREATE_TABLE_PROV_COMP = "create table if not exists PROV_COMP(" +
            "CIF_PROVEEDOR varchar(50) primary key," +
            "RAZ_PROVEEDOR varchar(100)," +
            "REG_NOTARIAL int," +
            "SEG_RESPONSABILIDAD varchar(50)," +
            "SEG_IMPORTE float," +
            "FEC_HOMOLOGACION date);";

    private static final String CREATE_TABLE_FACT_PROV = "create table if not exists FACT_PROV(" +
            "CIF_PROVEEDOR varchar(50)," +
            "RAZ_PROVEEDOR varchar(50)," +
            "NUM_FACTURA int primary key," +
            "DES_FACTURA varchar(100)," +
            "BAS_IMPONIBLE float," +
            "IVA_IMPORTE float," +
            "TOT_IMPORTE float," +
            "FEC_FACTURA date," +
            "FEC_VENCIMIENTO date);";

    private static final String INSERT_TABLE_PROV_COMP = "insert into PROV_COMP (" +
            "CIF_PROVEEDOR," +
            "RAZ_PROVEEDOR," +
            "REG_NOTARIAL," +
            "SEG_RESPONSABILIDAD," +
            "SEG_IMPORTE," +
            "FEC_HOMOLOGACION) " +
            "values (?, ?, ?, ?, ?, ?);";

    private static final String INSERT_TABLE_FACT_PROV = "insert into FACT_PROV (" +
            "CIF_PROVEEDOR," +
            "RAZ_PROVEEDOR," +
            "NUM_FACTURA," +
            "DES_FACTURA," +
            "BAS_IMPONIBLE," +
            "IVA_IMPORTE," +
            "TOT_IMPORTE," +
            "FEC_FACTURA," +
            "FEC_VENCIMIENTO) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String DROP_TABLE_PROV_COMP = "drop table if exists PROV_COMP";
    private static final String DROP_TABLE_FACT_PROV = "drop table if exists FACT_PROV";

    private DataBase(){}

    public static void drop(){
        try {
            loadDatabase();

            String sql = DROP_TABLE_FACT_PROV;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = DROP_TABLE_PROV_COMP;
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = CREATE_TABLE_PROV_COMP;
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = CREATE_TABLE_FACT_PROV;
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insertFactura(Factura factura){
        try {
            loadDatabase();

            String sql = CREATE_TABLE_FACT_PROV;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = INSERT_TABLE_FACT_PROV;
            statement = connection.prepareStatement(sql);
            statement.setString(1, factura.getCIF_PROVEEDOR());
            statement.setString(2, factura.getRAZ_PROVEEDOR());
            statement.setInt(3, factura.getNUM_FACTURA());
            statement.setString(4, factura.getDES_FACTURA());
            statement.setFloat(5, factura.getBAS_IMPONIBLE());
            statement.setFloat(6, factura.getIVA_IMPORTE());
            statement.setFloat(7, factura.getTOT_IMPORTE());
            statement.setDate(8, new Date(factura.getFEC_FACTURA().getTime()));
            statement.setDate(9, new Date(factura.getFEC_VENCIMIENTO().getTime()));
            statement.executeUpdate();

            statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            Controller.error("La factura ya existe");
        }
    }

    public static void insertProveedor(Proveedor proveedor){
        try {
            loadDatabase();

            String sql = CREATE_TABLE_PROV_COMP;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = INSERT_TABLE_PROV_COMP;
            statement = connection.prepareStatement(sql);
            statement.setString(1, proveedor.getCIF_PROVEEDOR());
            statement.setString(2, proveedor.getRAZ_PROVEEDOR());
            statement.setInt(3, proveedor.getREG_NOTARIAL());
            statement.setString(4, proveedor.getSEG_RESPONSABILIDAD());
            statement.setFloat(5, proveedor.getSEG_IMPORTE());
            statement.setDate(6, new Date(proveedor.getFEC_HOMOLOGACION().getTime()));
            statement.executeUpdate();

            statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            Controller.error("El proveedor ya existe");
        }
    }

    public static void updateProveedor(Proveedor proveedor){
        try {
            loadDatabase();

            String sql = CREATE_TABLE_PROV_COMP;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "update PROV_COMP set RAZ_PROVEEDOR = (?)," +
                    "REG_NOTARIAL = (?)," +
                    "SEG_RESPONSABILIDAD = (?)," +
                    "SEG_IMPORTE = (?)," +
                    "FEC_HOMOLOGACION =(?) " +
                    "where CIF_PROVEEDOR = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, proveedor.getRAZ_PROVEEDOR());
            statement.setInt(2, proveedor.getREG_NOTARIAL());
            statement.setString(3, proveedor.getSEG_RESPONSABILIDAD());
            statement.setFloat(4, proveedor.getSEG_IMPORTE());
            statement.setDate(5, new Date(proveedor.getFEC_HOMOLOGACION().getTime()));
            statement.setString(6, proveedor.getCIF_PROVEEDOR());
            statement.executeUpdate();

            statement.close();
            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            Controller.error("El proveedor ya existe");
        }
    }

    public static void update(int position, int level){
        try {
            loadDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "UPDATE gameStates SET level = (?) WHERE position = (?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, level);
            statement.setInt(2, position);
            statement.executeUpdate();

            statement.close();

            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static void delete(int position){
        try {
            loadDatabase();

            String sql = "CREATE TABLE IF NOT EXISTS gameStates (position int primary key, name text, level int)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM gameStates WHERE position = (?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, position);
            statement.executeUpdate();

            statement.close();

            if (connection != null)
                connection.close();
        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static void eliminarFactura(int num){
        try {
            loadDatabase();

            String sql = CREATE_TABLE_FACT_PROV;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM FACT_PROV WHERE NUM_FACTURA = (?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, num);
            if(statement.executeUpdate() == 0)
                throw new Exception("No se ha eliminado ninguna factura");
            statement.close();

            if (connection != null)
                connection.close();
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static void eliminarProveedor(String cif){
        try {
            loadDatabase();

            String sql = CREATE_TABLE_PROV_COMP;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM PROV_COMP WHERE CIF_PROVEEDOR = (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, cif);
            if(statement.executeUpdate() == 0)
                throw new Exception("No se ha eliminado ningun proveedor");

            statement.close();

            if (connection != null)
                connection.close();
        } catch (Exception cnfe) {
            cnfe.printStackTrace();
        }
    }

    public static Factura getFactura(int num){
        try {
            loadDatabase();

            String sql = "SELECT * FROM FACT_PROV WHERE NUM_FACTURA = (?) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, num);
            ResultSet result = statement.executeQuery();

            if(result.isClosed())
                return null;

            Factura factura = new Factura(result.getString("CIF_PROVEEDOR"),
                    result.getString("RAZ_PROVEEDOR"),
                    result.getInt("NUM_FACTURA"),
                    result.getString("DES_FACTURA"),
                    result.getFloat("BAS_IMPONIBLE"),
                    result.getFloat("IVA_IMPORTE"),
                    result.getFloat("TOT_IMPORTE"),
                    result.getDate("FEC_FACTURA"),
                    result.getDate("FEC_VENCIMIENTO"));

            statement.close();
            result.close();
            connection.close();

            return factura;

        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Factura> getFacturas(){
        try {
            loadDatabase();

            String sql = "SELECT * FROM FACT_PROV order by NUM_FACTURA asc";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            ArrayList<Factura> facturas = new ArrayList<>();

            if(result.isClosed())
                return null;
            while (result.next()) {
                Factura factura = new Factura(result.getString("CIF_PROVEEDOR"),
                        result.getString("RAZ_PROVEEDOR"),
                        result.getInt("NUM_FACTURA"),
                        result.getString("DES_FACTURA"),
                        result.getFloat("BAS_IMPONIBLE"),
                        result.getFloat("IVA_IMPORTE"),
                        result.getFloat("TOT_IMPORTE"),
                        result.getDate("FEC_FACTURA"),
                        result.getDate("FEC_VENCIMIENTO"));
                facturas.add(factura);
            }
            statement.close();
            result.close();
            connection.close();

            return facturas;

        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }

        return null;
    }

    public static Proveedor getProveedor(String cif){
        try {
            loadDatabase();

            String sql = "SELECT * FROM PROV_COMP WHERE CIF_PROVEEDOR = (?) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cif);
            ResultSet result = statement.executeQuery();

            if(result.isClosed())
                return null;

            Proveedor proveedor = new Proveedor(result.getString("CIF_PROVEEDOR"),
                    result.getString("RAZ_PROVEEDOR"),
                    result.getInt("REG_NOTARIAL"),
                    result.getString("SEG_RESPONSABILIDAD"),
                    result.getFloat("SEG_IMPORTE"),
                    result.getDate("FEC_HOMOLOGACION"));

            statement.close();
            result.close();
            connection.close();

            return proveedor;

        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Proveedor> getProveedores(){
        try {
            loadDatabase();

            String sql = "SELECT * FROM PROV_COMP";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            ArrayList<Proveedor> proveedores = new ArrayList<>();

            if(result.isClosed())
                return null;
            while(result.next()) {
                Proveedor proveedor = new Proveedor(result.getString("CIF_PROVEEDOR"),
                        result.getString("RAZ_PROVEEDOR"),
                        result.getInt("REG_NOTARIAL"),
                        result.getString("SEG_RESPONSABILIDAD"),
                        result.getFloat("SEG_IMPORTE"),
                        result.getDate("FEC_HOMOLOGACION"));

                proveedores.add(proveedor);
            }
            statement.close();
            result.close();
            connection.close();

            return proveedores;

        } catch (SQLException cnfe) {
            cnfe.printStackTrace();
        }

        return null;
    }

    private static void loadDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}