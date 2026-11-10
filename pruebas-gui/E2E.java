import java.awt.*; import java.awt.event.MouseEvent; import java.awt.image.BufferedImage; import java.io.File;
import java.time.LocalDate; import java.time.format.DateTimeFormatter; import java.util.*; import java.util.List;
import javax.imageio.ImageIO; import javax.swing.*; import javax.swing.table.TableModel;

public class E2E {
  static int paso = 0, ok = 0, mal = 0; static File dir = new File("capturas");
  static String F(int d){ return LocalDate.now().plusDays(d).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }

  public static void main(String[] a) throws Exception {
    dir.mkdirs();
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    vista.Main.main(new String[0]); vista.MainKiosco.main(new String[0]);
    Window op = esperar("Agencia de alquiler"), ki = esperar("Kiosco");
    edt(() -> { op.setBounds(0,0,1100,560); ki.setBounds(0,0,1280,860); });
    dormir(1500);
    foto(ki, "kiosco inicial"); foto(op, "operario inicial");
    check("Kiosco muestra los 4 disponibles", tarjetas(ki).equals(List.of("Fiat Cronos","Toyota Corolla","Honda CB190","Toyota Hilux")), tarjetas(ki));

    // 1. Operario registra la Ranger -> aparece en el kiosco (#20)
    click(op, "Agregar camioneta"); Window f = esperar("Agregar camioneta");
    llenar(f, "AE321JK","Ford","Ranger","Azul","2024","5000","50000","900"); foto(f, "operario carga Ranger"); click(f, "Guardar"); dormir(800);
    check("Operario ve la Ranger a $70000", celda(op, 0, "AE321JK", 7).startsWith("70000"), celda(op,0,"AE321JK",7));
    dormir(4000); foto(ki, "kiosco con Ranger");
    check("#20 Kiosco muestra la Ranger sin reiniciar", tarjetas(ki).contains("Ford Ranger"), tarjetas(ki));

    // 2. Patente repetida
    click(op, "Agregar auto"); f = esperar("Agregar auto");
    llenar(f, "AB123CD","VW","Gol","Rojo","2020","1","1000","4","5"); click(f, "Guardar"); Window e = esperarMsj();
    check("Patente repetida rechazada", texto(e).contains("ya está registrada"), texto(e)); foto(e, "patente repetida"); cerrarMsj(e); cerrar(f);

    // 3. Kiosco: Ana + Corolla rechazada, luego Lucia alquila
    tocar(ki, "Toyota Corolla"); f = esperar("Alquilar vehículo");
    llenar(f, "40222333", F(2)); dormir(400); foto(f, "kiosco formulario Ana");
    check("Kiosco muestra total estimado", etiquetas(f).contains("$92.000,00"), etiquetas(f));
    click(f, "Confirmar"); e = esperarMsj(); check("Kiosco rechaza a Ana (sin categoría B)", texto(e).contains("categoría B"), texto(e)); foto(e, "kiosco rechazo Ana"); cerrarMsj(e);
    llenar(f, "99999999", F(2)); click(f, "Confirmar"); e = esperarMsj(); check("Kiosco rechaza DNI inexistente", texto(e).contains("No hay ningún cliente"), texto(e)); cerrarMsj(e);
    llenar(f, "38111222", F(2)); click(f, "Confirmar"); e = esperarMsj();
    String exito = texto(e); check("Kiosco confirma alquiler de Lucía", exito.contains("contrato es el N°"), exito); foto(e, "kiosco exito Lucia"); cerrarMsj(e);
    int nroKiosco = Integer.parseInt(exito.replaceAll("(?s).*N° (\\d+).*", "$1"));
    dormir(500); check("Corolla desaparece del kiosco", !tarjetas(ki).contains("Toyota Corolla"), tarjetas(ki)); foto(ki, "kiosco sin Corolla");

    // 4. Operario ve el contrato del kiosco sin reiniciar (#19)
    dormir(4000); tab(op, 2); dormir(300); foto(op, "operario contratos con el del kiosco");
    check("#19 Operario ve el contrato "+nroKiosco+" del kiosco", !celda(op, 0, String.valueOf(nroKiosco), 6).isEmpty(), celda(op,0,String.valueOf(nroKiosco),6));
    tab(op, 0); dormir(300); check("#19 Operario ve el Corolla ALQUILADO", celda(op,0,"AC456EF",6).equals("ALQUILADO"), celda(op,0,"AC456EF",6));

    // 5. Operario alquila la Hilux -> desaparece del kiosco (#20)
    tab(op, 2); click(op, "Nuevo contrato"); f = esperar("Nuevo contrato");
    combo(f, 0, "33444555"); combo(f, 1, "AD789GH"); llenar(f, F(3)); dormir(300); foto(f, "operario contrato Hilux"); click(f, "Guardar"); dormir(800);
    dormir(4000); foto(ki, "kiosco sin Hilux");
    check("#20 Hilux desaparece del kiosco", !tarjetas(ki).contains("Toyota Hilux"), tarjetas(ki));

    // 6. Operario registra la devolucion del contrato del kiosco -> Corolla vuelve (#20)
    seleccionar(op, String.valueOf(nroKiosco)); click(op, "Registrar devolución"); f = esperar("Registrar devolución");
    llenar(f, F(0), "8200"); click(f, "Guardar"); e = esperarMsj(); check("Devolución calcula el total", texto(e).contains("Total a cobrar"), texto(e)); foto(e, "operario devolucion"); cerrarMsj(e);
    dormir(4000); check("#20 Corolla vuelve al kiosco tras la devolución", tarjetas(ki).contains("Toyota Corolla"), tarjetas(ki));

    // 7. Operario registra cliente nuevo y alquila en el kiosco (#19)
    tab(op, 1); click(op, "Agregar cliente"); f = esperar("Agregar cliente");
    llenar(f, "Carla","Nuñez","45123456","10/10/1995","3875000000","L-2001","10/10/2030"); casilla(f, "B"); foto(f, "operario nuevo cliente"); click(f, "Guardar"); dormir(800);
    Window av = ventana("Aviso"); if (av != null) cerrarMsj(av);
    tocar(ki, "Ford Ranger"); f = esperar("Alquilar vehículo"); llenar(f, "45123456", F(1)); click(f, "Confirmar"); e = esperarMsj();
    check("#19 Kiosco reconoce al cliente recién registrado", texto(e).contains("contrato es el N°"), texto(e)); cerrarMsj(e);

    // 8. Cerrar y reabrir el operario
    dormir(1000); edt(() -> op.dispose()); vista.Main.main(new String[0]); Window op2 = esperar("Agencia de alquiler"); edt(() -> op2.setBounds(0,0,1100,560)); dormir(1000);
    tab(op2, 2); dormir(300); foto(op2, "operario reabierto contratos");
    check("Al reabrir siguen todos los contratos", filas(op2) == 4, filas(op2)+" contratos");
    tab(op2, 0); dormir(200); check("Al reabrir sigue la Ranger ALQUILADA por el kiosco", celda(op2,0,"AE321JK",6).equals("ALQUILADO"), celda(op2,0,"AE321JK",6));
    foto(ki, "kiosco final");
    System.out.println("\nRESULTADO: " + ok + " OK, " + mal + " FALLAN");
    System.exit(0);
  }

  // ---------- utilidades ----------
  static void check(String n, boolean c, Object det){ if (c) ok++; else mal++; System.out.println((c?"OK   ":"FALLA ")+n+(c?"":"  -> "+det)); }
  static void dormir(long ms){ try { Thread.sleep(ms); } catch (Exception x) {} }
  static void edt(Runnable r){ if (SwingUtilities.isEventDispatchThread()) { r.run(); return; } try { SwingUtilities.invokeAndWait(r); } catch (Exception x) { throw new RuntimeException(x); } }
  static <T> T edtGet(java.util.concurrent.Callable<T> c){ Object[] o = new Object[1]; edt(() -> { try { o[0] = c.call(); } catch (Exception x) { throw new RuntimeException(x); } }); return (T) o[0]; }
  static String titulo(Window w){ return w instanceof Frame ? ((Frame) w).getTitle() : w instanceof Dialog ? ((Dialog) w).getTitle() : ""; }
  static Window ventana(String t){ return edtGet(() -> { Window r = null; for (Window w : Window.getWindows()) if (w.isShowing() && titulo(w).startsWith(t)) r = w; return r; }); }
  static Window esperar(String t){ for (int i = 0; i < 100; i++) { Window w = ventana(t); if (w != null) { dormir(300); return w; } dormir(100); } throw new RuntimeException("No apareció la ventana " + t); }
  static Window esperarMsj(){ for (int i = 0; i < 100; i++) { Window w = edtGet(() -> { Window r = null; for (Window x : Window.getWindows()) if (x.isShowing() && x instanceof JDialog && !todos(x, JOptionPane.class).isEmpty()) r = x; return r; }); if (w != null) { dormir(300); return w; } dormir(100); } throw new RuntimeException("No apareció ningún mensaje"); }
  static <T> List<T> todos(Component c, Class<T> k){ List<T> l = new ArrayList<>(); if (k.isInstance(c)) l.add(k.cast(c)); if (c instanceof Container) for (Component h : ((Container) c).getComponents()) l.addAll(todos(h, k)); return l; }
  static void click(Window w, String txt){ JButton b = edtGet(() -> { for (JButton x : todos(w, JButton.class)) if (txt.equals(x.getText()) && x.isShowing()) return x; return null; }); if (b == null) throw new RuntimeException("No hay botón " + txt); SwingUtilities.invokeLater(b::doClick); dormir(500); }
  static void llenar(Window w, String... v){ edt(() -> { List<JTextField> c = todos(w, JTextField.class); c.removeIf(x -> !x.isEditable() || x.getParent() instanceof JComboBox); for (int i = 0; i < v.length; i++) c.get(i).setText(v[i]); }); }
  static void casilla(Window w, String t){ edt(() -> { for (JCheckBox c : todos(w, JCheckBox.class)) if (t.equals(c.getText())) c.setSelected(true); }); }
  static void combo(Window w, int idx, String contiene){ edt(() -> { JComboBox<?> cb = todos(w, JComboBox.class).get(idx); for (int i = 0; i < cb.getItemCount(); i++) if (cb.getItemAt(i).toString().contains(contiene)) { cb.setSelectedIndex(i); return; } throw new RuntimeException("Combo sin " + contiene); }); }
  static void cerrarMsj(Window w){ JButton b = edtGet(() -> todos(w, JButton.class).get(0)); SwingUtilities.invokeLater(b::doClick); dormir(500); }
  static void cerrar(Window w){ edt(w::dispose); dormir(300); }
  static String texto(Window w){ return edtGet(() -> { StringBuilder s = new StringBuilder(); for (JTextArea t : todos(w, JTextArea.class)) s.append(t.getText()); for (JLabel l : todos(w, JLabel.class)) if (l.getText() != null) s.append(l.getText()); for (JOptionPane p : todos(w, JOptionPane.class)) if (p.getMessage() instanceof String) s.append(p.getMessage()); return s.toString(); }); }
  static List<String> etiquetas(Window w){ return edtGet(() -> { List<String> l = new ArrayList<>(); for (JLabel x : todos(w, JLabel.class)) l.add(x.getText()); return l; }); }
  static List<String> tarjetas(Window k){ return edtGet(() -> { List<String> l = new ArrayList<>(); for (JLabel x : todos(k, JLabel.class)) if ("marcaModelo".equals(x.getName()) && x.isShowing()) l.add(x.getText()); return l; }); }
  static void tocar(Window k, String mm){ Component t = edtGet(() -> { for (JLabel x : todos(k, JLabel.class)) if ("marcaModelo".equals(x.getName()) && mm.equals(x.getText()) && x.isShowing()) return x.getParent(); return null; }); if (t == null) throw new RuntimeException("No hay tarjeta " + mm);
    SwingUtilities.invokeLater(() -> t.dispatchEvent(new MouseEvent(t, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 10, 10, 1, false, MouseEvent.BUTTON1))); dormir(500); }
  static void tab(Window w, int i){ edt(() -> todos(w, JTabbedPane.class).get(0).setSelectedIndex(i)); }
  static JTable tabla(Window w){ return edtGet(() -> { for (JTable t : todos(w, JTable.class)) if (t.isShowing()) return t; return null; }); }
  static int filas(Window w){ return edtGet(() -> tabla(w).getRowCount()); }
  static String celda(Window w, int colClave, String clave, int col){ return edtGet(() -> { TableModel m = tabla(w).getModel(); for (int r = 0; r < m.getRowCount(); r++) if (String.valueOf(m.getValueAt(r, colClave)).equals(clave)) return String.valueOf(m.getValueAt(r, col)); return ""; }); }
  static void seleccionar(Window w, String clave){ edt(() -> { JTable t = tabla(w); for (int r = 0; r < t.getRowCount(); r++) if (String.valueOf(t.getValueAt(r, 0)).equals(clave)) t.setRowSelectionInterval(r, r); }); }
  static void foto(Window w, String nombre){ dormir(300); edt(() -> { JRootPane rp = ((RootPaneContainer) w).getRootPane(); BufferedImage img = new BufferedImage(rp.getWidth(), rp.getHeight(), BufferedImage.TYPE_INT_RGB); Graphics2D g = img.createGraphics(); rp.paint(g); g.dispose();
    try { ImageIO.write(img, "png", new File(dir, String.format("%02d %s.png", ++paso, nombre))); } catch (Exception x) { throw new RuntimeException(x); } }); }
}
