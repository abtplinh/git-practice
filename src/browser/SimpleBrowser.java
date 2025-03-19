package browser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleBrowser extends JFrame {
    private JTextField urlField;
    private JTextArea responseArea;
    private JComboBox<String> methodComboBox;
    
    public SimpleBrowser() {
        setTitle("Simple Browser");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel nhập URL và chọn phương thức request
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        urlField = new JTextField("http://");
        topPanel.add(urlField, BorderLayout.CENTER);

        methodComboBox = new JComboBox<>(new String[]{"GET", "POST", "HEAD"});
        topPanel.add(methodComboBox, BorderLayout.EAST);

        JButton sendButton = new JButton("Send");
        topPanel.add(sendButton, BorderLayout.SOUTH);

        // Khu vực hiển thị phản hồi
        responseArea = new JTextArea();
        responseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Xử lý sự kiện khi nhấn nút "Send"
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                String method = (String) methodComboBox.getSelectedItem();
                if (url != null && method != null) {
                    sendRequest(url, method);
                }
            }
        });
    }

    // Hàm thực hiện gửi request
    private void sendRequest(String urlString, String method) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            
            if (method.equals("POST")) {
                connection.setDoOutput(true);  // Enable output for POST
                OutputStream os = connection.getOutputStream();
                os.write("sample data".getBytes()); // Sample POST data
                os.flush();
                os.close();
            }

            int responseCode = connection.getResponseCode();
            responseArea.setText("Response Code: " + responseCode + "\n");

            if (method.equals("GET") || method.equals("POST")) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                responseArea.append("Response:\n" + content.toString());
                in.close();
            } else if (method.equals("HEAD")) {
                // Display headers for HEAD request
                for (String headerKey : connection.getHeaderFields().keySet()) {
                    responseArea.append(headerKey + ": " + connection.getHeaderField(headerKey) + "\n");
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            responseArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleBrowser browser = new SimpleBrowser();
            browser.setVisible(true);

            System.out.println("Hello");
            System.out.println("Hello1");
            System.out.println("Hello2");
            System.out.println("Hello3");

        });
    }
}
