package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdmindbController implements Initializable{

	int mcount1=0;
	int mcount2=0;
	int mcount3=0;
	int msum=0;
	
	@FXML
	Button searchButton,datesearchButton,datesearch2Button,countButton, sumButton;
	
	@FXML
	DatePicker dateDatePicker,startDatePicker,endDatePicker;
	
	@FXML
	PieChart rsPieChart;
	
	@FXML
	TextArea resultTextArea;
	
	@FXML
	TableView<Orderlist> orderlistTableView;
	//TableVoew<s> s는 각칼럼과 데이터형식이 일치하는 자료구조를 가진 클래스 파일명
	@FXML
	TableColumn<Orderlist, String> idxTableColumn;
	@FXML
	TableColumn<Orderlist, String> dateTableColumn;
	@FXML
	TableColumn<Orderlist, String> count1TableColumn;
	@FXML
	TableColumn<Orderlist, String> count2TableColumn;
	@FXML
	TableColumn<Orderlist, String> count3TableColumn;
	@FXML
	TableColumn<Orderlist, String> sumTableColumn;
	//TableColumn<s, t>
	//t는 s파일에서 선언한 변수의 데이터형
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("주문리스트 창이 열리고 초기화");
		idxTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
		count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
		count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
		sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
 
	}
	
	@FXML
	private void searchButtonAction(ActionEvent event) {
		//변수 초기화, textarea 초기화
		
		mcount1=0;
		mcount2=0;
		mcount3=0;
		msum=0;
		resultTextArea.setText("");
		
		DBconnect2 conn = new DBconnect2();
		Connection conn2 = conn.getconn();
	
		String sql="select idx, to_char(order_time, 'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
			+ " from orderlist_accounts1"
			+ " order by idx";
		
		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
			while(rs.next()) {
				datalist.add(
						new Orderlist(
								rs.getString(1),rs.getString(2),rs.getString(3),
								rs.getString(4),rs.getString(5),rs.getString(6)
								)
						);
				mcount1 = mcount1 + Integer.parseInt(rs.getString(3));
				mcount2 = mcount2 + Integer.parseInt(rs.getString(4));
				mcount3 = mcount3 + Integer.parseInt(rs.getString(5));
				msum = msum + Integer.parseInt(rs.getString(6));
			}
			
			orderlistTableView.setItems(datalist);
			resultTextArea.appendText("아메리카노 : "+mcount1+"잔\n");
			resultTextArea.appendText("카푸치노 : "+mcount2+"잔\n");
			resultTextArea.appendText("카페라떼 : "+mcount3+"잔\n");
			resultTextArea.appendText("총가격 : "+msum+"원");
			
			ps.close();
			rs.close();
			conn2.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void datesearchButtonAction(ActionEvent event) {
		//만약에 날짜가 비어있으면 ==> 경고메세지
		//그 외에는
			//디비 접속 ==> 해당 날짜의 데이터를 검색
		
		if(dateDatePicker.getValue()==null) {
			//경고메세지
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("ecyce");
			alert.setContentText("날짜를 먼저 선택하고 조회하세요");
			alert.show();
		}else {
			DBconnect2 conn = new DBconnect2();
			Connection conn2 = conn.getconn();
			
			//데이트피커에 있는 날짜에 해당하는 데이터를 검색해
			String sql="select idx, order_time, count1, count2, count3, sum"
					+ " from orderlist_accounts1"
					+ " where to_char(order_time, 'yyyy-mm-dd')=?"
					+ " order by idx";
			
			try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				
				ps.setString(1, dateDatePicker.getValue().toString());
				
				ResultSet rs = ps.executeQuery();
				
				ObservableList<Orderlist> datelist = FXCollections.observableArrayList();
				mcount1=0;
				mcount2=0;
				mcount3=0;
				msum=0;
				resultTextArea.setText("");
				while(rs.next()) {
					datelist.add(
							new Orderlist(
									rs.getString(1),rs.getString(2),rs.getString(3),
									rs.getString(4),rs.getString(5),rs.getString(6)
									)
							);
					mcount1 = mcount1 + Integer.parseInt(rs.getString(3));
					mcount2 = mcount2 + Integer.parseInt(rs.getString(4));
					mcount3 = mcount3 + Integer.parseInt(rs.getString(5));
					msum = msum + Integer.parseInt(rs.getString(6));
				}
				
				orderlistTableView.setItems(datelist);
				resultTextArea.appendText("아메리카노 : "+mcount1+"잔\n");
				resultTextArea.appendText("카푸치노 : "+mcount2+"잔\n");
				resultTextArea.appendText("카페라떼 : "+mcount3+"잔\n");
				resultTextArea.appendText("총가격 : "+msum+"원");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
			
		}
		
			
	}
		@FXML
		private void datesearch2ButtonAction(ActionEvent event) {
		//변수랑 textarea 초기화;
			
			if(startDatePicker.getValue()==null || endDatePicker.getValue()==null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ecyce");
				alert.setContentText("날짜를 둘다 선택하고 조회하세요");
				alert.show();
			}else {
				DBconnect2 conn = new DBconnect2();
				Connection conn2 = conn.getconn();
				
				String sql = "select idx, order_time, count1, count2, count3, sum"
						+" from orderlist_accounts1"
						+" where order_time >= ? and order_time <= ?"
						//+" where order_time between ? and ?"
						//주문일시가 A 날짜부터 B 날짜 사이인 경우
						+" order by idx";
				
				PreparedStatement ps;
				try {
					ps = conn2.prepareStatement(sql);
					ps.setString(1, startDatePicker.getValue().toString());
					ps.setString(2, endDatePicker.getValue().plusDays(1).toString());
					
					ResultSet rs = ps.executeQuery();
					
					ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
					mcount1=0;
					mcount2=0;
					mcount3=0;
					msum=0;
					resultTextArea.setText("");
					while(rs.next()) {
						datalist.add(
								new Orderlist(
										rs.getString(1),rs.getString(2),rs.getString(3),
										rs.getString(4),rs.getString(5),rs.getString(6)
										)
								);
						mcount1 = mcount1 + Integer.parseInt(rs.getString(3));
						mcount2 = mcount2 + Integer.parseInt(rs.getString(4));
						mcount3 = mcount3 + Integer.parseInt(rs.getString(5));
						msum = msum + Integer.parseInt(rs.getString(6));
					}
					
					orderlistTableView.setItems(datalist);
					resultTextArea.appendText("아메리카노 : "+mcount1+"잔\n");
					resultTextArea.appendText("카푸치노 : "+mcount2+"잔\n");
					resultTextArea.appendText("카페라떼 : "+mcount3+"잔\n");
					resultTextArea.appendText("총가격 : "+msum+"원");
					
						ps.close();
						rs.close();
						conn2.close();
					
					}
					
				 catch (SQLException e) {
					e.printStackTrace();
				 }
			}
			//만약 시작날짜 또는 종료날짜가 비어 있으면 ==> 경고메세지
		//그 외에는 DB접속
			//DB접속 ==> 해당 기간에 일치하는 데이터 검색 sql 작성
			//쿼리 세팅, 실행 ==> Resultset에 저장
			//어레이리스트 생성
			
			//while문으로 어레이리스트에 값을 추가(add)
			//변수값도 계산
			
			//테이블뷰에 표시하기(setItems)
			//통계도 표시
			
		}
	
	@FXML
	private void countButtonAction(ActionEvent event) {
		rsPieChart.setTitle("메뉴별 판매수량");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노"+mcount1+"잔", mcount1), 
				new PieChart.Data("카푸치노"+mcount2+"잔", mcount2),
				new PieChart.Data("카페라떼"+mcount3+"잔", mcount3)
				));
	}
	
	@FXML
	private void sumButtonAction(ActionEvent event) {
		rsPieChart.setTitle("메뉴별 판매금액");
		rsPieChart.setData(FXCollections.observableArrayList(
				new PieChart.Data("아메리카노"+mcount1*1000+"원", mcount1*1000), 
				new PieChart.Data("카푸치노"+mcount2*2000+"원", mcount2*2000),
				new PieChart.Data("카페라떼"+mcount3*3000+"원", mcount3*3000)
				));
	}
}
