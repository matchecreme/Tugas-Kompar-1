import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.*;

public class KomputasiVisualV2 extends JFrame {

    private JProgressBar[] serialBars = new JProgressBar[4];
    private JProgressBar[] parallelBars = new JProgressBar[4];

    private JLabel lblSerialTime = new JLabel("Waktu: 0 ms");
    private JLabel lblParallelTime = new JLabel("Waktu: 0 ms");
    private JLabel lblStatus = new JLabel("Status: Ready");

    private JButton btnStart = new JButton("Start Simulation");

    public KomputasiVisualV2() {

        setTitle("Serial vs Parallel Computing (Enhanced)");
        setSize(950, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel mainPanel = new JPanel(new GridLayout(1,2,20,0));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));

        mainPanel.add(createPanel("SERIAL MODE", serialBars, lblSerialTime, new Color(255,240,240)));
        mainPanel.add(createPanel("PARALLEL MODE", parallelBars, lblParallelTime, new Color(240,255,240)));

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(lblStatus, BorderLayout.WEST);
        bottom.add(btnStart, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        btnStart.addActionListener(e -> startSimulation());
    }

    private JPanel createPanel(String title, JProgressBar[] bars, JLabel time, Color bg) {

        JPanel panel = new JPanel(new GridLayout(7,1,5,5));
        panel.setBackground(bg);

        JLabel lbl = new JLabel(title, JLabel.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(lbl);

        for(int i=0;i<4;i++){
            bars[i] = new JProgressBar(0,100);
            bars[i].setStringPainted(true);
            bars[i].setForeground(new Color(
                    50 + i*40,
                    100 + i*20,
                    200 - i*30
            ));
            panel.add(bars[i]);
        }

        time.setHorizontalAlignment(JLabel.CENTER);
        time.setFont(new Font("Monospaced",Font.BOLD,16));
        panel.add(time);

        return panel;
    }

    private void startSimulation(){

        btnStart.setEnabled(false);
        lblStatus.setText("Status: RUNNING...");
        resetBars();

        // SERIAL EXECUTION
        new Thread(() -> {
            long start = System.currentTimeMillis();

            for(JProgressBar bar : serialBars){
                heavyTask(bar);
            }

            long end = System.currentTimeMillis();
            lblSerialTime.setText("Waktu: "+(end-start)+" ms");

        }).start();

        // PARALLEL EXECUTION
        new Thread(() -> {

            long start = System.currentTimeMillis();

            int cores = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(cores);

            for(JProgressBar bar : parallelBars){
                executor.execute(() -> heavyTask(bar));
            }

            executor.shutdown();

            try{
                executor.awaitTermination(2, TimeUnit.MINUTES);
            }catch(Exception ignored){}

            long end = System.currentTimeMillis();
            lblParallelTime.setText("Waktu: "+(end-start)+" ms");

            lblStatus.setText("Status: DONE ✅");
            btnStart.setEnabled(true);

        }).start();
    }

    private void heavyTask(JProgressBar bar){

        for(int i=0;i<=100;i++){

            final int value = i;

            SwingUtilities.invokeLater(() -> bar.setValue(value));

            // simulasi beban komputasi
            double dummy = 0;
            for(int j=0;j<5000;j++){
                dummy += Math.sqrt(j);
            }

            try{
                Thread.sleep(15);
            }catch(Exception ignored){}
        }
    }

    private void resetBars(){
        for(int i=0;i<4;i++){
            serialBars[i].setValue(0);
            parallelBars[i].setValue(0);
        }

        lblSerialTime.setText("Menghitung...");
        lblParallelTime.setText("Menghitung...");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new KomputasiVisualV2().setVisible(true)
        );
    }
}