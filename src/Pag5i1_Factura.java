import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag5i1_Factura {
    private JTable table1;
    private JFormattedTextField formattedTextField1;
    private JButton FACTURARButton;
    private JButton GENERARFACTURAButton;
    public JPanel pag5FacturaPanel;
    private JButton regresarButton;
    static JFrame frameFacturaCompra = new JFrame("Compra facturada");
    ConexionDB conexionDB=new ConexionDB();

    public Pag5i1_Factura() {
        try {
            Connection connection = conexionDB.ConexionLocal();
            int ultimaFactura = 0;

            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement factura = connection.prepareStatement("SELECT MAX(factura_id) AS idFactura FROM Facturas");
            ResultSet rsFactura = factura.executeQuery();
            if (rsFactura.next()){
                ultimaFactura = rsFactura.getInt(1);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT F.factura_id, F.fecha, C.nombre_cln AS Nombre_Cliente, \n" +
                    "       P.nombre_prd AS Nombre_Producto, D.cantidad_producto, P.precio_venta,\n" +
                    "       (D.cantidad_producto* P.precio_venta) AS Subtotal\n" +
                    "FROM Facturas F\n" +
                    "JOIN Detalle D ON F.factura_id = D.factura_id\n" +
                    "JOIN Productos P ON D.producto_id= P.producto_id\n" +
                    "JOIN Clientes C ON F.cliente_id = C.cliente_id\n" +
                    "WHERE F.factura_id= ?;");
            preparedStatement.setInt(1,ultimaFactura);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Factura ID");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cliente");
            modelo.addColumn("Producto");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Precio Unitario");
            modelo.addColumn("Subtotal");
            while (rs.next()){
                Object[] filas = new Object[cantidadColumnas];
                for (int i=0;i<cantidadColumnas;i++){
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFacturaCompra.setVisible(false);
                Pag5_Compra.frameCompra.setVisible(true);
            }
        });
    }
}
