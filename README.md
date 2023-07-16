# Kiosk

## main
<br>밑에 코드는 JavaFX에서 SceneBuilder에 만들어놓은 결과물을 가져오는 코드이다.
``` java
Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("성일CAFE - 5월");
			primaryStage.show();
```
![image](https://github.com/yeonjuyeong/Kiosk/assets/123055714/58f0b6fe-3bbf-4cc8-bbf6-84593f3260ec)

## samplecontroller
<br>버튼만들기
```java
 @FXML private Button
```

<br>메뉴를 만들어 배열에 저장,kiosksum클래스의 인스턴스를 생성함
```java
    private int sum=0;
    private String[] menu_name = {"아메리카노", "카푸치노", "카페라떼"};  
    private int countm[] = new int[3];
    
    Kiosksum kiosksum = new Kiosksum();
```

<br>m1p버튼이라는 것에 액션이벤트로 버튼이 상호작용가능하게 만들어 menu_append에 넣음
```java
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
```

<br>손님이 주문을 취소할경우를 위해 값들을 지우는 cancle버튼을 만들고 손님이 주문을 하기전 총액에 값을 띄우기 위해 sum버튼을 만듦
```java
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
```

<br>adminlogin이라는 SceneBuilder에서 만들어놓은 결과물을 가져오는 코드
```java
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
```

<br>총주문금액이 0이면 메세지 출력(메뉴선택, 계산하기 버튼 클릭후 주문해주세요) 그 외에는(총주문금액이 0이 아니면)
<br>DB접속 sql문 작성(현재 주문내역을 orderlist_accounts 테이블에 삽입하기)쿼리 실행 실행 결과가 있으면 메시지출력(주문 완료됨) 변수값과 화면 내용 초기화
<br>그 외에는(실행 결과가 없으면)==> 메시지 출력(주문오류:카운터에 문의)
```java
    @FXML
    public void OrderButtonAction(ActionEvent event) {
    	if (sum !=0) {
    		DBconnect conn = new DBconnect();
    		Connection conn2 = conn.getConnection();
    	String sql="insert into orderlist_accounts"
    			+ " (idx, order_time, menu1, count1, menu2, count2, menu3, count3, total)"
    			+ " values"
    			+ " (orderlist_idx_pk.nextval, current_timestamp, '아메리카노', ?, '카푸치노', ?, '카페라떼', ?, ? )";
    	try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			ps.setInt(1, countm[0]);
			ps.setInt(2, countm[1]);
			ps.setInt(3, countm[2]);
			ps.setInt(4, sum);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("주문이 되었습니다.");
				alert.show();

				countm[0]=0;
				countm[1]=0;
				countm[2]=0;
				sum=0;
				ListTextArea.setText("");
				sumLabel.setText("");
			}else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("다시 주문해주세요.");
				alert.show();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		}else {
			//경고매세지
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("계산하기 버튼 클릭후 주문하세요");
    		alert.show(); 
		}	
    	    	
    }
```
<br>관리자의 아이디, 비밀번호를 저장한 sql문
  ![image](https://github.com/yeonjuyeong/Kiosk/assets/123055714/b3af9533-287a-43bd-a488-4e65e7beabdc)
<br>사용자의 주문번호,사용시간,메뉴1,메뉴2,메뉴3,총합값 등을 저장하는 sql문
![image](https://github.com/yeonjuyeong/Kiosk/assets/123055714/c94162d1-cacf-4f43-888c-469c590aa9c6)

## dbconnect
<br>db의 드라이버, url, 이름, 비밀번호를 지정, 드라이버 클래스를 동적으로 로드한 후 데이터베이스에 연결함
```java
		String driver="oracle.jdbc.driver.OracleDriver";//데이터베이스 드라이버의 클래스 이름을 지정합니다.
		String url="jdbc:oracle:thin:@localhost:1521:xe";//데이터베이스 연결 URL을 지정합니다.
		String user="cafe2";//데이터베이스에 사용할 사용자 이름을 지정합니다.
		String password="cafe2";//데이터베이스에 사용할 사용자 비밀번호를 지정합니다.
```

<br> 드라이버 클래스를 동적으로 로드한 후 데이터베이스에 연결한 후 디비 연결 성공, 실패를 출력
```java
		try {
			Class.forName(driver);
			DBconn=DriverManager.getConnection(url, user, password);
			System.out.println("디비 연결 성공-20230509");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 연결 실패-20230509");
		}
		
		
		
		
		return DBconn;
```

## adminlogin

![image](https://github.com/yeonjuyeong/Kiosk/assets/123055714/2423aefb-7e75-4790-8407-ead0c472016d)

<br>아이디 칸과 비밀번호칸에 문자를 지우는 버튼과 어드민로그인창을 닫는 버튼
```java
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
```

<br>아이디와 비밀번호가 모두 입력되어 있는지 확인
```java
	@FXML
	private void LoginButtonAction(ActionEvent event) {
		if(IdTextField.getText().isEmpty()||PwPasswordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("아이디와 비번을 둘다 입력");
			alert.show();
		}
```

<br>아이디와 비밀번호에 값이 들어있다면 DB에 있는 admin_id와 admin_password를 조회함
```java
else {
			DBconnect conn = new DBconnect();
			Connection conn2 = conn.getConnection();
			
			String sql = "select admin_id, admin_password"
					+ " from admin_accounts"
					+ " where admin_id = ?"
					+ " and admin_password = ?";
```

<br>sql문을 준비한 후 첫번째 ?를 IdTextField에 넣고 두번째 ?를 PwPasswordField에 텍스트 값을 설정한다. sql을 실행하고 그 결과를 rs 변수에 저장한다.
```java
			try {
				PreparedStatement ps = conn2.prepareStatement(sql);
				
				ps.setString(1, IdTextField.getText());
				ps.setString(2, PwPasswordField.getText());
				
				ResultSet rs = ps.executeQuery();
```

<br>rs에 값이 db에 있는 값과 똑같으면 실행해서 어드민로그인창을 띄우고 다르면 로그인 실패창을 띄운다.
```java
				if(rs.next()) {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("Admindb.fxml"));
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();
					} catch (IOException e) {
						e.printStackTrace();
					}	
				}else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("로그인 실패");
					alert.show();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   ```

### 







