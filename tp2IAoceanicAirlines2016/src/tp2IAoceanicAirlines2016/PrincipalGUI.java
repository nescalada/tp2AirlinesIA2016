package tp2IAoceanicAirlines2016;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.cs3.prolog.connector.Connector;
import org.cs3.prolog.connector.common.QueryUtils;
import org.cs3.prolog.connector.process.PrologException;
import org.cs3.prolog.connector.process.PrologProcess;
import org.cs3.prolog.connector.process.PrologProcessException;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.util.List;
import java.util.Map;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class PrincipalGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrincipalGUI window = new PrincipalGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PrincipalGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PrincipalGUI.class.getResource("/tp2IAoceanicAirlines2016/Recursos/Airplane.png")));
		frame.setBounds(100, 100, 312, 282);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblOrigen = new JLabel("Origen:");
		lblOrigen.setBounds(15, 27, 69, 20);
		frame.getContentPane().add(lblOrigen);
		
		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setBounds(15, 77, 69, 20);
		frame.getContentPane().add(lblDestino);
		
		JComboBox cmboxOrigen = new JComboBox();
		cmboxOrigen.setBounds(110, 24, 167, 26);
		frame.getContentPane().add(cmboxOrigen);
		
		JComboBox cmboxDestino = new JComboBox();
		cmboxDestino.setBounds(110, 74, 167, 26);
		frame.getContentPane().add(cmboxDestino);
		
		JCheckBox chckbxPrefieroPocosTransbordos = new JCheckBox("Prefiero pocos transbordos");
		chckbxPrefieroPocosTransbordos.setBounds(15, 119, 262, 29);
		frame.getContentPane().add(chckbxPrefieroPocosTransbordos);
		
		JButton btnsolicitarRecomendacin = new JButton("  \u00A1Solicitar recomendaci\u00F3n!");
		btnsolicitarRecomendacin.setIcon(new ImageIcon(PrincipalGUI.class.getResource("/tp2IAoceanicAirlines2016/Recursos/Airplane.png")));
		btnsolicitarRecomendacin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				solicitarRecomendacion();
			}
		});
		btnsolicitarRecomendacin.setBounds(15, 164, 262, 49);
		frame.getContentPane().add(btnsolicitarRecomendacin);
	}
	
	private void solicitarRecomendacion(){
		//accion del botón solicitar recomendación
		//Llama a un proceso de prolog ... se crea si todavía no existe
        try {
            PrologProcess process = Connector.newPrologProcess();

            //Establece la base de echos
            cargarBaseDeEchosYReglas(process);

            // Crea una consulta con el método buildTerm
            // esto es lo mismo que "father_of(Father, peter)"
            String query = QueryUtils.bT("father_of", "Father", "peter");
            // Toma el primer resultado de la consulta e ignora si hay otros
            Map<String, Object> result = process.queryOnce(query);
            if (result == null) {
                //Si el resultado es nulo falla la consulta
                System.out.println("peter has no father");
            } else {
                // Si la consula tiene resultados se mapea y se muestra el dato embebido
                System.out.println(result.get("Father") + " is the father of peter");
            }

            // Otra consulta: father_of(john, Child)
            query = QueryUtils.bT("father_of", "john", "Child");
            // Devuelve todos los resultados en una lista
            // Cada elemento en la lista es un resultado
            // Si la consulta falla la lista estará vacía (pero no sera nula)
            List<Map<String, Object>> results = process.queryAll(query);
            //System.out.println(results.isEmpty());
            //con lo anterior se puede saber si la consulta devuelve vacio
            for (Map<String, Object> r : results) {
                //Itera sobre cada resultado
                System.out.println(r.get("Child") + " is a child of john");
            }
        } catch (PrologException a){
        	// TODO Auto-generated catch block
        				JOptionPane.showMessageDialog(null, "Verifique:\nLa existencia de su archivo .pl en el classpath de la aplicación.\nLa existencia de echos y reglas en su archivo .pl.\nQue swiprolog esté actualizado.", "ERROR AL INTENTAR LEER ARCHIVO PROLOG", JOptionPane.ERROR_MESSAGE);
        }catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private static void cargarBaseDeEchosYReglas(PrologProcess process) throws PrologProcessException {		
		// se pueden escribir directamente
        //process.queryOnce("assertz(father_of(paul, peter))");
        //process.queryOnce("assertz(father_of(john, paul))");
        //process.queryOnce("assertz(father_of(john, ringo))");
        //process.queryOnce("assertz(father_of(john, george))");

		//o consultando a un archivo
        String consultQuery = QueryUtils.bT("reconsult", "'src/tp2IAoceanicAirlines2016/Recursos/vuelosbe_origen-destino-tiempomin.pl'");
        process.queryOnce(consultQuery);
    }
}
