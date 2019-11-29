package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import domain.TableVO;
import domain.UserVO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class WriteMailController extends MasterController {
	@FXML
	private AnchorPane writeMail;

	@FXML
	private Button receiveMailBtn;

	@FXML
	private Button sendMailBtn;

	@FXML
	private Button mysendMailBtn;

	@FXML
	private Button tempMailBtn;

	@FXML
	private Label userInfo;

	@FXML
	private Button sendBtn;

	@FXML
	private TextField receive;

	@FXML
	private TextField title;

	@FXML
	private HTMLEditor content;

	private String owner;
	
	@FXML
	private TableView<TableVO> tableView;

	private UserVO user;

	public UserVO getUser() {
		return user;
	}

	public void setLoginInfo(UserVO vo) {
		this.user = vo;
		userInfo.setText(vo.getName() + "(" + vo.getId() + ")" + "님");
		owner = vo.getId();
	}

	public void receiveMail() {
		MainApp.app.loadPage("receiveMail");
		MainApp.app.slideOut(writeMail);
	}
	
	public void sendMail() {
		MainApp.app.slideOut(writeMail);
		MainApp.app.loadPage("sendMail");
	}
	
	public void myMail() {
		MainApp.app.slideOut(writeMail);
		MainApp.app.loadPage("myMail");
	}

	public void send() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		System.out.println(receive.getText());
		System.out.println(title.getText());
		System.out.println(content.getHtmlText());

		Date date = new Date();
		SimpleDateFormat rule = new SimpleDateFormat("yyyy.MM.dd");

		String sqlInsert = "INSERT INTO mail_item (`title`, `content`, `date`, `owner`, `receive`) "
				+ " VALUES (?, ?, ?, ?, ?)";

		String sqlUserUse = "SELECT * FROM mail WHERE id = ?";

		try {
			if (receive.getText().equals("") || title.getText().equals("")) {
				//누락된 값이 있는 경우
				Util.showAlert("누락된 항목", "누락된 항목이 있습니다.", AlertType.ERROR);
				return;
			}

			pstmt = con.prepareStatement(sqlUserUse);
			pstmt.setString(1, receive.getText());
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				// 아이디가 존재하지 않으면...
				Util.showAlert("존재하지 않는  받는이", "받는 사람의 계정이 존재하지 않습니다.", AlertType.ERROR);
				return;
			}

			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);

			pstmt = con.prepareStatement(sqlInsert);
			pstmt.setString(1, title.getText());
			pstmt.setString(2, content.getHtmlText());
			pstmt.setString(3, rule.format(date));
			pstmt.setString(4, owner);
			pstmt.setString(5, receive.getText());

			if (pstmt.executeUpdate() != 1) {
				Util.showAlert("에러", "데이터베이스 실행중 오류", AlertType.ERROR);
				return;
			}

			Util.showAlert("성공", "메일이 성공적으로 발송되었습니다.", AlertType.INFORMATION);
			MainApp.app.slideOut(writeMail);
			MainApp.app.loadPage("receiveMail");
			
//			ReceiveMailController rmc = new ReceiveMailController();
//			rmc.reload();
			
			//변수 초기화
			title.setText("");
			content.setHtmlText("");
			receive.setText("");
		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "DB오류, 메일 전송에 실패하였습니다.", AlertType.ERROR);
			return;
		} finally {
			JDBCUtil.close(rs);
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
}