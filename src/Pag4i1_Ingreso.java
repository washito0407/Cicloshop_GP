import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag4i1_Ingreso {
    public JPanel Ingreso;
    private JButton regresarButton;
    private JTable table1;
    private JButton modificarButton;
    private JButton ingresarButtonU;
    private JButton eliminarButton;
    private JTextField idField;
    private JTextArea descripcionTextArea;
    private JLabel imagenLabel;
    private JTextField nombreField;
    private JTextField precioCompraField;
    private JTextField precioVentaField;
    private JTextField stockField;
    static JFrame frameIngresoP = new JFrame("Ingreso de Productos");
    ConexionDB conexionDB = new ConexionDB();

    public Pag4i1_Ingreso() {
        try {
            Connection connection = conexionDB.ConexionLocal();

            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Productos");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Producto ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Precio Compra");
            modelo.addColumn("Precio Venta");
            modelo.addColumn("Stock");
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

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = table1.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        obtenerDatos(filaSeleccionada);
                    }
                }
            }
        });

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameIngresoP.setVisible(false);
                Pag4_Admin.frameAdminP.setVisible(true);
            }
        });
        ingresarButtonU.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = conexionDB.ConexionLocal();
                    PreparedStatement ps = connection.prepareStatement("SHOW CREATE TABLE Productos");
                    //PreparedStatement ps = connection.prepareStatement("INSERT INTO Productos VALUES (1,'producto1', 'descripcion1', 10.0, 180.5, 35)");
                    //, (2,'producto2','descripcion2',700.25,600.5,25), (3,'producto3','descripcion3',80.0,50.5,10)"
                    //ps.executeUpdate();
                }catch (SQLException exception){
                    System.out.println(exception.getMessage());
                }
            }
        });
    }

    private void createUIComponents() {
        table1 = new JTable();
    }
    private void obtenerDatos(int filaSeleccionada){
        try {
            Connection connection = conexionDB.ConexionLocal();
            int idFilaSeleccionada= Integer.parseInt(table1.getModel().getValueAt(filaSeleccionada,0).toString());
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Productos WHERE producto_id = ?");
            ps.setInt(1, idFilaSeleccionada);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()){
                idField.setText(String.valueOf(resultSet.getInt("producto_id")));
                nombreField.setText(resultSet.getString("nombre_prd"));
                descripcionTextArea.setText(resultSet.getString("descripcion"));
                precioCompraField.setText(resultSet.getString("precio_compra"));
                precioVentaField.setText(resultSet.getString("precio_venta"));
                stockField.setText(String.valueOf(resultSet.getInt("stock")));
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}

