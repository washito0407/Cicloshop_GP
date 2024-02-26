import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class Pag5i_Cliente {
    private JTextField nombreField;
    private JTable table1;
    private JButton SELECCIONARUSUARIOButton;
    private JButton CREARUSUARIOButton;
    private JTextField apellidoField;
    private JTextField direccionField;
    private JTextField celularField;
    private JTextField cedulaField;
    private JTextField emailField;
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
                int ultimaFactura=0;
                if (filaSeleccionada != -1){
                    try {
                        Connection connection = conexionDB.ConexionLocal();
                        PreparedStatement factura = connection.prepareStatement("SELECT MAX(factura_id) AS idFactura FROM Facturas");
                        ResultSet rsFactura = factura.executeQuery();
                        if (rsFactura.next()){
                            ultimaFactura = rsFactura.getInt(1);
                        }

                        int idClient = Integer.parseInt(table1.getValueAt(filaSeleccionada, 0).toString());
                        PreparedStatement ps = connection.prepareStatement("UPDATE Facturas SET cliente_id=? WHERE factura_id=?");
                        ps.setInt(1,idClient);
                        ps.setInt(2, ultimaFactura);
                        ps.executeUpdate();


                        frameCliente.setVisible(false);
                        Pag5_Compra.frameCompra.dispose();
                        Pag5i1_Factura.frameFacturaCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        Pag5i1_Factura.frameFacturaCompra.setContentPane(new Pag5i1_Factura().pag5FacturaPanel);
                        Pag5i1_Factura.frameFacturaCompra.setSize(500,700);
                        Pag5i1_Factura.frameFacturaCompra.setVisible(true);
                        Pag5i1_Factura.frameFacturaCompra.setLocationRelativeTo(null);
                    }catch (SQLException ex){
                        JOptionPane.showMessageDialog(null,ex.getMessage());
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Seleccione un cliente");
                }
            }
        });
        CREARUSUARIOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO Clientes(" +
                            "cliente_id, nombre_cln, apellido_cln, direccion_cln, celular_cln, email_cln) VALUES (?,?,?,?,?,?)");
                    ps.setInt(1,Integer.parseInt(cedulaField.getText()));
                    ps.setString(2,nombreField.getText());
                    ps.setString(3,apellidoField.getText());
                    ps.setString(4,direccionField.getText());
                    ps.setString(5,celularField.getText());
                    ps.setString(6,emailField.getText());
                    ps.executeUpdate();
                    actualizarTabla();
                    cedulaField.setText("");
                    nombreField.setText("");
                    apellidoField.setText("");
                    direccionField.setText("");
                    celularField.setText("");
                    emailField.setText("");
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Clientes");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Cliente ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellido");
            modelo.addColumn("Direccion");
            modelo.addColumn("Celular");
            modelo.addColumn("Email");
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
