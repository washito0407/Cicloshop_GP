import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag4i3_FacturasG {
    private JButton REGRESARButton;
    private JTable table1;
    private JButton MOSTRARButton;
    private JButton IMPRIMIRButton;
    private JButton ELIMINARButton;
    public JPanel pag4Facturas;
    static JFrame frameFacturasP = new JFrame("Facturas");
    ConexionDB conexionDB = new ConexionDB();

    public Pag4i3_FacturasG() {
        actualizarTabla();
        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameFacturasP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
        ELIMINARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada!=-1){
                    int idFilaSeleccionada = Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString());
                    Connection connection = conexionDB.ConexionLocal();
                    try {
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM Facturas WHERE factura_id=?");
                        ps.setInt(1,idFilaSeleccionada);
                        ps.executeUpdate();
                        actualizarTabla();
                    }catch (SQLException ex){
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }

                }
            }
        });
        MOSTRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada!=-1){
                    int idFilaSeleccionda=Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString());
                    Connection connection = conexionDB.ConexionLocal();
                    try{
                        PreparedStatement ps = connection.prepareStatement("SELECT F.factura_id, F.fecha, C.nombre_cln AS Nombre_Cliente, \n" +
                                "       P.nombre_prd AS Nombre_Producto, D.cantidad_producto, P.precio_venta,\n" +
                                "       (D.cantidad_producto* P.precio_venta) AS Subtotal\n" +
                                "FROM Facturas F\n" +
                                "JOIN Detalle D ON F.factura_id = D.factura_id\n" +
                                "JOIN Productos P ON D.producto_id= P.producto_id\n" +
                                "JOIN Clientes C ON F.cliente_id = C.cliente_id\n" +
                                "WHERE F.factura_id=?");
                        ps.setInt(1,idFilaSeleccionda);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()){
                            System.out.println("Nombre del producto: "+rs.getString("Nombre_Producto"));
                            System.out.println("Cantidad: "+rs.getInt("cantidad_producto"));
                            System.out.println("Precio: "+rs.getDouble("precio_venta"));
                            System.out.println("Subtotal: "+rs.getDouble("Subtotal"));
                        }
                    }catch (SQLException ex){
                        JOptionPane.showMessageDialog(null,ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    public void actualizarTabla(){
        try {
            Connection connection = conexionDB.ConexionLocal();
            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Facturas");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Factura ID");
            modelo.addColumn("Fecha");
            modelo.addColumn("Hora");
            modelo.addColumn("Cliente ID");
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

    }
}
