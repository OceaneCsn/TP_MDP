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
	
	private int nbFeatures;
	private double[] features;
	private HashMap<HashMap<Etat, Action>, Integer> indices;
	private int index;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		index = -1;
		nbFeatures = _nbEtat*_nbAction;
		features = new double[nbFeatures];
		indices = new HashMap<HashMap<Etat, Action>, Integer>();
	}
	
	@Override
	public int getFeatureNb() {
		return nbFeatures;
	}

	@Override
	public double[] getFeatures(Etat e, Action a){
		int ind;
		HashMap<Etat, Action> etat_action= new HashMap<Etat, Action>();
		etat_action.put(e, a);
		// on ajoute les features et leur index dans la feature function au fur et a mesure
		// qu'on les rencontre
		// ainsi, si on rencontre deux fois le même etat, son indice sera deja connu, sinon, on
		// ajoutera un 1 au dernier indice connu +1.
		if(!indices.containsKey(etat_action)){
			index +=1;
			ind = index;
			indices.put(etat_action, ind);
		}
		else {
			ind = indices.get(etat_action);
			
		}
		for(int i = 0; i <nbFeatures; i++) {
			features[i] = 0.0;
			if(i == ind) {
				features[i] = 1.0;
			}
		}
		
		return features;
	}
}
