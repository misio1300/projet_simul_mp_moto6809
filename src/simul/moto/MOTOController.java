package simul.moto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MOTOController 
{
	@FXML
	private TextArea txtCLI;
	@FXML
	private TextField txtPC;
	@FXML
	private TextField proxIns; //pour le text prox inst
	@FXML
	private TextField flags;
	@FXML
	private TableView<ObservableList<String>> tabProg;
	@FXML
	private TableColumn<ObservableList<String>, String> tabProgC1;
	@FXML
	private TableColumn<ObservableList<String>, String> tabProgC2;
	@FXML
	private TableView<ObservableList<String>> tabRam;
	@FXML
	private TableColumn<ObservableList<String>, String> tabRamC1;
	@FXML
	private TableColumn<ObservableList<String>, String> tabRamC2;
	@FXML
	private TableView<ObservableList<String>> tabRom;
	@FXML
	private TableColumn<ObservableList<String>, String> tabRomC1;
	@FXML
	private TableColumn<ObservableList<String>, String> tabRomC2;
	@FXML
	private TextField acumB;
	@FXML
	private TextField acumA;
	@FXML
	byte[] mem = new byte[65536];
	@FXML
	private TextField regX;
	private Logique logic;
	//private double x;
	private boolean enterclick = false;
	
	public void setLogic(Logique logic)
	{
		this.logic = logic;
	}
	@FXML
	public void initialize() 
	{
	    // Configura as colunas
		tabProgC1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
		tabProgC2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
		
		tabRamC1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
		tabRamC2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
		
		tabRomC1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
		tabRomC2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
		
		this.atualiserRAMTab();
		this.atualiserROMTab();
		//Memoire.reset();
		this.init();
		//this.initTabRamRom("RAM");
		//this.initTabRamRom("ROM");
	}
	@FXML
	private void enter()
	{

		//this.setAcumA("A");
		//this.setAcumB("B");
		//regX.setText("F");
		//logic.ecrireMemoire(0x000A, 0x0A);
		enterclick = true;
		 String programme = this.gettxtCLI();
		 String err = logic.fetch(programme);
		 if(err != null)
		 {
		    Alert al = new Alert(Alert.AlertType.ERROR);
		    al.setTitle("Erreur de programme");
		    al.setHeaderText(err);
		    al.show();
		    return; 
		 }
		 else
		 {
			 logic.progMemoire(programme);
			 this.insertTabProg();
			 String[] lignes = programme.split("\\R");
			 int i=0;
			 this.setProxIns(lignes[i]);
			 for (String ligne : lignes) 
		     {
		        if (ligne.isEmpty()) continue;
		       // try
		       // {
		        	//Thread.sleep(3000);
		        	String[] mot = ligne.split("\\s+");
			        String instruct = mot[0];
			        String mod = (mot.length > 1) ? mot[1] : "";
			        Logique.execute(instruct, mod);
			        if(i < lignes.length-1) 
		        	{
		        		this.setProxIns(lignes[i + 1]);
		        	}			     
			        
		       // }
		       // catch(InterruptedException e)
		       // {
		       // 	Thread.currentThread().interrupt();
		       // }
		     }
		 }
	}
	@FXML
	private void reset()
	{
		this.init();
	}

	
	// Método que processa os dados e adiciona linhas individualmente
	private void insertTabProg() 
	{
	    int adress = 0xFC00;
	    txtCLI.appendText("\n");
	
	    String[] instructions = txtCLI.getText().split("\\R"); 
	    for (String instruction : instructions) 
	    {
	    	if (instruction.isBlank() || instruction.equals("END")) 
    		{
    			ObservableList<String> ligne = FXCollections.observableArrayList(
                String.format("%04X", adress),
                "END");
    	    	tabProg.getItems().add(ligne);
    	    	String pc = "$" + String.format("%04X", adress);
    	    	this.setTxtPc(pc);
    	    }
	    	else
	    	{
		    	String pc = "$" + String.format("%04X", adress);
		    	this.setTxtPc(pc);
		        ObservableList<String> ligne = FXCollections.observableArrayList(
		        String.format("%04X", adress),
		        instruction);
		    	tabProg.getItems().add(ligne);// adiciona linha individualmente
		    	String[] mot = instruction.split("\\s+");
		        adress += (logic.nbOct(mot[0], logic.decode((mot.length > 1) ? mot[1] : "")));//instruction.length();
	    	}
	    }
	}
	/*private void initTabRamRom(String x) 
	{
	    if(x=="RAM")
	    {
	    	for (int adr = 0x0000; adr < 0xFC00; adr++) 
		    {
	            ObservableList<String> ligne = FXCollections.observableArrayList(
	            String.format("%04X", adr),
	            "00");
		    	tabRam.getItems().add(ligne);
		    }
	    }
	    	else
	    	{
	    		for (int adr = 0xFC00; adr < 0xFFFF; adr++) 
	    	    {
	                ObservableList<String> ligne = FXCollections.observableArrayList(
	                String.format("%04X", adr),
	                "00");
	    	    	tabRom.getItems().add(ligne);
	    	    }
	    	}
	}*/
	public void insertTabRamRom(/*int mem,*/ int v)
	{
		
	}
	@FXML
	private void save()
	{
		if(!enterclick)
		{
			Alert alerte = new Alert(Alert.AlertType.INFORMATION);
	    	alerte.setTitle("ERREUR!!!");
	    	alerte.setHeaderText("IMPOSSIBLE DE SAUVEGARDER");
	    	alerte.setContentText("Joindre les instructions avant de sauvegarder.");
	    	alerte.show();
		}
			
	}
	public String gettxtCLI() 
	{
		return txtCLI.getText();
	}
	public void setTxtCli(String b) {
		javafx.application.Platform.runLater(() -> { this.txtCLI.setText(b);});
	}
	public String getAcumB() 
	{
		return acumB.getText();
	}
	public void setAcumB(String b) {
		javafx.application.Platform.runLater(() -> { this.acumB.setText(b);});
	}
	public void setProxIns(String b) {
		javafx.application.Platform.runLater(() -> { this.proxIns.setText(b);});
	}
	public void setTxtPc(String b) {
		javafx.application.Platform.runLater(() -> { this.txtPC.setText(b);});
	}
	public void setAcumA(String b) {
		javafx.application.Platform.runLater(() -> { this.acumA.setText(b);});
	}
	public String getAcumA() 
	{
		return this.acumA.getText();
	}
	public String getRegX() {
		return regX.getText();
	}
	public void setRegX(String x) {
		javafx.application.Platform.runLater(() -> { this.regX.setText(x);});
	}
	public void setFlags(String x) {
		javafx.application.Platform.runLater(() -> { this.flags.setText(x);});
	}
	public void atualiserRAMTab() {
	    tabRam.getItems().clear();

	    for (int adr = 0; adr < Memoire.RAM_SIZE; adr++) {
	        int val = Memoire.lireInt(adr);
	        ObservableList<String> ligne = FXCollections.observableArrayList(
	            String.format("%04X", adr),
	            String.format("%02X", val)
	        );
	        tabRam.getItems().add(ligne);
	    }
	}
	public void atualiserROMTab() {
	    tabRom.getItems().clear();

	    for (int adr = Memoire.ROM_START; adr < Memoire.MEM_SIZE; adr++) {
	        int val = Memoire.lireInt(adr);
	        ObservableList<String> ligne = FXCollections.observableArrayList(
	            String.format("%04X", adr),
	            String.format("%02X", val)
	        );
	        tabRom.getItems().add(ligne);
	    }
	}
	public void atualiserCaseMemoire(int address) {
	    int val = Memoire.lireInt(address);
	    String valHex = String.format("%02X", val);

	    if (address < Memoire.ROM_START) {
	        tabRam.getItems().get(address).set(1, valHex);
	    } else {
	        int idx = address - Memoire.ROM_START;
	        tabRom.getItems().get(idx).set(1, valHex);
	    }
	}
	public void initFlags()
	{
		this.setFlags("0 0 0 0 0 1 0 0");
	}
	public void init()
	{
		this.setAcumA("00");
		this.setAcumB("00");
		this.setTxtPc("");
		this.setRegX("0000");
		this.setProxIns("");
		this.setTxtCli("");
		this.initFlags();
	}


}
