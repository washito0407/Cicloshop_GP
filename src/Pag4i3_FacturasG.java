import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag4i3_FacturasG {
    private JButton REGRESARButton;
    private JTable table1;
    private JButton MOSTRARButton;
    private JButton ELIMINARButton;
    public JPanel pag4Facturas;
    static JFrame frameFacturasP = new JFrame("Facturas");
    static ConexionDB conexionDB = new ConexionDB();

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
                if (filaSeleccionada != -1) {
                    int option = JOptionPane.showConfirmDialog(null,"Estas seguro de eliminar la factura?","Confirmar eliminación",JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION){
                        try {
                            Connection connection = conexionDB.ConexionLocal();
                            PreparedStatement ps = connection.prepareStatement("DELETE FROM VENTAS WHERE id_v = ?");
                            ps.setInt(1,Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString()));
                            ps.executeUpdate();
                            actualizarTabla();
                        }catch (SQLException ex){
                            JOptionPane.showMessageDialog(null,"Error: "+ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        MOSTRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada!=-1){
                    int idVenta = Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString());
                    Pag5i1_Factura factura = new Pag5i1_Factura();
                    factura.obtenerFactura(idVenta);

                    Pag5i1_Factura.frameFacturaCompra.setContentPane(factura.pag5FacturaPanel);
                    Pag5i1_Factura.frameFacturaCompra.setSize(500,400);
                    Pag5i1_Factura.frameFacturaCompra.setVisible(true);
                    Pag5i1_Factura.frameFacturaCompra.setLocationRelativeTo(null);
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione una venta","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void actualizarTabla(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            table1.setDefaultEditor(Object.class, null);
            table1.getTableHeader().setReorderingAllowed(false) ;

            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id_v, fecha_v, CLIENTES.nombre, estado_v, CAJEROS.nombre FROM VENTAS\n" +
                    "JOIN CLIENTES ON VENTAS.id_cliente = CLIENTES.id_cliente\n" +
                    "JOIN CAJEROS ON VENTAS.id_cajero = CAJEROS.id_cajero " +
                    "ORDER BY id_v;");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("ID Venta");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cliente");
            modelo.addColumn("Estado");
            modelo.addColumn("Cajero");
            while (rs.next()) {
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ignored) {
        }
    }
}
