import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This client connects to a server and gives them a few parameters that
 * they have to use to calculate an answer for the client, when the client
 *  gets the response the connection is interrupted
 * 
 * @author Rohrbasser Yoann, Vogel Maximilian
 */
public class CalculatorClient {

	static final Logger LOG = Logger.getLogger(CalculatorClient.class.getName());
    
    final static int BUFFER_SIZE = 1024;

	/**
	 * Send 2 numbers and the operator affecting them to a server at given
     * ip address
	 */
	public void sendCalculationRequest(String ipAdr, double firstNumber, double secondNumber, String operator) {
		Socket clientSocket = null;
		OutputStream os = null;
		InputStream is = null;
		
		try {
            // Using port 80
			clientSocket = new Socket(ipAdr, 80);
			os = clientSocket.getOutputStream();
			is = clientSocket.getInputStream();

			String concatenatedOperationInfo = firstNumber + " " + operator + " " +  secondNumber;
			os.write(concatenatedOperationInfo.getBytes());

			ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int newBytes;
			while ((newBytes = is.read(buffer)) != -1) {
				responseBuffer.write(buffer, 0, newBytes);
			}

			LOG.log(Level.INFO, "Response sent by the server: ");
			LOG.log(Level.INFO, responseBuffer.toString());
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, null, ex);
		} finally {
			try {
				is.close();
			} catch (IOException ex) {
				Logger.getLogger(CalculatorClient.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				os.close();
			} catch (IOException ex) {
				Logger.getLogger(CalculatorClient.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				clientSocket.close();
			} catch (IOException ex) {
				Logger.getLogger(CalculatorClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

		CalculatorClient client = new CalculatorClient();
		client.sendCalculationRequest("127.0.0.1", 5, 6, "+");
	}

}