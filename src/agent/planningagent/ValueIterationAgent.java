package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param nbIterations
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
		//*** Initialisation arbitraire de la map � 0
		System.out.println("constructor with gamma");
		for(Etat etat : mdp.getEtatsAccessibles()) {
			System.out.println(etat);
			V.put(etat, 0.0);
		}
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);
		V = new HashMap<Etat,Double>();
		//*** Initialisation arbitraire de la map � 0
		System.out.println("constructor without gamma");
		for(Etat etat : mdp.getEtatsAccessibles()) {
			System.out.println(etat);
			V.put(etat, 0.0);
		}
	}
	
	/*class created so nextIterationV() could return V values
	 * AND best politique for the whole grid
	 */
	final class valuesActions {
		private final HashMap<Etat,Double> nextV;
		private final HashMap<Etat,List<Action>> nextPolitique;
		
		public valuesActions(HashMap<Etat,Double> nextV, HashMap<Etat,List<Action>> nextPolitique)
		{
			this.nextV = nextV;
			this.nextPolitique = nextPolitique;
		}
		
		public HashMap<Etat,Double> getV_values()
		{
			return nextV;
		}
		
		public HashMap<Etat,List<Action>> get_politique()
		{
			return nextPolitique;
		}
	}
	/**
	 * Prepare la mise a jour de V : calcule V_k(s) en fonction de V_{k-1}(s')
	 * et renvoie : la nouvelle table V (pour updateV) 
	 * et la table des actions possibles (pour getPolitique)
	 */

	public valuesActions nextIterationV()
	{
		HashMap<Etat, Double> futureV = V;
		HashMap<Etat,List<Action>> allBestActions = new HashMap<Etat,List<Action>>();
		for(Etat s : V.keySet()) {
			ArrayList<Action> bestActions = new ArrayList<Action>();
			Double somme = 0.0;
			// iterate on all possible actions to find the best one (ie with higher somme)
			for(Action a : mdp.getActionsPossibles(s)) {
				double cdt = 0;
				try {
					Map<Etat, Double> transitions = mdp.getEtatTransitionProba(s, a);
					// sum over all possibles Etats for this action
					for(Map.Entry<Etat, Double> transi : mdp.getEtatTransitionProba(s, a).entrySet()) {
						cdt = cdt + transi.getValue()*(mdp.getRecompense(s, a, transi.getKey()) + gamma*V.get(transi.getKey()));
						if(cdt >= somme) {
							somme = cdt;
							bestActions.add(a);
							futureV.put(s, somme);
						}
					}
				}
				catch(Exception ex) {
					System.out.println("Action non autoris�e dans cet �tat : "+ex);
				}
			}
			allBestActions.put(s,bestActions);
		}
		
		return new valuesActions(futureV,allBestActions);
	}
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		System.out.println("V was updated");
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta=0.0;
		//*** VOTRE CODE
		V = nextIterationV().getV_values();
		System.out.println(V.values());
		
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		vmax = Collections.max(V.values(),null);
		vmin = Collections.min(V.values(),null);
		
		
		
		//******************* laisser notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune action possible, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE
		List<Action> possibleActions = getPolitique(e);
		if (!possibleActions.isEmpty())
		{
			int randindex = ThreadLocalRandom.current().nextInt(0, possibleActions.size());
			return possibleActions.get(randindex);
		}
		else 
		{
			return Action2D.NONE;
		}
	}
	@Override
	public double getValeur(Etat _e) {
		//*** VOTRE CODE
		return V.get(_e);
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		List<Action> returnactions = nextIterationV().get_politique().get(_e);
	
		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}
