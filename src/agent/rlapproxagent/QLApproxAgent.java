package agent.rlapproxagent;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{
	
	private FeatureFunction feature_function;
	private List<Double > weights;
	private int nbFeatures;
	
	public QLApproxAgent(double alpha, double gamma, Environnement _env, FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		feature_function = _featurefunction;
		nbFeatures = feature_function.getFeatureNb();
		weights = new ArrayList<Double>();
		for (int i = 0; i <nbFeatures; i++) {
			weights.add(Math.random());
		}
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		
		double[] features = feature_function.getFeatures(e, a);
		double Q = 0.0;
		for (int i = 0; i <nbFeatures; i++) {
			Q += features[i]*weights.get(i);
		}
		return Q;
	}
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		
		//fonction de mise a jour des poids
		double maxQ = -1000000000;
		for (Action ac: this.getActionsLegales(esuivant)) {
			if(this.getQValeur(esuivant, ac) > maxQ) {
				maxQ = this.getQValeur(esuivant, ac);
			}
		}
		
		double[] features = feature_function.getFeatures(e, a);

		for (int k = 0; k <nbFeatures; k++) {
			if(features[k] != 0) {
				double new_weight = weights.get(k) + alpha*(reward + gamma*maxQ - this.getQValeur(e, a))*features[k];
				weights.set(k, new_weight);
			}
		}
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
		
		this.weights.clear();
		//this.feature_function.reset();
		
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}
