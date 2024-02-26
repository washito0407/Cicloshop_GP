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
