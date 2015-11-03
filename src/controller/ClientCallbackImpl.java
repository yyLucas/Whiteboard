package controller;
import interfaces.ClientCallbackInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handle messages back from server, update the client's UI.
 * @author Yang Zhang (Lucas)
 *
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface
{
	private static final long serialVersionUID = -786208686227223575L;
	
	
	private String name;
	ClientController controller;
	ObservableList<String> clientList = FXCollections.observableArrayList();
	
	protected ClientCallbackImpl(ClientController clientController, String name) throws RemoteException {
		super();
		this.name = name;
		controller = clientController;
	}
	
	@Override
	public void updateClientList(List<String> list) throws RemoteException {
		// TODO Auto-generated method stub
		//Must use JavaFX thread to update the component, or it will throw java.lang.IllegalStateException
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	clientList.setAll(list);
        		controller.userList.setItems(clientList);
            }
       });
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public void serverCallback(String msg) throws RemoteException {
		controller.txArea.appendText(msg+"\n");
	}

	@Override
	public void drawShape(String shape, double x, double y) throws RemoteException {
		// TODO Auto-generated method stub
		if(shape.equals("line")){
			//System.out.println(shape);
			controller.drawLine(x, y);
		}else if(shape.equals("circle")){
			//System.out.println(shape);
			controller.drawOval(x, y);
		}else if(shape.equals("rectangle")){
			//System.out.println(shape);
			controller.drawRectangle(x, y);
		}
	}

	@Override
	public void setColor(int color) throws RemoteException {
		// TODO Auto-generated method stub
		controller.setColor(color);
	}

	@Override
	public void drawShape(String string, double xCor, double yCor,
			double finalX, double finalY) throws RemoteException {
		// TODO Auto-generated method stub
		if(string.equals("line")){
			//System.out.println(shape);
			controller.drawLine(xCor, yCor, finalX, finalY);
		}else if(string.equals("circle")){
			//System.out.println(shape);
			controller.drawOval(xCor, yCor, finalX, finalY);
		}else if(string.equals("rectangle")){
			//System.out.println(shape);
			controller.drawRectangle(xCor, yCor, finalX, finalY);
		}
	}

	@Override
	public void clearCanvas() throws RemoteException {
		// TODO Auto-generated method stub
		controller.clearCanvas();
	}
}
