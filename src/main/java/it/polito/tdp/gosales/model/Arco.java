package it.polito.tdp.gosales.model;

import java.util.Objects;

public class Arco implements Comparable<Arco>{
	private Products p1;
	private Products p2;
	private int peso;
	public Arco(Products p1, Products p2, int peso) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.peso = peso;
	}
	public Products getP1() {
		return p1;
	}
	public void setP1(Products p1) {
		this.p1 = p1;
	}
	public Products getP2() {
		return p2;
	}
	public void setP2(Products p2) {
		this.p2 = p2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		return Objects.hash(p1, p2, peso);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		return Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2) && peso == other.peso;
	}
	@Override
	public int compareTo(Arco o) {
		return o.peso-this.peso;
	}
	@Override
	public String toString() {
		return  p1.getNumber() + " <--> " + p2.getNumber() + "   - " + peso;
	}
	
	
}
