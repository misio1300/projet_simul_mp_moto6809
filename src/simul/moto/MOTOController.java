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
	private TextField valPc;
	@FXML
	private TextField regDP;
	@FXML
	private TextField regPU;
	@FXML
	private TextField regPS;
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
	private TextField acumD;
	@FXML
	byte[] mem = new byte[65536];
	@FXML
	private TextField regX;
	@FXML
	private TextField regY;
	
	private Logique logic;
	private String programme;
	private String err;
	private String[] lignes;
	private int i=0;
	private boolean compileerr = true;
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
	/*@FXML
	private void enter()
	{

		//this.setAcumA("A");
		//this.setAcumB("B");
		//regX.setText("F");
		//logic.ecrireMemoire(0x000A, 0x0A);
		logic.ecrireMemoire(0x0007, 0x34);
		logic.ecrireMemoire(0x0000, 0x01);
		logic.ecrireMemoire(0x0001, 0x0E);
		logic.ecrireMemoire(0x010E, 0x0A);
		enterclick = true;
		 programme = this.gettxtCLI();
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
	}*/
	int compileprog = 0;
	@FXML
	private void compile()
	{
		programme = this.gettxtCLI();
		 err = logic.fetch(programme);
		 if(err != null)
		 {
		    Alert al = new Alert(Alert.AlertType.ERROR);
		    al.setTitle("Erreur de programme");
		    al.setHeaderText(err);
		    al.show();
		    return; 
		 }
		 else if(compileprog > 0 && programme.equals(this.gettxtCLI()))
		 {
			 Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur de programme");
			    al.setHeaderText("Le programme n'a pas été modifié.");
			    al.show();
			    return;
		 }
		 else 
		 {
			 logic.progMemoire(programme);
			 this.insertTabProg();
			 lignes = programme.split("\\R");
			 this.setProxIns(lignes[0]);
			 this.compileerr = false;
			 compileprog++;
			 this.err = "null";
		 }
	}
	@FXML
	private void run()
	{
		if(compileerr)
		{
			if(err != null)
			{
				Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur d'execution");
			    al.setHeaderText(err + " Dans l'editeur du texte.");
			    al.show();
			    return;
			}
			else
			{
				Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur d'execution");
			    al.setHeaderText("Le programme n'a pas été compilé.");
			    al.show();
			    return;
			}
		}
		else 
		{
			 int j=0;
			 int adress = 0xFC00;
			 String[] lignes = programme.split("\\R");
			 this.setProxIns(lignes[j]);
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
			        if(j < lignes.length-1) 
		        	{
				        adress = adress + logic.nbOct(instruct, logic.decode(mod));
				        this.setValPc(String.format("%04X", adress));
		        		this.setProxIns(lignes[j + 1]);
		        	}
			        j++;
		     }
		}
		
	}
	int adress;
	@FXML
	private void stepbystep()
	{
		if(compileerr)
		{
			if(err != null)
			{
				Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur d'execution");
			    al.setHeaderText(err + " Dans l'editeur du texte.");
			    al.show();
			    return;
			}
			else
			{
				Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur d'execution");
			    al.setHeaderText("Le programme n'a pas été compilé.");
			    al.show();
			    return;
			}
		}
		else
		{
			if(i==0)
			{
				adress = 0xFC00;
				lignes = programme.split("\\R");
			}
			if(i >= lignes.length - 1)
			{
				Alert al = new Alert(Alert.AlertType.ERROR);
			    al.setTitle("Erreur d'execution");
			    al.setHeaderText("Fin du programme.");
			    al.show();
			    return;
			}
				String[] mot = lignes[i].split("\\s+");
		        String instruct = mot[0];
		        String mod = (mot.length > 1) ? mot[1] : "";
		        Logique.execute(instruct, mod);
		        adress = adress + logic.nbOct(instruct, logic.decode(mod));
		        this.setValPc(String.format("%04X", adress));
		        if(i < lignes.length-1) 
	        	{
	        		this.setProxIns(lignes[i + 1]);
	        	}	
			i++;
		}
	}
	@FXML
	private void reset()
	{
		this.init();
		this.compileprog = 0;
		tabProg.getItems().clear();
		i = 0;
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
    	    	//String pc = String.format("%04X", adress);
    	    	//this.setValPc(pc);
    	    }
	    	else
	    	{
		    	//String pc = String.format("%04X", adress);
		    	//this.setValPc(pc);
		        ObservableList<String> ligne = FXCollections.observableArrayList(
		        String.format("%04X", adress),
		        instruction);
		    	tabProg.getItems().add(ligne);// adiciona linha individualmente
		    	String[] mot = instruction.split("\\s+");
		        adress += (logic.nbOct(mot[0], logic.decode((mot.length > 1) ? mot[1] : "")));//instruction.length();
	    	}
	    }
	    this.setValPc(String.format("%04X", 0xFC00));
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
	public void setValPc(String b) {
		javafx.application.Platform.runLater(() -> { this.valPc.setText(b);});
	}
	public String getValPc() {
		return valPc.getText();
	}
	public void setAcumA(String b) {
		javafx.application.Platform.runLater(() -> { this.acumA.setText(b);});
	}
	public String getAcumA() 
	{
		return this.acumA.getText();
	}
	public void setAcumD(String b) {
		javafx.application.Platform.runLater(() -> { this.acumD.setText(b);});
	}
	public String getAcumD() 
	{
		return this.acumD.getText();
	}
	public String getRegX() {
		return regX.getText();
	}
	public void setRegX(String x) {
		javafx.application.Platform.runLater(() -> { this.regX.setText(x);});
	}
	public String getRegY() {
		return regY.getText();
	}
	public void setRegY(String x) {
		javafx.application.Platform.runLater(() -> { this.regY.setText(x);});
	}
	public String getRegDP() {
		return regDP.getText();
	}
	public void setRegDP(String x) {
		javafx.application.Platform.runLater(() -> { this.regDP.setText(x);});
	}
	public String getRegPU() {
		return regPU.getText();
	}
	public void setRegPU(String x) {
		javafx.application.Platform.runLater(() -> { this.regPU.setText(x);});
	}
	public String getRegPS() {
		return regPS.getText();
	}
	public void setRegPS(String x) {
		javafx.application.Platform.runLater(() -> { this.regPS.setText(x);});
	}
	public void setFlags(String x) {
		javafx.application.Platform.runLater(() -> { this.flags.setText(x);});
	}
	public void atualiserRAMTab() {
	    tabRam.getItems().clear();

	    for (int adr = 0; adr < Memoire.RAM_SIZE; adr++) {
	        int val = Memoire.lire(adr);
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
	        int val = Memoire.lire(adr);
	        ObservableList<String> ligne = FXCollections.observableArrayList(
	            String.format("%04X", adr),
	            String.format("%02X", val)
	        );
	        tabRom.getItems().add(ligne);
	    }
	}
	public void atualiserCaseMemoire(int address) {
	    int val = Memoire.lire(address);
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
		this.setAcumD("0000");
		this.setValPc("");
		this.setRegX("0000");
		this.setRegY("0000");
		this.setRegPS("0000");
		this.setRegPU("0000");
		this.setRegDP("00");
		this.setProxIns("");
		this.setTxtCli("");
		this.initFlags();
	}


}
