import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KomputasiVisual extends JFrame {
    private JProgressBar[] serialBars = new JProgressBar[4];
    private JProgressBar[] parallelBars = new JProgressBar[4];
    private JLabel lblTimeSerial = new JLabel("Waktu: 0 ms");
    private JLabel lblTimeParallel = new JLabel("Waktu: 0 ms");
    private JButton btnStart = new JButton("Mulai Perbandingan");

    public KomputasiVisual() {
        setTitle("Analisis Komputasi: Serial vs Parallel");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL UTAMA (Visualisasi) ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Setup Section Serial
        mainPanel.add(createSection("SERIAL (Single Thread)", serialBars, lblTimeSerial, new Color(255, 230, 230)));
        
        // Setup Section Parallel
        mainPanel.add(createSection("PARALLEL (Multi-Thread)", parallelBars, lblTimeParallel, new Color(230, 255, 230)));

        // --- PANEL BAWAH (Kontrol & Edukasi) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Info Keunggulan
        String infoText = "<html><body style='padding:10px; font-family: sans-serif;'>"
                + "<b>Keunggulan Serial:</b> Penggunaan memori rendah, mudah di-debug, tidak ada <i>race condition</i>.<br>"
                + "<b>Keunggulan Parallel:</b> Jauh lebih cepat untuk tugas berat, efisiensi CPU maksimal, <i>user experience</i> lebih lancar."
                + "</body></html>";
        JLabel lblInfo = new JLabel(infoText);
        
        bottomPanel.add(lblInfo, BorderLayout.CENTER);
        bottomPanel.add(btnStart, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnStart.addActionListener(e -> jalankanSimulasi());
    }

    private JPanel createSection(String title, JProgressBar[] bars, JLabel timeLabel, Color bg) {
        JPanel p = new JPanel(new GridLayout(7, 1, 5, 5));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        p.add(lblTitle);

        for (int i = 0; i < 4; i++) {
            bars[i] = new JProgressBar(0, 100);
            bars[i].setStringPainted(true);
            p.add(bars[i]);
        }
        
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        p.add(timeLabel);
        
        return p;
    }

    private void jalankanSimulasi() {
        btnStart.setEnabled(false);
        resetApp();

        // 1. EKSEKUSI SERIAL
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for (JProgressBar bar : serialBars) {
                prosesTugas(bar);
            }
            long endTime = System.currentTimeMillis();
            lblTimeSerial.setText("Waktu: " + (endTime - startTime) + " ms");
            btnStart.setEnabled(true);
        }).start();

        // 2. EKSEKUSI PARALLEL
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (JProgressBar bar : parallelBars) {
                executor.submit(() -> prosesTugas(bar));
            }
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {}
            long endTime = System.currentTimeMillis();
            lblTimeParallel.setText("Waktu: " + (endTime - startTime) + " ms");
        }).start();
    }

    private void prosesTugas(JProgressBar bar) {
        for (int i = 0; i <= 100; i++) {
            final int p = i;
            SwingUtilities.invokeLater(() -> bar.setValue(p));
            try { Thread.sleep(20); } catch (InterruptedException e) {}
        }
    }

    private void resetApp() {
        for (int i = 0; i < 4; i++) {
            serialBars[i].setValue(0);
            parallelBars[i].setValue(0);
        }
        lblTimeSerial.setText("Menghitung...");
        lblTimeParallel.setText("Menghitung...");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KomputasiVisual().setVisible(true));
    }
}
