package main;

import java.util.HashMap;
import java.util.Map;

import domain.UserVO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.LoginController;
import views.MainController;
import views.MasterController;
import views.MyMailController;
import views.ReceiveMailController;
import views.RegisterController;
import views.SendMailController;
import views.ViewerController;
import views.WriteMailController;

public class MainApp extends Application {
	public static MainApp app;

	private StackPane mainPage = null;

	private Map<String, MasterController> controllerMap = new HashMap<>();
	
	public MasterController getController(String name) {
		return controllerMap.get(name);
	}
	
	@Override
	public void start(Stage primaryStage) {
		app = this; // 싱글톤으로 만들어준다.
		try {
			FXMLLoader mainLoader = new FXMLLoader();
			mainLoader.setLocation(getClass().getResource("/views/MainLayout.fxml"));
			mainPage = mainLoader.load();

			MainController mc = mainLoader.getController();
			mc.setRoot(mainPage);
			controllerMap.put("main", mc);

			FXMLLoader loginLoader = new FXMLLoader();
			loginLoader.setLocation(getClass().getResource("/views/LoginLayout.fxml"));
			AnchorPane loginPage = loginLoader.load();

			LoginController lc = loginLoader.getController();
			lc.setRoot(loginPage);
			controllerMap.put("login", lc);

			// 회원가입 페이지 로드
			FXMLLoader registerLoader = new FXMLLoader();
			registerLoader.setLocation(getClass().getResource("/views/RegisterLayout.fxml"));
			AnchorPane registerPage = registerLoader.load();

			RegisterController rc = registerLoader.getController();
			rc.setRoot(registerPage);
			controllerMap.put("register", rc);

			// 받은메일함 페이지 로드
//			FXMLLoader receiveMailLoader = new FXMLLoader();
//			receiveMailLoader.setLocation(getClass().getResource("/views/ReceiveMail.fxml"));
//			AnchorPane receiveMail = receiveMailLoader.load();
//
//			ReceiveMailController RMC = receiveMailLoader.getController();
//			RMC.setRoot(receiveMail);
//			controllerMap.put("receiveMail", RMC);

//			 받은메일함 페이지 로드
			FXMLLoader receiveMailLoader = new FXMLLoader();
			receiveMailLoader.setLocation(getClass().getResource("/views/ReceiveMail.fxml"));
			AnchorPane receiveMail = receiveMailLoader.load();

			ReceiveMailController RMC = receiveMailLoader.getController();
			RMC.setRoot(receiveMail);
			controllerMap.put("receiveMail", RMC);

			// 메일 쓰기 페이지 로드
			FXMLLoader writeMailLoader = new FXMLLoader();
			writeMailLoader.setLocation(getClass().getResource("/views/NotepadLayout.fxml"));
			AnchorPane writeMail = writeMailLoader.load();

			WriteMailController WMC = writeMailLoader.getController();
			WMC.setRoot(writeMail);
			controllerMap.put("writeMail", WMC);

			// 보낸메일함 로드
			FXMLLoader sendMailLoader = new FXMLLoader();
			sendMailLoader.setLocation(getClass().getResource("/views/SendMail.fxml"));
			AnchorPane sendMail = sendMailLoader.load();

			SendMailController SML = sendMailLoader.getController();
			SML.setRoot(sendMail);
			controllerMap.put("sendMail", SML);

			// 내게쓴메일함 로드
			FXMLLoader myMailLoader = new FXMLLoader();
			myMailLoader.setLocation(getClass().getResource("/views/MyMail.fxml"));
			AnchorPane myMail = myMailLoader.load();

			MyMailController MMC = myMailLoader.getController();
			MMC.setRoot(myMail);
			controllerMap.put("myMail", MMC);

			// 뷰어(메일 읽기) 로드
			FXMLLoader viewerLoader = new FXMLLoader();
			viewerLoader.setLocation(getClass().getResource("/views/Viewer.fxml"));
			AnchorPane viewer = viewerLoader.load();

			ViewerController VC = viewerLoader.getController();
			VC.setRoot(viewer);
			controllerMap.put("viewer", VC);

			//
			Scene scene = new Scene(mainPage);
			mainPage.getChildren().add(loginPage);

			scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.setTitle("10116 박명재 - 더퍼니잇 메일");
			Image icon = new Image("/images/Mail_iOS.png");
			primaryStage.getIcons().add(icon);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("앱 로딩중 오류 발생");
		}

	}

	public void loadPage(String name) {
		MasterController c = controllerMap.get(name);
		c.init();
		Pane pane = c.getRoot();
		mainPage.getChildren().add(pane);

		pane.setTranslateX(-800);
		pane.setOpacity(0);

		Timeline timeline = new Timeline();
		KeyValue toRight = new KeyValue(pane.translateXProperty(), 0);
		KeyValue fadeIn = new KeyValue(pane.opacityProperty(), 1);

		KeyFrame frame = new KeyFrame(Duration.millis(500), toRight, fadeIn);

		timeline.getKeyFrames().add(frame);
		timeline.play();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void slideOut(Pane pane) {
		Timeline timeline = new Timeline();
		KeyValue toRight = new KeyValue(pane.translateXProperty(), 800);
		KeyValue fadeOut = new KeyValue(pane.opacityProperty(), 0);

		KeyFrame frame = new KeyFrame(Duration.millis(500), (e) -> {
			mainPage.getChildren().remove(pane);
		}, toRight, fadeOut);

		timeline.getKeyFrames().add(frame);
		timeline.play();
	}

	public void setLoginInfo(UserVO vo) {
		MainController mc = (MainController) controllerMap.get("main");
		mc.setLoginInfo(vo);

		ReceiveMailController rmc = (ReceiveMailController) controllerMap.get("receiveMail");
		rmc.setLoginInfo(vo);

		WriteMailController wmc = (WriteMailController) controllerMap.get("writeMail");
		wmc.setLoginInfo(vo);

		SendMailController smc = (SendMailController) controllerMap.get("sendMail");
		smc.setLoginInfo(vo);

		MyMailController mmc = (MyMailController) controllerMap.get("myMail");
		mmc.setLoginInfo(vo);

		ViewerController vc = (ViewerController) controllerMap.get("viewer");
		vc.setLoginInfo(vo);
	}
}