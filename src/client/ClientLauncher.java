package client;

import gui.HomePageGui;


public class ClientLauncher {
    public void launch(){
		new HomePageGui().launch();
	}
    
    public static void main(String[] args) {
        new ClientLauncher().launch();
    }
}


