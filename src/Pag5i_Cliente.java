import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class Pag5i_Cliente {
    private JTextField correoField;
    private JTable table1;
    private JButton SELECCIONARUSUARIOButton;
    private JButton CREARUSUARIOButton;
    private JTextField telefonoField;
    private JTextField nombreField;
    private JTextField direccionField;
    public JPanel clientePanel;
    private JButton regresarButton;
    ConexionDB conexionDB=new ConexionDB();
    static JFrame frameCliente = new JFrame("Clientes");

    public Pag5i_Cliente() {
        actualizarTabla();
        SELECCIONARUSUARIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada!=-1){
                    int idCliente = Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString());
                    try {
                        Connection connection = conexionDB.ConexionLocal();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO VENTAS(id_cliente, id_cajero) VALUES (?,?)");
                        ps.setInt(1,idCliente);
                        ps.setInt(2,Pag3_Login.idCajeroActual);
                        ps.executeUpdate();

                        PreparedStatement obtenerUltimaVenta = connection.prepareStatement("SELECT MAX(id_v) FROM VENTAS");
                        ResultSet rs = obtenerUltimaVenta.executeQuery();
                        rs.next();
                        int ultimaVenta = rs.getInt("MAX(id_v)");
                        PreparedStatement generarFactura = connection.prepareStatement("INSERT INTO DETALLES(id_v, id_p, cantidad_p) VALUES (?,?,?)");
                        for (int i = 0; i< Pag5_Compra.modeloCarrito.getRowCount(); i++){
                            generarFactura.setInt(1, ultimaVenta);
                            generarFactura.setInt(2,Integer.parseInt(Pag5_Compra.modeloCarrito.getValueAt(i,0).toString()));
                            generarFactura.setInt(3,Integer.parseInt(Pag5_Compra.modeloCarrito.getValueAt(i,3).toString()));
                            generarFactura.executeUpdate();
                            Pag5_Compra.modeloCarrito.removeRow(i);
                        }
                        JOptionPane.showMessageDialog(null,"Se ha realizado la compra correctamente");

                        Pag5_Compra.frameCompra.setVisible(true);
                        frameCliente.dispose();

                        Pag5i1_Factura factura = new Pag5i1_Factura();
                        factura.obtenerFactura(ultimaVenta);

                        if (!Pag5i1_Factura.frameFacturaCompra.isUndecorated()){
                            Pag5i1_Factura.frameFacturaCompra.setUndecorated(true);
                        }
                        Pag5i1_Factura.frameFacturaCompra.setContentPane(factura.pag5FacturaPanel);
                        Pag5i1_Factura.frameFacturaCompra.setSize(500,400);
                        Pag5i1_Factura.frameFacturaCompra.setVisible(true);
                        Pag5i1_Factura.frameFacturaCompra.setLocationRelativeTo(null);
                    }catch (SQLException ex){
                        JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage(),"SQL ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione un cliente","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        CREARUSUARIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nombreField.getText().isEmpty() || telefonoField.getText().isEmpty() || correoField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Todos los campos deben estar completados");
                }else{
                    try {
                        Connection connection = conexionDB.ConexionLocal();
                        PreparedStatement ps = connection.prepareStatement("INSERT INTO CLIENTES(nombre, correo, telefono, direccion) VALUES (?,?,?,?)");
                        ps.setString(1,nombreField.getText());
                        ps.setString(2, correoField.getText());
                        ps.setString(3, telefonoField.getText());
                        ps.setString(4, direccionField.getText());
                        ps.executeUpdate();
                        actualizarTabla();
                        JOptionPane.showMessageDialog(null,"Se han ingresado los datos correctamente");
                        nombreField.setText("");
                        correoField.setText("");
                        telefonoField.setText("");
                        direccionField.setText("");
                    }catch (SQLException exception){
                        JOptionPane.showMessageDialog(null,exception.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCliente.setVisible(false);
                Pag5_Compra.frameCompra.setVisible(true);
            }
        });
    }
    public void actualizarTabla(){
        try {
            Connection connection = conexionDB.ConexionLocal();

            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLIENTES");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Cliente ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Email");
            modelo.addColumn("Telefono");
            modelo.addColumn("Direccion");
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
