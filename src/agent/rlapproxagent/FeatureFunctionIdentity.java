package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	
	private int nbFeatures;
	private int nbEtat;
	private int nbAction;
	private double[] features;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		
		nbFeatures = _nbEtat*_nbAction;
		nbAction = _nbAction;
		nbEtat = _nbEtat;
		features = new double[nbFeatures];
	}
	
	@Override
	public int getFeatureNb() {
		return nbFeatures;
	}

	@Override
	public double[] getFeatures(Etat e, Action a){
		
		for(int i = 0; i <nbFeatures; i++) {
			features[i] = 0.0;
			//if() mettre le 1 là ou il faut : ?
		}
		return null;
	}
	

}
