package agent.rlagent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;


import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	protected List<Action> former_actions_possibles;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma, _env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		former_actions_possibles = _env.getActionsPossibles(_env.getEtatCourant());
	}
	
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}
		double maxtmp = -10000000000.0;
		for(Action a : this.getActionsLegales(e)) {
			
			if(this.getQValeur(e, a)>maxtmp ) {
				maxtmp = this.getQValeur(e, a);
				returnactions = new ArrayList<Action>();
				returnactions.add(a);
			}
			if(this.getQValeur(e, a) == maxtmp ) {
				returnactions.add(a);
			}
		}
		return returnactions;		
	}
	
	@Override
	public double getValeur(Etat e) {
		
		if(qvaleurs.containsKey(e)) {
			return Collections.max(qvaleurs.get(e).values());
		}
		
		return 0.0;
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		if(qvaleurs.containsKey(e)) {
			return qvaleurs.get(e).get(a);
		}
		return 0.0;
	}
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		if(qvaleurs.containsKey(e)) {
			HashMap<Action, Double> tmpMap = new HashMap<Action, Double>(qvaleurs.get(e));
			tmpMap.put(a, d);
			qvaleurs.put(e,tmpMap);
		}
		else {
			HashMap<Action,Double> valeurAction = new HashMap<Action,Double>();
			for (Action ac : env.getActionsPossibles(e)) {
				if(ac.equals(a)) {
					valeurAction.put(ac, d);
				}
				else {
					valeurAction.put(ac, 0.0);
				}
				
			}
			qvaleurs.put(e, valeurAction);
		}
		
		ArrayList<Double> maxs = new ArrayList<Double>();
		ArrayList<Double> mins = new ArrayList<Double>();
		for (Etat et:qvaleurs.keySet()) {
			maxs.add(Collections.max(qvaleurs.get(et).values(),null));
			mins.add(Collections.min(qvaleurs.get(et).values(),null));
		}
		vmax = Collections.max(maxs);	
		vmin = Collections.min(mins);	
		this.notifyObs();
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL) 
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);
		if(!qvaleurs.containsKey(e)) {
			for (Action ac : former_actions_possibles) {
				this.setQValeur(e, ac, 0.0);
			}
		}
		if(!qvaleurs.containsKey(esuivant)) {
			for (Action ac : this.getActionsLegales(esuivant)) {
				this.setQValeur(esuivant, ac, 0.0);
			}
		}
		double maxQ = Collections.max(qvaleurs.get(esuivant).values(),null);
		double valeur = (1-alpha)*getQValeur(e,a) + alpha*(reward + gamma * maxQ);
		setQValeur(e, a, valeur);
		former_actions_possibles = env.getActionsPossibles(env.getEtatCourant());
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		qvaleurs.clear();
		this.episodeNb = 0;
		this.notifyObs();
	}









	


}
