package it.polito.tdp.gosales;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.Arco;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Products;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCercaPercorso;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbColore;

    @FXML
    private ComboBox<Products> cmbProdotto;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextArea txtResGrafo;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCercaPercorso(ActionEvent event) {
    	cmbProdotto.getItems().addAll(model.getVertici());
    	Products p = cmbProdotto.getValue();
    	model.trovaPercorso(p);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    int anno = cmbAnno.getValue();
    String colore = cmbColore.getValue();
    if (anno ==0 || colore.compareTo("")==0) {
    	txtResGrafo.setText("Inserisci un colore e un anno");
    	return;
    }
    model.creaGrafo(anno, colore);
    txtResGrafo.appendText("#V: "+ model.getV());
    txtResGrafo.appendText("#A: "+ model.getA());
    List<Arco> archi = model.getArchi(anno, colore);
    int N = Math.min(3, archi.size());
    for (int i =0; i<N; i++) {
    	txtArchi.appendText(archi.get(i)+"\n");
    }
    txtArchi.appendText("\n"+ "I prodotti ripetuti sono: "+"\n");
    List<Products> ripetuti = model.prodottiRip(anno, colore);
    for (Products p: ripetuti) {
    	txtArchi.appendText(p+ " ");
    }
    
    }

    @FXML
    void initialize() {
        assert btnCercaPercorso != null : "fx:id=\"btnCercaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbColore != null : "fx:id=\"cmbColore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResGrafo != null : "fx:id=\"txtResGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbAnno.getItems().add(2015);
    	cmbAnno.getItems().add(2016);
    	cmbAnno.getItems().add(2017);
    	cmbAnno.getItems().add(2018);
    	cmbColore.getItems().addAll(model.getColori());
    }

}
