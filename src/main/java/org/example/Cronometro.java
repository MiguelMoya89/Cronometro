package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Cronometro extends JFrame implements ActionListener {
    private Timer timer = new Timer(1000, this);
    private int segundos = 0;
    private int tiempoAlarma = -1; // -1 indica que no hay alarma establecida
    private JLabel label = new JLabel("00:00");
    private JTextField campoAlarma = new JTextField(3); // reducir el tamaño del campo de texto
    private JButton botonInicio = new JButton("Inicio");
    private JButton botonPausa = new JButton("Pausa");
    private JButton botonReinicio = new JButton("Reinicio");
    private JButton botonEstablecerAlarma = new JButton("Establecer alarma");
    private JLabel labelAlarma = new JLabel("");
    private JLabel labelFechaHora = new JLabel();

    public Cronometro() {
        super("CronometroMMM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 100);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(botonInicio);
        panel.add(botonPausa);
        panel.add(botonReinicio);
        panel.add(campoAlarma); // mover el campo de texto al final
        panel.add(botonEstablecerAlarma); // agregar el botón después del campo de texto
        panel.add(labelAlarma); // agregamos el label de la alarma al panel
        panel.add(new JLabel("Fecha y Hora:"));
        panel.add(labelFechaHora); // agregar un JLabel para la fecha y hora actual
        add(panel);

        botonInicio.addActionListener(this);
        botonPausa.addActionListener(this);
        botonReinicio.addActionListener(this);
        botonEstablecerAlarma.addActionListener(this);

        setVisible(true);

        // Actualizar la fecha y hora cada segundo
        Timer timerFechaHora = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date ahora = new Date();
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                labelFechaHora.setText(formateador.format(ahora));
            }
        });

        timerFechaHora.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            segundos++;
            actualizarLabel();

            if (segundos == tiempoAlarma) {
                reproducirSonido(); // Reproduce el sonido antes de mostrar el mensaje
                JOptionPane.showMessageDialog(this, "¡La alarma ha sonado!");
                tiempoAlarma = -1; // reseteamos la alarma
                labelAlarma.setText(""); // actualizamos el texto del label de la alarma
            }
            return;
        }

        if (e.getSource() == botonInicio) {
            timer.start();
            return;
        }

        if (e.getSource() == botonPausa) {
            timer.stop();
            return;
        }

        if (e.getSource() == botonReinicio) {
            segundos = 0;
            actualizarLabel();
            return;
        }

        if (e.getSource() == botonEstablecerAlarma) {
            try {
                tiempoAlarma = Integer.parseInt(campoAlarma.getText());
                labelAlarma.setText("Alarma establecida"); // actualizamos el texto del label de la alarma
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un número válido.");
            }

            return;
        }

    }

    private void actualizarLabel() {
        int minutos = segundos / 60;
        int segs = segundos % 60;

        String textoMinutos = minutos < 10 ? "0" + minutos : "" + minutos;
        String textoSegundos = segs < 10 ? "0" + segs : "" + segs;

        label.setText(textoMinutos + ":" + textoSegundos);
    }

    public static void main(String[] args) {
        new Cronometro();
    }

    private void reproducirSonido() {
        try {
            Clip clip = AudioSystem.getClip();
            File audioFile = new File("src/main/java/org/example/alarma1.wav");
            clip.open(AudioSystem.getAudioInputStream(audioFile));
            clip.start();
            clip.loop(1); // Reproduce el sonido dos veces
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

}