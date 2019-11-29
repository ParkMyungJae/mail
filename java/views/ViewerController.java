package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import domain.TableVO;
import domain.UserVO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class ViewerController extends MasterController {
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

	@FXML
	public void initialize() {
		content.setDisable(true);

		receive.setText("");
		title.setText("");
		content.setHtmlText("");
	}

	// 해당 화면의 결과
	public void val(int e) {
		Connection con = JDBCUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String sql = "SELECT `owner`, `title`, `content` FROM `mail_item` WHERE id = ?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, e);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				receive.setText(rs.getString("owner"));
				title.setText(rs.getString("title"));
				content.setHtmlText(rs.getString("content"));

//				receive.setText("3");
//				title.setText("3");
//				content.setHtmlText("s");
			}

			pstmt.close();
			rs.close();
		} catch (Exception e2) {
			e2.printStackTrace();
			Util.showAlert("메일 보기 실패", "메일을 여는 도중 실패하였습니다.", AlertType.ERROR);
		}

		System.out.println(e);
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

	public void close() {
		MainApp.app.slideOut(getRoot());
	}

	public void myMail() {
		MainApp.app.slideOut(writeMail);
		MainApp.app.loadPage("myMail");
	}
	
	public void sendMail() {
		MainApp.app.slideOut(writeMail);
		MainApp.app.loadPage("sendMail");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
}
