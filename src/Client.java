import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static KeysUser keysUser;
    private static KeysNeighbour keysNeighbour;

    public static void main(String[] args) {
        try {
            clientSocket = new Socket("192.168.56.1", 8888);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            keysUser = new KeysUser();
            keysNeighbour = new KeysNeighbour();

            new Thread(() -> sendMessage(out)).start();
            new Thread(() -> receiveMessage(in, out)).start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(0);
        }
    }

    private static void sendMessage(BufferedWriter bufferedWriter) {
        while (true) {
            try {
                String message = reader.readLine();
                if (!message.isBlank()) {
                    if (message.startsWith("\\chat\\")) {
                        message = encodedMessage(message);
                    }
                    bufferedWriter.write(message + "\n");
                    bufferedWriter.flush();
                }
            } catch (Exception e) {
                System.out.println("Сервер закрыл соединение");
                System.exit(0);
                break;
            }
        }
    }

    private static String encodedMessage(String message) {
        message = message.substring(6);
        System.out.println("Ваше сообщение: " + message);
        StringBuilder encodedString = new StringBuilder("\\chat\\");
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            BigDecimal charCodeDecimal = new BigDecimal((int) c);
            BigDecimal encodedCharDecimal =
                    charCodeDecimal.pow(keysNeighbour.getE()).remainder(BigDecimal.valueOf(keysNeighbour.getN()));
            int encodedChar = encodedCharDecimal.intValue();
            encodedString.append(encodedChar).append(" ");
        }
        message = encodedString.toString().trim();
        return message;
    }

    private static void receiveMessage(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        while (true) {
            String word;
            try {
                word = bufferedReader.readLine();
                if (word.equals("Вы подключены к чату. Происходит обмен ключами.")) {
                    System.out.println(word);
                    String key = "\\key\\" + String.valueOf(keysUser.getE()) + " " + String.valueOf(keysUser.getN());
                    System.out.println("Ваш открытый ключ: (" + keysUser.getE() + ", " + keysUser.getN() + ")");
                    System.out.println("Ваш закрытый ключ: (" + keysUser.getD() + ", " + keysUser.getN() + ")");
                    bufferedWriter.write(key + "\n");
                    bufferedWriter.flush();
                } else if (word.startsWith("\\key\\")) {
                    word = word.substring(5);
                    String[] stringWithKey = word.split(" ");
                    keysNeighbour.setE(Integer.parseInt(stringWithKey[0]));
                    keysNeighbour.setN(Integer.parseInt(stringWithKey[1]));
                    System.out.println("Открытый ключ собеседника: (" + keysNeighbour.getE() + ", " + keysNeighbour.getN() + ")");
                } else if (word.startsWith("\\message\\")) {
                    String startWord = word.substring(9, 32);
                    word = word.substring(32);
                    String[] encodedArray = word.split(" ");
                    StringBuilder decodedString = new StringBuilder();
                    for (String encodedChar : encodedArray) {
                        BigDecimal encodedInt = new BigDecimal(encodedChar);
                        BigDecimal decodedChar =
                                encodedInt.pow((int) keysUser.getD()).remainder(BigDecimal.valueOf(keysUser.getN()));
                        decodedString.append((char) decodedChar.intValue());
                    }
                    System.out.println(startWord + decodedString.toString());
                } else if (word == null) {
                    System.out.println("Сервер закрыл соединение");
                    System.exit(0);
                } else
                    System.out.println(word);
            } catch (Exception e) {
                System.out.println("Сервер закрыл соединение");
                System.exit(0);
                break;
            }
        }
    }
}