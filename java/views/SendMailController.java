package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import domain.TableVO;
import domain.UserVO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import main.MainApp;
import util.JDBCUtil;
import util.Util;

public class SendMailController extends MasterController {
	@FXML
	private Button logoutBtn;

	@FXML
	private Button receiveMailBtn;

	@FXML
	private Button sendMailBtn;

	@FXML
	private Button mysendMailBtn;

	@FXML
	private Button tempMailBtn;

	@FXML
	private TextArea userInfo;

	@FXML
	private AnchorPane reAnchorPane;

//	@FXML
//	private ListView<String> listView;

// 테이블 부분
	@FXML
	private TableView<TableVO> tableView;
	@FXML
	private TableColumn<TableVO, Integer> idColumn;
	@FXML
	private TableColumn<TableVO, String> ownerColumn;
	@FXML
	private TableColumn<TableVO, String> titleColumn;
	@FXML
	private TableColumn<TableVO, String> dateColumn;

//	테이블 부분 끝

	private ObservableList<TableVO> tableList = FXCollections.observableArrayList();

//	private ObservableList<String> item;

	private Map<String, MasterController> controllerMap = new HashMap<>();

	private UserVO user;

	public UserVO getUser() {
		System.out.println(user);
		return user;
	}

	@FXML
	public void initialize() {
		Date date = new Date();
		SimpleDateFormat rule = new SimpleDateFormat("yyyy.MM.dd");

		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		ownerColumn.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		tableView.setItems(tableList);

		try {
			TableVO t = new TableVO(new SimpleIntegerProperty(1), new SimpleStringProperty("관리자"),
					new SimpleStringProperty("\"새로 고침\" 버튼을 눌러 갱신해주세요. "), new SimpleStringProperty(rule.format(date)));

			tableList.add(t);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("불러오기 실패");
		}
	}

	public void setLoginInfo(UserVO vo) {
		this.user = vo;
		userInfo.setText(vo.getName() + "(" + vo.getId() + ")" + "님");
	}
	
	public void logout() {
		tableView.getItems().clear();
		tableList.clear();

		Date date = new Date();
		SimpleDateFormat rule = new SimpleDateFormat("yyyy.MM.dd");

		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		ownerColumn.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		tableView.setItems(tableList);

		try {
			TableVO t = new TableVO(new SimpleIntegerProperty(1), new SimpleStringProperty("관리자"),
					new SimpleStringProperty("\"새로 고침\" 버튼을 눌러 갱신해주세요. "), new SimpleStringProperty(rule.format(date)));

			tableList.add(t);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("불러오기 실패");
		}

		user = null;

		MainApp.app.slideOut(reAnchorPane);
		MainApp.app.loadPage("receiveMail");
		MainApp.app.loadPage("login");
	}


	public void receiveMail() {
		MainApp.app.slideOut(reAnchorPane);
		MainApp.app.loadPage("receiveMail");
	}

	public void writeMail() {
		MainApp.app.slideOut(reAnchorPane);
		MainApp.app.loadPage("writeMail");
	}

	public void myMail() {
		MainApp.app.slideOut(reAnchorPane);
		MainApp.app.loadPage("myMail");
	}

	public void delete() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;

		String sql = "DELETE FROM `mail_item` WHERE owner = ?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			pstmt.executeUpdate();

			Util.showAlert("비우기 완료", "메일함 비우기에 성공하였습니다.", AlertType.INFORMATION);

			tableView.getItems().clear();
			tableList.clear();
		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("비우기 실패", "메일함 비우기에 실패하였습니다.", AlertType.ERROR);
		}
	}

	public void unitDelete() {
		Connection con = JDBCUtil.getConnection();
		PreparedStatement pstmt = null;
		
		TableVO vo = tableView.getSelectionModel().getSelectedItem();
		idViewer = vo.idProperty().intValue();

		String sql = " DELETE FROM `mail_item` WHERE id = ? ";
		
		try {			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idViewer);
			pstmt.executeUpdate();

			Util.showAlert("삭제 완료", "1개의 메일을 삭제하였습니다. 갱신해주세요.", AlertType.INFORMATION);

		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("삭제 실패", "1개의 메일이 삭제되지 않았습니다.", AlertType.ERROR);
		}
	}

	public void reload() {
		Connection con = JDBCUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String sql = "SELECT `id`, `owner`, `title`, `date`  FROM `mail_item` WHERE owner = ? ORDER BY `id` DESC";

		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
		ownerColumn.setCellValueFactory(cellData -> cellData.getValue().ownerProperty());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		tableView.setItems(tableList);

		try {
			tableView.getItems().clear();
			tableList.clear();

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getId());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				TableVO t = new TableVO(new SimpleIntegerProperty(rs.getInt("id")),
						new SimpleStringProperty(rs.getString("owner")),
						new SimpleStringProperty(rs.getString("title")),
						new SimpleStringProperty(rs.getString("date")));

				tableList.add(t);

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("불러오기 실패");
		}

	}

	public int idViewer = 0;

	public void select() {
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() > 1) {
					TableVO vo = tableView.getSelectionModel().getSelectedItem();

					// 메일번호 value값
					System.out.println(vo.idProperty().intValue());
					MainApp.app.loadPage("viewer");
					idViewer = vo.idProperty().intValue();

					ViewerController vc = (ViewerController) MainApp.app.getController("viewer");
					vc.val(idViewer);

				}
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
}
