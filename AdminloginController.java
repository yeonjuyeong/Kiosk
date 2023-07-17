package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminloginController {
	@FXML Button LoginButtion, ClearButton, CloseButton;
	
	@FXML TextField IdTextField;
	
	@FXML PasswordField PwPasswordField;

	@FXML
	private void LoginButtonAction(ActionEvent event) {
		if(IdTextField.getText().isEmpty() || PwPasswordField.getText().isEmpty()) {
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("둘다 입력");
			alert.setHeaderText("ecyce");
			alert.setContentText("아이디와 비번을 둘다 입력");
			alert.show();
		}else {
			DBconnect2 conn = new DBconnect2();
			Connection conn2 = conn.getconn();
			
			
			String sql = "select adminid, adminpw"
					+ " from admin_accounts"
					+ " where adminid = ?"
					+ " and adminpw = ?";
			
			try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				
				ps.setString(1, IdTextField.getText());
				ps.setString(2, PwPasswordField.getText());
				
				ResultSet rs = ps.executeQuery();
				
				
				if(rs.next()) {
					System.out.println("로그인 성공");
					try {
						Parent root = FXMLLoader.load(getClass().getResource("admindb.fxml"));
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.setTitle("관리자 DB 조회");
						stage.show();
					} catch(Exception e) {
						e.printStackTrace();
					}
					CloseButtonAction(event);
									
				}else {
					System.out.println("로그인 실패");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@FXML
	private void ClearButttonAction(ActionEvent event) {
		IdTextField.setText("");
		PwPasswordField.setText("");
	}
	
	
	@FXML
	private void CloseButtonAction(ActionEvent event) {
		Stage stage = new Stage();
		stage = (Stage)CloseButton.getScene().getWindow();
		stage.close();
	}
	
	
	
}
