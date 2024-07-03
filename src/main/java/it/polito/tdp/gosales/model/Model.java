package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	private GOsalesDAO dao;
	private List<String> colori;
	private List<Products> prodotti;
	private Graph<Products, DefaultWeightedEdge> grafo;
	private Map<Integer, Products>mappa;
	private List<Arco> best;
	private int n;
	private List<Arco> archi;
	
	
	
	public Model() {
		this.dao = new GOsalesDAO();
		this.colori = dao.getColori();
		this.prodotti = new ArrayList<>();
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.mappa= new HashMap<>();
	}
	
	public List<String> getColori(){
		return this.colori;
		
	}
	
	public void creaGrafo(int anno, String colore) {
		for (Products p: dao.getAllProducts()) {
			mappa.put(p.getNumber(), p);
		}
		this.prodotti = dao.getProducts(colore);
		Graphs.addAllVertices(this.grafo, prodotti);
		this.archi = dao.getArchi(anno, colore, mappa);
		for (Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getP1(),a.getP2() , a.getPeso());
		}
		System.out.println("V: "+ this.grafo.vertexSet().size()+ "\n"+ "A: "+ this.grafo.edgeSet().size());
	}
	
	public int getV() {
		return this.grafo.vertexSet().size();
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	public List<Arco> getArchi(int anno,String colore){
		List<Arco> archi = dao.getArchi(anno, colore, mappa);
		List<Arco> three = new ArrayList<>();
		Collections.sort(archi);
		 int N = Math.min(3, archi.size());
	    for (int i =0; i<N; i++) {
	    	three.add(archi.get(i));
	    }
		return three;
	}
	
	public List<Products> prodottiRip(int anno,String colore){
		List<Arco> archi = getArchi(anno, colore);
		List<Products> prodotti = new ArrayList<>(); 
		List<Products> ripetuti = new ArrayList<>(); 
		for (Arco a : archi) {
			Products p1 = a.getP1();
			Products p2 = a.getP2();
			if (prodotti.contains(p1)) {
				ripetuti.add(p1);
			}
			if (prodotti.contains(p2)) {
				ripetuti.add(p2);
			}
			prodotti.add(p2);
			prodotti.add(p1);
		}
		return ripetuti;
	}
	
	public Set<Products> getVertici(){
		return this.grafo.vertexSet();
	}

	public List<Arco> trovaPercorso(Products p){
		this.best = new ArrayList<>();
		this.n = 0;
		
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			//se il vertice iniziale corrisponde al mio vertice di partenza aggiungo l'arco
			if (this.grafo.getEdgeSource(e).equals(p)) {
				List<Arco> parziale = new ArrayList<>();
				Arco a = new Arco(p, this.grafo.getEdgeTarget(e) ,(int) this.grafo.getEdgeWeight(e) );
				parziale.add(a);
				ricorsione(parziale,this.grafo.getEdgeTarget(e));
				parziale.remove(parziale.remove(parziale.size()-1));
				
			}
		}
		return this.best;
	}
	
	
	private void ricorsione(List<Arco> parziale, Products edgeTarget) {
		//condizione di uscita 
		if (parziale.size()>= n) {
			this.best = new ArrayList<>(parziale);
			this.n = parziale.size();		
		}
		//trovo gli archi che partono dal mio ultimo veritice 
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeSource(e).equals(edgeTarget)) {
				//se ha peso maggiore di tutti gli altri (basta verificare con quello prima)
				if (decrescente(parziale, e )) {
					//se non l'ho mai aggiunto(in entrambe le direzioni)
					Arco a1 = new Arco(edgeTarget, this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e));
					Arco inverso = new Arco( this.grafo.getEdgeTarget(e), edgeTarget,(int) this.grafo.getEdgeWeight(e));
					if (!parziale.contains(inverso) && !parziale.contains(a1)) {
						parziale.add(a1);
						ricorsione(parziale, this.grafo.getEdgeTarget(e));
						parziale.remove(parziale.size()-1);
					}
				}
			}
		}
	}
	
	
	
	
	
	

	private boolean decrescente(List<Arco> parziale, DefaultWeightedEdge e) {
		double peso1 = this.grafo.getEdgeWeight(e);
		Arco a2 = parziale.get(parziale.size()-1);
		if (peso1> a2.getPeso()) {
			return true;
		}
		return false;
	}

	private boolean isConnected(DefaultWeightedEdge e, Products p) {
		if (this.grafo.getEdgeSource(e).equals(p) || this.grafo.getEdgeTarget(e).equals(p)) {
			return true;
		}
		return false;
	}
	
}
