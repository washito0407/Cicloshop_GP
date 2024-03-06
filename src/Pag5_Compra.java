import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.*;

public class Pag5_Compra {
    private JTable table1;
    private JButton REGRESARButton;
    private JButton SELECCIONARButton;
    public JPanel pag5CompraPanel;
    private JLabel imagenLabel;
    private JTable table2;
    private JButton ELIMINARButton;
    private JButton REALIZARCOMPRAButton;
    private JButton MODIFICARCANTIDADButton;
    private JButton REFRESCARPRODUCTOSButton;
    static JFrame frameCompra = new JFrame("Compra");
    ConexionDB conexionDB = new ConexionDB();
    private DefaultTableModel modelo = new DefaultTableModel();
    static DefaultTableModel modeloCarrito = new DefaultTableModel();

    public Pag5_Compra() {
        table1.setDefaultEditor(Object.class, null);
        table2.setDefaultEditor(Object.class, null);

        modeloCarrito = new DefaultTableModel();
        modeloCarrito.addColumn("Producto ID");
        modeloCarrito.addColumn("Nombre");
        modeloCarrito.addColumn("Precio");
        modeloCarrito.addColumn("Cantidad");
        modeloCarrito.addColumn("Total");
        table2.setModel(modeloCarrito); // Tabla Carrito

        actualizarTablaSQL(); //Tabla de Productos Disponibles

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

        SELECCIONARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                boolean productoEnCarrito=false;
                if (filaSeleccionada != -1) {
                    int idProducto = Integer.parseInt(table1.getValueAt(filaSeleccionada, 0).toString());
                    for (int i=0; i<modeloCarrito.getRowCount();i++){
                        if (Integer.parseInt(modeloCarrito.getValueAt(i,0).toString())==idProducto){
                            productoEnCarrito=true;
                            break;
                        };
                    }
                    if (!productoEnCarrito){
                        String valor = JOptionPane.showInputDialog(null, "Cantidad: ", "Cuantos productos?", JOptionPane.PLAIN_MESSAGE);
                        if (valor != null) {
                            try {
                                int cantidad = Integer.parseInt(valor);
                                int cantidadProducto = Integer.parseInt(table1.getValueAt(filaSeleccionada, 3).toString());
                                if (cantidad > cantidadProducto) {
                                    JOptionPane.showMessageDialog(null, "Seleccione una cantidad que no supere el stock!", "Cantidad excede la del producto", JOptionPane.ERROR_MESSAGE);
                                } else if (cantidad<1){
                                    JOptionPane.showMessageDialog(null,"Ingrese una cantidad correcta","ERROR",JOptionPane.ERROR_MESSAGE);
                                }else {
                                    double precioP = Double.parseDouble(table1.getValueAt(filaSeleccionada, 2).toString());
                                    double total = precioP * cantidad;

                                    String nombreP = table1.getValueAt(filaSeleccionada, 1).toString();

                                    Object[] filas = new Object[modeloCarrito.getColumnCount()];
                                    filas[0] = idProducto;
                                    filas[1] = nombreP;
                                    filas[2] = precioP;
                                    filas[3] = cantidad;
                                    filas[4] = total;

                                    modeloCarrito.addRow(filas);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Ingrese un cantidad correcta", "ERROR", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"El producto ya está agregado", "Producto en carrito",JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto", "Fila no seleccionada", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        ELIMINARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table2.getSelectedRow();
                if (filaSeleccionada != -1){
                    modeloCarrito.removeRow(filaSeleccionada);
                }else {
                    JOptionPane.showMessageDialog(null,"Selecciona una fila antes de eliminar");
                }

            }
        });
        MODIFICARCANTIDADButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table2.getSelectedRow();
                if (filaSeleccionada!=-1){
                    String valor = JOptionPane.showInputDialog(null, "Cantidad: ", "Modificar cantidad", JOptionPane.PLAIN_MESSAGE);
                    if (valor != null) {
                        try {
                            int cantidad = Integer.parseInt(valor);
                            if (cantidad<1){
                                JOptionPane.showMessageDialog(null,"Ingrese una cantidad correcta","",JOptionPane.ERROR_MESSAGE);
                            }else {
                                int idProducto = Integer.parseInt(modeloCarrito.getValueAt(filaSeleccionada, 0).toString());
                                int stockDisponible=-1;
                                for (int i=0; i<modelo.getRowCount();i++){
                                    if (Integer.parseInt(modelo.getValueAt(i,0).toString())==idProducto){
                                        stockDisponible = Integer.parseInt(modelo.getValueAt(i,3).toString());
                                        break;
                                    };
                                }
                                if (stockDisponible!=-1){
                                    if (cantidad > stockDisponible) {
                                        JOptionPane.showMessageDialog(null, "Seleccione una cantidad que no supere el stock!", "Cantidad excede la del producto", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        modeloCarrito.setValueAt(cantidad, filaSeleccionada, 3);
                                        double totalNuevo = Double.parseDouble(modeloCarrito.getValueAt(filaSeleccionada,2).toString()) * cantidad;
                                        modeloCarrito.setValueAt(totalNuevo, filaSeleccionada, 4);

                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Ingrese un cantidad correcta", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Seleccione un producto", "Fila no seleccionada", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        REALIZARCOMPRAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table2.getRowCount()>0){
                    frameCompra.setVisible(false);
                    Pag5i_Cliente.frameCliente.setContentPane(new Pag5i_Cliente().clientePanel);
                    Pag5i_Cliente.frameCliente.setSize(600,350);
                    Pag5i_Cliente.frameCliente.setVisible(true);
                    Pag5i_Cliente.frameCliente.setLocationRelativeTo(null);
                    Pag5i_Cliente.frameCliente.setVisible(true);
                }else {
                    JOptionPane.showMessageDialog(null,"Ingrese al menos un producto");
                }
            }
        });
        REFRESCARPRODUCTOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTablaSQL();
            }
        });
    }
    public void actualizarTablaSQL() {
        try {
            modelo = new DefaultTableModel();
            table1.setModel(modelo);
            table1.setDefaultEditor(Object.class, null);
            table1.getTableHeader().setReorderingAllowed(false) ;

            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PRODUCTOS WHERE estado_p='DISPONIBLE'");
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            int cantidadColumnas = resultSetMetaData.getColumnCount();

            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Precio");
            modelo.addColumn("Stock");
            while (rs.next()) {
                Object[] filas = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                modelo.addRow(filas);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: "+ex.getMessage(),"ERROR SQL",JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ignored) {
        }
    }
    public void obtenerImagen(int filaSeleccionada) {
        try {
            Connection connection = conexionDB.ConexionLocal();
            String idFilaSeleccionada=table1.getValueAt(filaSeleccionada,0).toString();
            PreparedStatement selectImagen = connection.prepareStatement("SELECT imagen FROM PRODUCTOS WHERE id_p=?");
            selectImagen.setInt(1,Integer.parseInt(idFilaSeleccionada));
            ResultSet result = selectImagen.executeQuery();
            if (result.next()){
                try {
                    Blob imagen = result.getBlob("imagen");
                    InputStream in = imagen.getBinaryStream();
                    BufferedImage image = ImageIO.read(in);
                    Icon icon = new ImageIcon(image.getScaledInstance(imagenLabel.getWidth(), imagenLabel.getHeight(), Image.SCALE_DEFAULT));
                    imagenLabel.setIcon(icon);
                }catch (Exception  ex){
                    imagenLabel.setIcon(null);
                }
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

    }
}
