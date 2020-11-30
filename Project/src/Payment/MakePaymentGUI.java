package Payment;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextField;

public class MakePaymentGUI extends JPanel {
	
	private JPanel frame;
	private JTextField securityCodeField;
	private JTextField cardNumberField;
	private JButton returnHomeButton;
	private JButton submitPaymentButton;
	private JLabel loginLabel;
	private MakePayment makePayment;
	private BufferedReader reader;



	public MakePaymentGUI(MakePayment makePayment){
		setMakePayment(makePayment);
		
		frame = new JPanel();
		frame.setBounds(0, 0, 1000, 650);
		frame.setBackground(new Color(176, 196, 222));
		frame.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Payment needed for your transaction:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(319, 11, 346, 72);
		frame.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("$20.00");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(453, 82, 75, 44);
		frame.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Please enter your credit card number:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(354, 155, 276, 46);
		frame.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Please enter your security code:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(374, 241, 235, 44);
		frame.add(lblNewLabel_3);
		
		submitPaymentButton = new JButton("Submit Payment");
		submitPaymentButton.setBounds(409, 410, 166, 46);
		frame.add(submitPaymentButton);
		
		securityCodeField = new JTextField();
		securityCodeField.setBounds(387, 275, 209, 30);
		frame.add(securityCodeField);
		securityCodeField.setColumns(10);
		
		cardNumberField = new JTextField();
		cardNumberField.setColumns(10);
		cardNumberField.setBounds(364, 193, 245, 30);
		frame.add(cardNumberField);
		
		returnHomeButton = new JButton("Return to Home");
		returnHomeButton.setBounds(10, 11, 150, 43);
		frame.add(returnHomeButton);
		
		loginLabel = new JLabel("Login Status: Not Logged In");
		loginLabel.setBounds(804, 17, 159, 30);
		frame.add(loginLabel);
		
		add(frame);
	}
	

	public JTextField getSecurityCodeField() {
		return securityCodeField;
	}

	public JTextField getCardNumberField() {
		return cardNumberField;
	}

	public JButton getSubmitPaymentButton() {
		return submitPaymentButton;
	}

	
	public JButton getReturnHomeButton() {
		return returnHomeButton;
	}
	
	public JLabel getLoginLabel() {
		return loginLabel;
	}
	
	
	public boolean makePayment(double amount) {
//        int option = 0;
//        System.out.println("Select 1 to pay with credit card or 2 te pay with a voucher");
//        try {
////            option = Integer.parseInt(reader.readLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (option == 1) {
//            return payWithCreditCard(amount);
//        }
//        else if (option == 2) {
//            return payWithVoucher(amount);
//        }
//        else
//            System.out.println("ERROR");
    return false;
    }

    private boolean payWithCreditCard(double amount) {
		return true;
//        String cc = "";
//        int cvv = 0;
//        try {
//            System.out.println("Enter your credit card number");
//            cc = reader.readLine();
//            System.out.println("Enter the CVV number of your card");
//            cvv = Integer.parseInt(reader.readLine());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return makePayment.payWithCreditCard(cc, cvv, amount);
    }

    private boolean payWithVoucher(double amount) {
        int vouchNum = 0;
        try {
            System.out.println("Enter your voucher number");
            vouchNum = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        double remainder = makePayment.payWithVoucher(vouchNum, amount);
        if (remainder == 0)
            return true;
        else if (remainder == -1)
            return false;
        else
            return payWithCreditCard(remainder);
    }

    public void setMakePayment(MakePayment makePayment) {
        this.makePayment = makePayment;
    }
}