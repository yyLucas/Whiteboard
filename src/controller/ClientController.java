package controller;

import interfaces.ClientCallbackInterface;
import interfaces.ServerConstants;
import interfaces.ServerInterface;

import java.net.URL;

import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable; 
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*; 
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * JavaFX controller, control the UI, action listeners, and mouse listeners.
 * @author Yang Zhang (Lucas)
 *
 */
public class ClientController implements Initializable { 
	public ClientCallbackInterface clientCallback;
	public ServerInterface chat;
	
	Optional<String> nickName;
	boolean draw_Line = true;
	boolean draw_Rect = false;
	boolean draw_Oval = false;
	
	double xCor;
	double yCor;
	
	@FXML Button btnSend;
	@FXML TextField txField;
	@FXML TextArea txArea;
	@FXML ListView<String> userList;
	@FXML Canvas canvas;
	
	@FXML Button btnGrey;
	@FXML Button btnBlack;
	@FXML Button btnBrown;
	@FXML Button btnGold;
	@FXML Button btnGreen;
	@FXML Button btnDarkCyan;
	@FXML Button btnDarkBlue;
	@FXML Button btnPurple;
	@FXML Button btnChocolate;
	@FXML Button btnOrange;
	@FXML Button btnViolet;
	@FXML Button btnBlue;
	@FXML Button btnCyan;
	@FXML Button btnLime;
	@FXML Button btnYellow;
	@FXML Button btnRed;
	
	@FXML Button drawPensil;
	@FXML Button drawCircle;
	@FXML Button drawRectangle;
	@FXML Button btnClear;
	
	
	GraphicsContext paint;


	@Override // This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		register();
		
		paint = canvas.getGraphicsContext2D();
		paint.setFill(Color.BLACK);
		paint.setLineWidth(5);
		
		btnSend.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					chat.send(clientCallback, txField.getText());
					txField.clear();
					//txArea.appendText(txField.getText());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	     
			}
		});
		
		btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {					
				try {
					clearCanvas();
					chat.sendClearCommand(clientCallback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

/* LINE BREAK, THE FOLLOWING IS *CANVAS* ACTION*/
/*
* 
*/
				
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				xCor = e.getX();
				yCor = e.getY();				
			}

		});
		
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				double finalX = e.getX();
				double finalY = e.getY();
				//The position is not changed
				//
				//
				if(xCor == finalX && yCor == finalY){
					try {
						chat.sendDrawingColor(clientCallback, getColorIndex() );
						if(draw_Line){
							drawLine(xCor, yCor);
							//chat.sendDrawingColor(clientCallback, getColorIndex() );
							chat.sendDrawingCommand(clientCallback, "line", xCor, yCor);
						}else if(draw_Oval){
							drawOval(xCor, yCor);
							//chat.sendDrawingColor(clientCallback, getColorIndex() );
							chat.sendDrawingCommand(clientCallback, "circle", xCor, yCor);
						}else if(draw_Rect){
							drawRectangle(xCor, yCor);
							//chat.sendDrawingColor(clientCallback, getColorIndex() );
							chat.sendDrawingCommand(clientCallback, "rectangle", xCor, yCor);
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				//The position has been changed
				//
				//
				}else{
					//System.out.println("moved");
					try {
						chat.sendDrawingColor(clientCallback, getColorIndex() );
						if(draw_Line){
							drawLine(xCor, yCor, finalX, finalY);
							chat.sendDrawingCommand(clientCallback, "line", xCor, yCor, finalX, finalY);
						}else if(draw_Oval){
							if(finalX >= xCor && finalY >= yCor){
								drawOval(xCor, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "circle", xCor, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else if(finalX >= xCor && finalY < yCor){
								drawOval(xCor, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "circle", xCor, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else if(finalX < xCor && finalY >= yCor){
								drawOval(finalX, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "circle", finalX, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else{
								drawOval(finalX, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "circle", finalX, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}							
						}else if(draw_Rect){
							if(finalX >= xCor && finalY >= yCor){
								drawRectangle(xCor, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "rectangle", xCor, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else if(finalX >= xCor && finalY < yCor){
								drawRectangle(xCor, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "rectangle", xCor, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else if(finalX < xCor && finalY >= yCor){
								drawRectangle(finalX, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "rectangle", finalX, yCor, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}else{
								drawRectangle(finalX, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
								chat.sendDrawingCommand(clientCallback, "rectangle", finalX, finalY, Math.abs(finalX-xCor), Math.abs(finalY-yCor));
							}
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		});
		

		
		
/* LINE BREAK, THE FOLLOWING IS *SHAPE* BTN ACTION*/
/*
* 
*/
		drawPensil.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				draw_Line = true;
				draw_Oval = false;
				draw_Rect = false;
			}
		});
		
		drawCircle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				draw_Line = false;
				draw_Oval = true;
				draw_Rect = false;
			}
		});
		
		drawRectangle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				draw_Line = false;
				draw_Oval = false;
				draw_Rect = true;
			}
		});
		
		
		
/* LINE BREAK, THE FOLLOWING IS *COLOR* BTN ACTION*/
/*
 * 
 */
		btnGrey.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#B5B1B1"));
				paint.setStroke(Color.web("#B5B1B1"));
			}
		});
		
		btnBlack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#000000"));	
				paint.setStroke(Color.web("#000000"));
			}
		});
		
		btnBrown.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#8B1A1A"));	
				paint.setStroke(Color.web("#8B1A1A"));
			}
		});
		
		btnGold.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#FFC125"));	
				paint.setStroke(Color.web("#FFC125"));
			}
		});
		
		btnGreen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#458B00"));	
				paint.setStroke(Color.web("#458B00"));
			}
		});
		
		btnDarkCyan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#7AC5CD"));	
				paint.setStroke(Color.web("#7AC5CD"));
			}
		});
		
		btnDarkBlue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#00008B"));	
				paint.setStroke(Color.web("#00008B"));
			}
		});
		
		btnPurple.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#68228B"));	
				paint.setStroke(Color.web("#68228B"));
			}
		});
		
		btnChocolate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#CD6600"));	
				paint.setStroke(Color.web("#CD6600"));
			}
		});
		
		btnOrange.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#FF8C00"));	
				paint.setStroke(Color.web("#FF8C00"));
			}
		});
		
		btnViolet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#BF3EFF"));	
				paint.setStroke(Color.web("#BF3EFF"));
			}
		});
		
		btnBlue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#0000FF"));	
				paint.setStroke(Color.web("#0000FF"));
			}
		});
		
		btnCyan.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#98F5FF"));	
				paint.setStroke(Color.web("#98F5FF"));
			}
		});
		
		btnLime.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#7FFF00"));	
				paint.setStroke(Color.web("#7FFF00"));
			}
		});
		
		btnYellow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.web("#FFFF00"));	
				paint.setStroke(Color.web("#FFFF00"));
			}
		});
		
		btnRed.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				paint.setFill(Color.RED);	
				paint.setStroke(Color.RED);
			}
		});
	}
	
	private int getColorIndex() {
		// TODO Auto-generated method stub
		Paint color = paint.getFill();
		if(color.equals(Color.web("#B5B1B1"))){
			return 1;			
		}else if(color.equals(Color.web("#000000"))){
			return 2;
		}else if(color.equals(Color.web("#8B1A1A"))){
			return 3;
		}else if(color.equals(Color.web("#FFC125"))){
			return 4;
		}else if(color.equals(Color.web("#458B00"))){
			return 5;
		}else if(color.equals(Color.web("#7AC5CD"))){
			return 6;
		}else if(color.equals(Color.web("#00008B"))){
			return 7;
		}else if(color.equals(Color.web("#68228B"))){
			return 8;
		}else if(color.equals(Color.web("#CD6600"))){
			return 9;
		}else if(color.equals(Color.web("#FF8C00"))){
			return 10;
		}else if(color.equals(Color.web("#BF3EFF"))){
			return 11;
		}else if(color.equals(Color.web("#0000FF"))){
			return 12;
		}else if(color.equals(Color.web("#98F5FF"))){
			return 13;
		}else if(color.equals(Color.web("#7FFF00"))){
			return 14;
		}else if(color.equals(Color.web("#FFFF00"))){
			return 15;
		}else{
			return 16;
		}
		
	}
	
	public void setColor(int color) {
		// TODO Auto-generated method stub
		switch (color){
		
		case 16:
			paint.setFill(Color.RED);	
			paint.setStroke(Color.RED);			
			break;
		case 15:
			paint.setFill(Color.web("#FFFF00"));	
			paint.setStroke(Color.web("#FFFF00"));			
			break;
		case 14:
			paint.setFill(Color.web("#7FFF00"));	
			paint.setStroke(Color.web("#7FFF00"));			
			break;
		case 13:
			paint.setFill(Color.web("#98F5FF"));	
			paint.setStroke(Color.web("#98F5FF"));			
			break;
		case 12:
			paint.setFill(Color.web("#0000FF"));	
			paint.setStroke(Color.web("#0000FF"));			
			break;
		case 11:
			paint.setFill(Color.web("#BF3EFF"));	
			paint.setStroke(Color.web("#BF3EFF"));			
			break;
		case 10:
			paint.setFill(Color.web("#FF8C00"));	
			paint.setStroke(Color.web("#FF8C00"));			
			break;
		case 9:
			paint.setFill(Color.web("#CD6600"));	
			paint.setStroke(Color.web("#CD6600"));			
			break;
		case 8:
			paint.setFill(Color.web("#68228B"));	
			paint.setStroke(Color.web("#68228B"));			
			break;
		case 7:
			paint.setFill(Color.web("#00008B"));	
			paint.setStroke(Color.web("#00008B"));			
			break;
		case 6:
			paint.setFill(Color.web("#7AC5CD"));	
			paint.setStroke(Color.web("#7AC5CD"));			
			break;
		case 5:
			paint.setFill(Color.web("#458B00"));	
			paint.setStroke(Color.web("#458B00"));			
			break;
		case 4:
			paint.setFill(Color.web("#FFC125"));	
			paint.setStroke(Color.web("#FFC125"));			
			break;
		case 3:
			paint.setFill(Color.web("#8B1A1A"));	
			paint.setStroke(Color.web("#8B1A1A"));			
			break;
		case 2:
			paint.setFill(Color.web("#000000"));	
			paint.setStroke(Color.web("#000000"));			
			break;
		case 1:
			paint.setFill(Color.web("#B5B1B1"));	
			paint.setStroke(Color.web("#B5B1B1"));			
			break;			
		}
		
	}
	
	public void clearCanvas() {
		paint.clearRect(0, 0, 500, 350);
    }
	
	public void drawLine(double x, double y) {
		paint.strokeLine(x, y, x+ServerConstants.LINE_SIZE, y+ServerConstants.LINE_SIZE);
    }
	
	public void drawRectangle(double x, double y) {
		paint.fillRect(x, y, ServerConstants.RECTANGE_SIZE, ServerConstants.RECTANGE_SIZE);
    }
	
	public void drawOval(double x, double y) {
		paint.fillOval(x, y, ServerConstants.CIRCLE_SIZE, ServerConstants.CIRCLE_SIZE);
    }
	
	public void drawLine(double x1, double y1, double x2, double y2) {
		paint.strokeLine(x1, y1, x2, y2);
    }
	
	public void drawRectangle(double x, double y, double width, double height) {
		paint.fillRect(x, y, width, height);
    }
	
	public void drawOval(double x, double y, double width, double height) {
		paint.fillOval(x, y, width, height);
    }
	
	public void setTitle(Stage primaryStage) {
		primaryStage.setTitle(nickName.get());		
	}


	private void register() {
		if (System.getSecurityManager() == null) {

			System.setProperty("java.security.policy", "wideopen.policy");
			System.setSecurityManager(new SecurityManager());
		}
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry("localhost",50000);
			chat = (ServerInterface) registry.lookup(ServerConstants.name);

			TextInputDialog dialog = new TextInputDialog("Name");
			dialog.setTitle("Please enter your name");
			dialog.setContentText("Please enter your name:");

			nickName = dialog.showAndWait();
			if (nickName.isPresent()){
				clientCallback = new ClientCallbackImpl(this, nickName.get());
				chat.register(clientCallback);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
	}


}
	
	
