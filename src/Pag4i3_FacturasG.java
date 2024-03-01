import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

import static com.itextpdf.text.pdf.PdfFormField.createList;

public class Pag4i3_FacturasG {
    private JButton REGRESARButton;
    private JTable table1;
    private JButton MOSTRARButton;
    private JButton IMPRIMIRButton;
    private JButton ELIMINARButton;
    public JPanel pag4Facturas;
    static JFrame frameFacturasP = new JFrame("Facturas");
    static ConexionDB conexionDB = new ConexionDB();
    static double precioTotal=0;
    static int iFactura = 1;
    static String filePath = String.format("%s%d%s","src/facturas/factura",iFactura,".pdf");
    static File file = new File(filePath);

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
                    int idFilaSeleccionada = Integer.parseInt(table1.getValueAt(filaSeleccionada, 0).toString());
                    Connection connection = conexionDB.ConexionLocal();
                    try {
                        PreparedStatement ps = connection.prepareStatement("DELETE FROM Facturas WHERE factura_id=?");
                        ps.setInt(1, idFilaSeleccionada);
                        ps.executeUpdate();
                        actualizarTabla();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }

                }
            }
        });
        MOSTRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada != -1) {
                    int idFilaSeleccionda = Integer.parseInt(table1.getValueAt(filaSeleccionada, 0).toString());
                    Connection connection = conexionDB.ConexionLocal();
                    try {
                        PreparedStatement ps = connection.prepareStatement("SELECT F.factura_id, F.fecha, C.nombre_cln AS Nombre_Cliente, \n" +
                                "       P.nombre_prd AS Nombre_Producto, D.cantidad_producto, P.precio_venta,\n" +
                                "       (D.cantidad_producto* P.precio_venta) AS Subtotal\n" +
                                "FROM Facturas F\n" +
                                "JOIN Detalle D ON F.factura_id = D.factura_id\n" +
                                "JOIN Productos P ON D.producto_id= P.producto_id\n" +
                                "JOIN Clientes C ON F.cliente_id = C.cliente_id\n" +
                                "WHERE F.factura_id=?");
                        ps.setInt(1, idFilaSeleccionda);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            System.out.println("Nombre del producto: " + rs.getString("Nombre_Producto"));
                            System.out.println("Cantidad: " + rs.getInt("cantidad_producto"));
                            System.out.println("Precio: " + rs.getDouble("precio_venta"));
                            System.out.println("Subtotal: " + rs.getDouble("Subtotal"));
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR SQL", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        IMPRIMIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int filaSeleccionada = table1.getSelectedRow();
                if (filaSeleccionada != -1){
                    int idFactura = Integer.parseInt(table1.getValueAt(filaSeleccionada,0).toString());
                    try {
                        while (!file.createNewFile()){
                            iFactura++;
                            filePath = String.format("%s%d%s", "src/facturas/factura", iFactura, ".pdf");
                            file = new File(filePath);
                        }

                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(filePath));
                        document.open();
                        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);

                        // Logo CicloShop
                        Image imagen = Image.getInstance("src/imagenes/logo(Pequenio).jpg");
                        imagen.setAlignment(Element.ALIGN_CENTER);
                        imagen.setBorderColor(BaseColor.BLACK);
                        document.add(imagen);

                        // Datos CicloShop
                        Paragraph datosCicloShop = new Paragraph();
                        datosCicloShop.add(new Paragraph("Telefono: 0969037943", normalFont));
                        datosCicloShop.add(new Paragraph("Email: cicloShop@mail.com", normalFont));
                        document.add(datosCicloShop);
                        document.add(Chunk.NEWLINE);

                        insertarLinea(document); // Linea Separadora
                        document.add(Chunk.NEWLINE);

                        //Datos Personales
                        datosPersonalesTabla(document, idFactura);
                        document.add(Chunk.NEWLINE);

                        insertarLinea(document);
                        document.add(Chunk.NEWLINE);

                        //Datos de Productos
                        tablaProductos(document, idFactura);
                        document.add(Chunk.NEWLINE);
                        document.add(Chunk.NEWLINE);

                        //Total Factura
                        tablaTotal(document);

                        document.close();
                        precioTotal=0;
                        Desktop.getDesktop().open(new java.io.File(filePath));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Seleccione una factura","Fila no seleccionada",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void datosPersonalesTabla(Document document, int idFactura) throws DocumentException {
        try {
            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement ps = connection.prepareStatement("SELECT Nombre_Cliente, fecha, Celular_Cliente,Direccion_Cliente,Email_Cliente FROM facturaDatos WHERE factura_id=?");
            ps.setInt(1,idFactura);
            ResultSet rs = ps.executeQuery();
            rs.next();
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell[] cells = {
                    new PdfPCell(new Phrase("Factura a:")),
                    new PdfPCell(new Phrase(rs.getString(1))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Factura N°:")),
                    new PdfPCell(new Phrase(String.valueOf(idFactura))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Fecha Factura:")),
                    new PdfPCell(new Phrase(rs.getString(2))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("Celular:")),
                    new PdfPCell(new Phrase(rs.getString(3))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase(rs.getString(4))),
                    new PdfPCell(new Phrase(rs.getString(5))),
                    new PdfPCell(new Phrase("")),
                    new PdfPCell(new Phrase(""))
            };
            //Quitar los bordes de cada celda en la tabla
            for (PdfPCell cell : cells) {
                cell.setBorderWidth(0);
                table.addCell(cell);
            }

            document.add(table);

        }catch (SQLException ex){
                JOptionPane.showMessageDialog(null,ex.getMessage(),"ERROR SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void tablaProductos(Document document, int idFactura) throws DocumentException {
        float [] columnWidth = {245f, 85f, 85f ,85f};
        PdfPTable table = new PdfPTable(columnWidth);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell[] headerCell = {
                new PdfPCell(new Phrase("Producto:")),
                new PdfPCell(new Phrase("Cantidad")),
                new PdfPCell(new Phrase("Precio")),
                new PdfPCell(new Phrase("Subtotal")),
        };
        for (PdfPCell cell : headerCell) {
            cell.setBorderWidth(0);
            table.addCell(cell);
        }
        try {

            Connection connection = conexionDB.ConexionLocal();
            PreparedStatement ps = connection.prepareStatement("SELECT Nombre_Producto, cantidad_producto, precio_venta, Subtotal FROM facturaDatos WHERE factura_id=?");
            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                precioTotal = precioTotal + rs.getDouble(4);
                PdfPCell[] cells = {
                        new PdfPCell(new Phrase(rs.getString(1))),
                        new PdfPCell(new Phrase(String.valueOf(rs.getInt(2)))),
                        new PdfPCell(new Phrase(String.format("%.2f",rs.getDouble(3)))),
                        new PdfPCell(new Phrase(String.format("%.2f",rs.getDouble(4)))),
                };
                for (PdfPCell cell : cells) {
                    cell.setBorderWidth(0);
                    table.addCell(cell);
                }
            }
            document.add(table);
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(),"ERROR SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void tablaTotal(Document document) throws DocumentException {
        double iva = precioTotal*0.15;
        double totalIva=precioTotal+iva;
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell[] cells = {
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("Subtotal sin IVA")),
                new PdfPCell(new Phrase(String.valueOf(precioTotal))),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("IVA 15%")),
                new PdfPCell(new Phrase(String.format("%.2f",iva))),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("")),
                new PdfPCell(new Phrase("TOTAL")),
                new PdfPCell(new Phrase(String.format("%.2f",totalIva)))
        };

        for (PdfPCell cell : cells) {
            cell.setBorderWidth(0);
            table.addCell(cell);
        }
        document.add(table);
    }
    public void insertarLinea(Document document) throws DocumentException {
        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(2f);
        document.add(dottedline);
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
