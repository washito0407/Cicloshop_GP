import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
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
    private JButton subirImagenButton;
    private JButton limpiarButton;
    static JFrame frameIngresoP = new JFrame("Ingreso de Productos");
    ConexionDB conexionDB = new ConexionDB();

    public Pag4i1_Ingreso() {
        table1.setDefaultEditor(Object.class, null);
        actualizarTabla();
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
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO Productos(producto_id, nombre_prd, descripcion, precio_compra, precio_venta, stock, imagen_prd) VALUES (?,?,?,?,?,?,?)");
                    ps.setInt(1,Integer.parseInt(idField.getText()));
                    ps.setString(2, nombreField.getText());
                    ps.setString(3, descripcionTextArea.getText());
                    ps.setDouble(4, Double.parseDouble(precioCompraField.getText()));
                    ps.setDouble(5, Double.parseDouble(precioVentaField.getText()));
                    ps.setInt(6, Integer.parseInt(stockField.getText()));
                    try {
                        byte[] imagenBytes = convertirIconoABytes(imagenLabel.getIcon());
                        ps.setBytes(7,imagenBytes);
                    }catch (NullPointerException npex){
                        ps.setString(7,"DEFAULT");
                    }

                    ps.executeUpdate();
                    actualizarTabla();
                    JOptionPane.showMessageDialog(null,"Se han ingresado los datos correctamente");
                }catch (SQLException | IOException exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }catch (NumberFormatException nfex){
                    JOptionPane.showMessageDialog(null,"Debe completar los campos de precio obligatoriamente","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Productos\n" +
                            "SET producto_id = ?, nombre_prd = ?, descripcion = ?, precio_compra = ?, precio_venta = ?, stock = ?, imagen_prd = ?\n" +
                            "WHERE producto_id = ?");
                    preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
                    preparedStatement.setString(2, nombreField.getText());
                    preparedStatement.setString(3, descripcionTextArea.getText());
                    preparedStatement.setDouble(4, Double.parseDouble(precioCompraField.getText()));
                    preparedStatement.setDouble(5, Double.parseDouble(precioVentaField.getText()));
                    preparedStatement.setInt(6, Integer.parseInt(stockField.getText()));
                    byte[] imagenBytes = convertirIconoABytes(imagenLabel.getIcon());
                    preparedStatement.setBytes(7,imagenBytes);
                    preparedStatement.setInt(8, Integer.parseInt(idField.getText()));
                    preparedStatement.executeUpdate();
                    actualizarTabla();
                    JOptionPane.showMessageDialog(null,"Se han modificado los datos correctamente");
                }catch (SQLException ex){
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        subirImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    ImageIcon imagen;
                    FileNameExtensionFilter filtro = new FileNameExtensionFilter("PNG,JPG","png","jpg");
                    fileChooser.setFileFilter(filtro);
                    int abrir = fileChooser.showOpenDialog(null);
                    if (abrir==JFileChooser.APPROVE_OPTION){
                        File archivo = fileChooser.getSelectedFile();
                        String ruta = archivo.getAbsolutePath();
                        imagen =  new ImageIcon(ruta);
                        imagenLabel.setIcon(imagen);
                        JOptionPane.showMessageDialog(null,"Se ha seleccionado correctamente la foto");
                    }
                }catch (NullPointerException exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage());
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection connection = conexionDB.ConexionLocal();
                try {
                    PreparedStatement ps = connection.prepareStatement("DELETE FROM Productos WHERE producto_id = ?");
                    ps.setInt(1, Integer.parseInt(idField.getText()));
                    ps.executeUpdate();
                    actualizarTabla();
                    JOptionPane.showMessageDialog(null,"Se han eliminado los datos correctamente");
                }catch (SQLException sqlException){
                    JOptionPane.showMessageDialog(null,"No se puede eliminar el producto, elimine las facturas correspondientes","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCampos();
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
                try{
                    byte[] bytes = resultSet.getBytes("imagen_prd");
                    ImageIcon imageIcon = new ImageIcon(bytes);
                    imagenLabel.setIcon(imageIcon);
                }catch (NullPointerException ignored){
                    imagenLabel.setIcon(null);
                }
            }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }
    private static byte[] convertirIconoABytes(Icon icon) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (baos) {
            ImageIcon imageIcon = (ImageIcon) icon;
            Image image = imageIcon.getImage();
            BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            g2.drawImage(image, 0, 0, null);
            g2.dispose();
            ImageIO.write(bufferedImage, "png", baos);
        }
        return baos.toByteArray();
    }
    public void actualizarTabla(){
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
            actualizarCampos();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void actualizarCampos(){
        idField.setText("");
        nombreField.setText("");
        descripcionTextArea.setText("");
        precioCompraField.setText("");
        precioVentaField.setText("");
        stockField.setText("");
        imagenLabel.setIcon(null);
    }
}

