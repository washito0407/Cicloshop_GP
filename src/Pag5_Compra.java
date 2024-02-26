import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pag5_Compra {
    private JTable table1;
    private JButton REGRESARButton;
    private JButton SELECCIONARButton;
    public JPanel pag5CompraPanel;
    private JLabel imagenLabel;
    private JTable table2;
    private JButton ELIMINARButton;
    private JSpinner cantidadSpinner;
    private JButton FACTURARButton;
    static JFrame frameCompra = new JFrame("Compra");
    ConexionDB conexionDB = new ConexionDB();

    public Pag5_Compra() {
        table1.setDefaultEditor(Object.class, null);
        table2.setDefaultEditor(Object.class, null);
        //Inicializamos el spinner
        SpinnerModel spinnerModel = new SpinnerNumberModel(1,1,20,1);
        cantidadSpinner.setModel(spinnerModel);

        try {
            Connection connection = conexionDB.ConexionLocal();

            DefaultTableModel modelo = new DefaultTableModel();
            table1.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT producto_id, nombre_prd, descripcion, precio_venta, stock FROM Productos");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Producto ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Precio");
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
        actualizarTabla();
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = table1.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        obtenerImagen(filaSeleccionada);
                    }
                }
            }
        });

        REGRESARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCompra.dispose();
                Pag1_Inicio.frame.setVisible(true);
            }
        });
        FACTURARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameCompra.setVisible(false);
                Pag5i1_Factura.frameFacturaCompra.setContentPane(new Pag5i1_Factura().pag5FacturaPanel);
                Pag5i1_Factura.frameFacturaCompra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Pag5i1_Factura.frameFacturaCompra.setSize(800, 800);
                Pag5i1_Factura.frameFacturaCompra.setVisible(true);
                Pag5i1_Factura.frameFacturaCompra.setLocationRelativeTo(null);
            }
        });
        SELECCIONARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cantidadProducto = (Integer) cantidadSpinner.getValue();
                int idFilaSeleccionada= Integer.parseInt(table1.getModel().getValueAt(table1.getSelectedRow(),0).toString());
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO Detalle(cantidad_producto, producto_id) VALUES (?, ?)");
                    ps.setInt(1,cantidadProducto);
                    ps.setInt(2,idFilaSeleccionada);
                    ps.executeUpdate();
                    actualizarTabla();
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }catch (ArrayIndexOutOfBoundsException exception){
                    JOptionPane.showMessageDialog(null,"Seleccione el producto que desee agregar");
                }
            }
        });
        ELIMINARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idFilaSeleccionada= Integer.parseInt(table2.getModel().getValueAt(table2.getSelectedRow(),0).toString());
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement ps =connection.prepareStatement("DELETE FROM Detalle where detalle_id = ?");
                    ps.setInt(1,idFilaSeleccionada);
                    ps.executeUpdate();
                    actualizarTabla();
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(null,ex.getMessage());
                }catch (ArrayIndexOutOfBoundsException exception){
                    JOptionPane.showMessageDialog(null, "Seleccione el producto que desea quitar del carrito");
                }

            }
        });
    }

    public void actualizarTabla(){
        try {
            Connection connection = conexionDB.ConexionLocal();

            DefaultTableModel modelo = new DefaultTableModel();
            table2.setModel(modelo);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM detallesVista");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("Detalle ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Precio");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Total");
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
    public void obtenerImagen(int filaSeleccionada){
        Connection connection = conexionDB.ConexionLocal();
        try{
            int idFilaSeleccionada= Integer.parseInt(table1.getModel().getValueAt(filaSeleccionada,0).toString());
            PreparedStatement ps = connection.prepareStatement("SELECT imagen_prd FROM Productos WHERE producto_id = ?");
            ps.setInt(1, idFilaSeleccionada);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                byte[] bytes = rs.getBytes("imagen_prd");
                ImageIcon imageIcon = new ImageIcon(bytes);
                imagenLabel.setIcon(imageIcon);
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }catch (NullPointerException exception){
            imagenLabel.setIcon(null);
        }
    }
}
