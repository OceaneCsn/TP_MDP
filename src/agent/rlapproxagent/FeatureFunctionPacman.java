package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;
	
	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {
		
	}

	@Override
	public int getFeatureNb() {
		return 4;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[4];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}
		
		//etat du pacman si il fait l'action a
		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));
		 

		//phi0 : biais
		vfeatures[0] = 1;
		
		//phi1 : nombre de fantomes pouvant atteindre la position suivante du pacman en un pas
		int nbGhosts = stategamepacman.getNumberOfGhosts();
		vfeatures[1] = 0;
		for (int i = 0; i < nbGhosts; i++) {
			StateAgentPacman ghost = stategamepacman.getGhostState(i);
			if(Math.abs(ghost.getX()-pacmanstate_next.getX())+Math.abs(ghost.getY()-pacmanstate_next.getY())==1) {
				vfeatures[1] ++;
			}
		}
				
		//phi2 : presence d'un pacdot si l'agent fait l'action a
		if(stategamepacman.getClosestDot(pacmanstate_next)==0) {
			vfeatures[2] = 1;
		}
		else {
			vfeatures[2] = 0;
		}
		
		//phi3 : distance au pacdot
		double size = stategamepacman.getMaze().getSizeX()*stategamepacman.getMaze().getSizeX();
		vfeatures[3] = stategamepacman.getClosestDot(pacmanstate_next)/size;
		
		return vfeatures;
	}

	public void reset() {
		vfeatures = new double[4];
		
	}

}
