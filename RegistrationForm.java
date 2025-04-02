import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationForm extends Frame implements ActionListener {
    TextField nameField, mobileField;
    TextArea addressArea, outputArea;
    Checkbox maleCheckbox, femaleCheckbox, acceptCheckbox;
    Button submitButton, resetButton;
    Choice dayChoice, monthChoice, yearChoice, prefixChoice;
    Dialog successDialog, errorDialog;

    RegistrationForm() {
        setTitle("Registration Form");
        setSize(800, 500);
        setLayout(null);
        successDialog = new Dialog(this, "Submission Successful", true);
        successDialog.setSize(300, 150);
        successDialog.setLayout(new FlowLayout());
        Label successLabel = new Label("Form Submitted Successfully!");
        successDialog.add(successLabel);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> successDialog.setVisible(false));
        successDialog.add(okButton);

        errorDialog = new Dialog(this, "Error", true);
        errorDialog.setSize(300, 150);
        errorDialog.setLayout(new FlowLayout());
        Label errorLabel = new Label();
        errorDialog.add(errorLabel);
        Button errorOkButton = new Button("OK");
        errorOkButton.addActionListener(e -> errorDialog.setVisible(false));
        errorDialog.add(errorOkButton);

        Label formTitle = new Label("Registration Form", Label.CENTER);
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setBounds(100, 30, 200, 30);
        add(formTitle);

        Label nameLabel = new Label("Name:");
        nameLabel.setBounds(50, 80, 100, 30);
        add(nameLabel);

        nameField = new TextField();
        nameField.setBounds(150, 80, 200, 30);
        add(nameField);

        Label mobileLabel = new Label("Mobile:");
        mobileLabel.setBounds(50, 130, 100, 30);
        add(mobileLabel);

        prefixChoice = new Choice();
        prefixChoice.add("+2519");
        prefixChoice.add("+2517");
        prefixChoice.setBounds(150, 130, 50, 30);
        add(prefixChoice);

        mobileField = new TextField();
        mobileField.setBounds(210, 130, 140, 30);
        add(mobileField);

        Label genderLabel = new Label("Gender:");
        genderLabel.setBounds(50, 180, 100, 30);
        add(genderLabel);

        maleCheckbox = new Checkbox("Male");
        maleCheckbox.setBounds(150, 180, 60, 30);
        add(maleCheckbox);

        femaleCheckbox = new Checkbox("Female");
        femaleCheckbox.setBounds(220, 180, 80, 30);
        add(femaleCheckbox);

        Label dobLabel = new Label("DOB:");
        dobLabel.setBounds(50, 230, 100, 30);
        add(dobLabel);

        dayChoice = new Choice();
        for (int i = 1; i <= 31; i++) {
            dayChoice.add(String.valueOf(i));
        }
        dayChoice.setBounds(150, 230, 50, 30);
        add(dayChoice);

        monthChoice = new Choice();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (String month : months) {
            monthChoice.add(month);
        }
        monthChoice.setBounds(210, 230, 70, 30);
        add(monthChoice);

        yearChoice = new Choice();
        for (int i = 1980; i <= 2024; i++) {
            yearChoice.add(String.valueOf(i));
        }
        yearChoice.setBounds(290, 230, 60, 30);
        add(yearChoice);

        Label addressLabel = new Label("Address:");
        addressLabel.setBounds(50, 280, 100, 30);
        add(addressLabel);

        addressArea = new TextArea();
        addressArea.setBounds(150, 280, 200, 60);
        add(addressArea);

        acceptCheckbox = new Checkbox("Accept Terms and Conditions");
        acceptCheckbox.setBounds(50, 360, 250, 30);
        add(acceptCheckbox);

        submitButton = new Button("Submit");
        submitButton.setBounds(100, 400, 80, 30);
        submitButton.addActionListener(this);
        add(submitButton);

        resetButton = new Button("Reset");
        resetButton.setBounds(200, 400, 80, 30);
        resetButton.addActionListener(this);
        add(resetButton);

        Label outputLabel = new Label("Output:");
        outputLabel.setBounds(450, 50, 100, 30);
        outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(outputLabel);

        outputArea = new TextArea();
        outputArea.setBounds(450, 80, 300, 300);
        outputArea.setEditable(false);
        add(outputArea);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            
            if (nameField.getText().isEmpty()) {
                showErrorDialog("Please enter your name.");
                return;
            }
            
            if (!nameField.getText().matches("[a-zA-Z ]+") || nameField.getText().length() < 4) {
                showErrorDialog("Name should contain only alphabetic characters and be at least 4 characters long.");
                return;
            }

            String mobileText = mobileField.getText();
            if (mobileText.isEmpty() || mobileText.length() != 8 || !mobileText.matches("\\d+")) {
                showErrorDialog("Please enter an 8-digit mobile number.");
                return;
            }

            if (!acceptCheckbox.getState()) {
                showErrorDialog("Please accept the Terms and Conditions.");
                return;
            }

            int day = Integer.parseInt(dayChoice.getSelectedItem());
            int month = monthChoice.getSelectedIndex() + 1;  
            int year = Integer.parseInt(yearChoice.getSelectedItem());

            LocalDate dob = LocalDate.of(year, month, day);
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(dob, currentDate).getYears();

            // Write data to a text file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("registration_data.txt", true))) {
                writer.write("Name: " + nameField.getText());
                writer.newLine();
                writer.write("Mobile: " + prefixChoice.getSelectedItem() + mobileText);
                writer.newLine();
                writer.write("Gender: " + (maleCheckbox.getState() ? "Male" : femaleCheckbox.getState() ? "Female" : "Unspecified"));
                writer.newLine();
                writer.write("DOB: " + dayChoice.getSelectedItem() + "-" + monthChoice.getSelectedItem() + "-" + yearChoice.getSelectedItem());
                writer.newLine();
                writer.write("Age: " + age + " years");
                writer.newLine();
                writer.write("Address: " + addressArea.getText());
                writer.newLine();
                writer.write("----------------------------------------------------");
                writer.newLine();
                showMessage("Form data saved successfully!");
            } catch (IOException ex) {
                showErrorDialog("Error saving data to file: " + ex.getMessage());
            }

            successDialog.setVisible(true);

            outputArea.setText("Form Submitted Successfully!\n\n");
            outputArea.append("Name: " + nameField.getText() + "\n");
            outputArea.append("Mobile: " + prefixChoice.getSelectedItem() + mobileText + "\n");
            outputArea.append("Gender: " + (maleCheckbox.getState() ? "Male" : femaleCheckbox.getState() ? "Female" : "Unspecified") + "\n");
            outputArea.append("DOB: " + dayChoice.getSelectedItem() + "-" + monthChoice.getSelectedItem() + "-" + yearChoice.getSelectedItem() + "\n");
            outputArea.append("Age: " + age + " years\n");
            outputArea.append("Address: " + addressArea.getText());
        } else if (e.getSource() == resetButton) {
            
            nameField.setText("");
            mobileField.setText("");
            prefixChoice.select(0);
            dayChoice.select(0);
            monthChoice.select(0);
            yearChoice.select(0);
            addressArea.setText("");
            maleCheckbox.setState(false);
            femaleCheckbox.setState(false);
            acceptCheckbox.setState(false);
            outputArea.setText("");
        }
    }

    private void showMessage(String message) {
        Label messageLabel = new Label(message);
        Dialog messageDialog = new Dialog(this, "Message", true);
        messageDialog.setSize(300, 150);
        messageDialog.setLayout(new FlowLayout());
        messageDialog.add(messageLabel);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> messageDialog.setVisible(false));
        messageDialog.add(okButton);
        messageDialog.setVisible(true);
    }

    private void showErrorDialog(String message) {
        ((Label) errorDialog.getComponent(0)).setText(message);
        errorDialog.setVisible(true);
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
