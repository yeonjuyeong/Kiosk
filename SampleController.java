package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class SampleController {

    @FXML private Button CalButton, CancleButton;
    @FXML private Button M1pButton, M2pButton, M3pButton;
    @FXML private Button M1mButton, M2mButton, M3mButton;
    @FXML private TextArea ListTextArea;
    @FXML private Label sumLabel;
    @FXML private Button AdminButton, OrderButton;
    
    private int sum=0;
    private String[] menu_name = {"아메리카노", "카푸치노", "카페라떼"};  
    private int countm[] = new int[3];
    
    Kiosksum kiosksum = new Kiosksum();
    
    @FXML
    public void M1pButtonAction(ActionEvent event) {
    	countm[0]=countm[0]+1;
    	menu_append();
    }

	private void menu_append() {		
		ListTextArea.setText("");
		for(int i=0; i<3;i++) {
			ListTextArea.appendText(menu_name[i] + " : " + countm[i] + "잔"+"\n");
		}	
	}

	@FXML
	public void M2pButtonAction(ActionEvent event) {
    	countm[1]++;    	
    	menu_append();
    }
    
    @FXML
    public void M3pButtonAction(ActionEvent event) {
    	countm[2]++;    	
    	menu_append();
    }
    
    
    @FXML
    public void M1mButtonAction(ActionEvent event) {
    	if(countm[0]>0) countm[0]--;
    	menu_append();
    }

    @FXML
    public void M2mButtonAction(ActionEvent event) {
    	if(countm[1]>0) countm[1]--;
    	menu_append();
    }
    
    @FXML
    public  void M3mButtonAction(ActionEvent event) {
    	if(countm[2]>0) countm[2]--;
    	menu_append();
    }
    
    
    @FXML
    public void CancleButtonAction(ActionEvent event) {
    	sumLabel.setText("0");
    	ListTextArea.setText("");
    	for(int i=0;i<3; i++) {
    		countm[i]=0;
    	}	
    }


    @FXML
    public void SumButtonAction(ActionEvent event) {
    	sum = kiosksum.ksum(countm);
    	sumLabel.setText(sum + "");
    }
    
    
    @FXML
    public void AdminButtonAction(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("adminlogin.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("관리자 로그인 화면-5월");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    
    }
    
    
    @FXML
    public void OrderButtonAction(ActionEvent event) {
    	if(sum != 0) {
    		//디비접속
    		DBconnect2 conn = new DBconnect2();
    		Connection conn2 = conn.getconn();
    		
    	String sql="insert into orderlist_accounts1"
    			+ " (idx, order_time, menu1, count1, menu2, count2, menu3, count3, sum)"
    			+ " values"
    			+ " (orderlist_idx_pk1.nextval, current_timestamp, '아메리카노', ?, '카푸치노', ?, '카페라떼', ?, ?)";
    	
    	try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			// PreparedStatement= sql을 저장하는 방같은 개념 conn2에 있는 sql을 ps방에 넣음
			
			ps.setInt(1, countm[0]);
			ps.setInt(2, countm[1]);
			ps.setInt(3, countm[2]);
			ps.setInt(4, sum);
			//sql문에 있는 count1,2,3 방안에 들어있는 ?를 바꿈
			ResultSet rs = ps.executeQuery();
			//rs값이 있으면 
				// 주문 성공 매세지 띄우기
				//화면에 보이는 내용 초기화, 실제 변수값도 초기화

			if(rs.next()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ecyce");
				alert.setContentText("주문이 완료되었습니다.");
				alert.show();
				
				//화면에 보이는 값초기화
				ListTextArea.setText("");
				sumLabel.setText("0");
				//화면에 보이지 않는 값 초기화
				countm[0]=0;
				countm[1]=0;
				countm[2]=0;
				sum=0;
			}else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ecyce");
				alert.setContentText("주문오류 카운터에 문의하세요");
				alert.show();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	}else {
			//경고매세지
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("ecyce");
    		alert.setContentText("계산하기 버튼 클릭후 주문하세요");
    		alert.show();
		}
    }  
}
