import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;

public class TestMain extends Application{





    @Override
    public void start(Stage stage) throws Exception {

	VideoCapture capture = new VideoCapture(0);

	Mat frame = new Mat();
	MatOfByte buffer = new MatOfByte();
	MatOfRect faceDetections = new MatOfRect();
	MatOfRect rightEyeDetections = new MatOfRect();
	MatOfRect leftEyeDetections = new MatOfRect();
	MatOfRect eyeDetections = new MatOfRect();

	CascadeClassifier faceDetector = new CascadeClassifier("resources/haarcascade_frontalface_alt.xml");
	CascadeClassifier rightEyeDetector = new CascadeClassifier("resources/haarcascade_righteye_2splits.xml");
	CascadeClassifier leftEyeDetector = new CascadeClassifier("resources/haarcascade_lefteye_2splits.xml");
	CascadeClassifier eyeDetector = new CascadeClassifier("resources/haarcascade_eye.xml");


	ImageView iView = new ImageView();
	//iView.setImage();


	Timer timer = new Timer();
	timer.scheduleAtFixedRate(new TimerTask() {



	    @Override
	    public void run() {
		capture.read(frame);

		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(frame, frame);
		//Imgproc.threshold(frame, frame, 90, 255, Imgproc.THRESH_BINARY);

		faceDetector.detectMultiScale(frame, faceDetections, 1.1, 5, 0, new Size(30,30), new Size());
		rightEyeDetector.detectMultiScale(frame, rightEyeDetections, 1.1, 5, 0, new Size(30,30), new Size());
		leftEyeDetector.detectMultiScale(frame, leftEyeDetections, 1.1, 5, 0, new Size(30,30), new Size());

		Rect[] faces = faceDetections.toArray();
		Rect[] rEyes = rightEyeDetections.toArray();
		Rect[] lEyes = leftEyeDetections.toArray();

		Rect face;

		if(faces.length > 0) {
		    face = faces[0];
		} else {
		    face = new Rect(0,0,2000,2000);
		}


		Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0,0,255,255), 2);


		double minX = 2000;
		int rInd = -1;

		double maxX = 0;
		int lInd = -1;

		for (int i = 0; i < rEyes.length; i++) {

		    Rect rect = rEyes[i];
		    if(!face.contains(rect.tl()) || !face.contains(rect.br())) continue;

		    if(rect.tl().x < minX) {
			minX = rect.tl().x;
			rInd = i;
		    }

		    
		}

		for (int i = 0; i < lEyes.length; i++) {
		    Rect rect = lEyes[i];
		    if(!face.contains(rect.tl()) || !face.contains(rect.br())) continue;

		    if(rect.br().x > maxX) {
			maxX = rect.br().x;
			lInd = i;
		    }

		    
		}

		if(rInd != -1) 
		    Imgproc.rectangle(frame, rEyes[rInd].tl(), rEyes[rInd].br(), new Scalar(0,255,0,255), 2);

		if(lInd !=-1)
		    Imgproc.rectangle(frame, lEyes[lInd].tl(), lEyes[lInd].br(), new Scalar(0,255,0,255), 2);

		Imgcodecs.imencode(".png", frame, buffer);

		Image img = new Image(new ByteArrayInputStream(buffer.toArray()));
		iView.setImage(img);

		//		if(rInd == -1 || lInd == -1) return;
		//
		//		Rect rightEye = rEyes[rInd];
		//		Rect leftEye = lEyes[lInd];
		//
		//
		//		Mat roi1 = new Mat(mainFrame, rightEye);
		//		Mat roi2 = new Mat(mainFrame, leftEye);
		//
		//		Imgproc.threshold(roi1, roi1, 120, 255, Imgproc.THRESH_BINARY);
		//		Imgproc.threshold(roi2, roi2, 120, 255, Imgproc.THRESH_BINARY);
		//	
		//		roi1.convertTo(roi1, CvType.CV_64FC3);
		//		roi2.convertTo(roi2, CvType.CV_64FC3);
		//	
		//		int size1 = (int) (roi1.total() * roi1.channels());
		//		double[] rArr = new double[size1]; 
		//		roi1.get(0,0,rArr);	
		//			
		//		int totalIntensityR = 0;
		//	
		//		//for(int y = 0; y < roi1.height(); y++){
		//		    for(int x=0; x < size1; x++) {
		//			System.out.println(rArr[x]);
		//		    }
		//		//}
		//
		//		int whiteR = Core.countNonZero(roi1);
		//		int whiteL = Core.countNonZero(roi2);

		//		if(whiteR / (rightEye.width * rightEye.height) > 0.5) {
		//		    System.out.println("Open");
		//		} else {
		//		    System.out.println("Closed");
		//		}

	    }

	}, 0, 33);



	//	Button btn = new Button();
	//	btn.setText("Text");
	//
	//
	//	btn.setOnAction(new EventHandler<ActionEvent>() {
	//
	//	    @Override
	//	    public void handle(ActionEvent event) {
	//
	//
	//		System.out.println("result");
	//	    }
	//	});

	VBox box = new VBox();
	box.getChildren().add(iView);
	//box.getChildren().add(btn);

	StackPane root = new StackPane();
	root.getChildren().add(box);

	Scene scene = new Scene(root, 300, 250);

	stage.setTitle("Title");
	stage.setScene(scene);
	stage.show();
    }

    public static void main(String[] args) {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	launch(args);
    }

}
