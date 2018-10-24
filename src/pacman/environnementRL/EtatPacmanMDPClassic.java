package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private int nbGhosts;
	private ArrayList position;
	private ArrayList ghostPositionsX;
	private ArrayList ghostPositionsY;
	private int closestDot;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		nbGhosts = _stategamepacman.getNumberOfGhosts();
		ghostPositionsX = new ArrayList();
		ghostPositionsY = new ArrayList();
		position = new ArrayList();
		for( int g = 0; g <nbGhosts; g++) {
			System.out.println(g);
			System.out.println(nbGhosts);

			StateAgentPacman stateGhost = _stategamepacman.getGhostState(g);
			ghostPositionsX.add(_stategamepacman.getGhostState(g).getX());
			ghostPositionsY.add(_stategamepacman.getGhostState(g).getY());
		}
		
		StateAgentPacman stateAgent = _stategamepacman.getPacmanState(0);
		position.add(stateAgent.getX());
		position.add(stateAgent.getY());
		
		closestDot = _stategamepacman.getClosestDot(stateAgent);
	}
	
	@Override
	public String toString() {
		
		return "Current agent is located in "+String.valueOf(position.get(0))+" , "+String.valueOf(position.get(1));
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return clone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + closestDot;
		result = prime * result + ((ghostPositionsX == null) ? 0 : ghostPositionsX.hashCode());
		result = prime * result + ((ghostPositionsY == null) ? 0 : ghostPositionsY.hashCode());
		result = prime * result + nbGhosts;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;
		if (closestDot != other.closestDot)
			return false;
		if (ghostPositionsX == null) {
			if (other.ghostPositionsX != null)
				return false;
		} else if (!ghostPositionsX.equals(other.ghostPositionsX))
			return false;
		if (ghostPositionsY == null) {
			if (other.ghostPositionsY != null)
				return false;
		} else if (!ghostPositionsY.equals(other.ghostPositionsY))
			return false;
		if (nbGhosts != other.nbGhosts)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}



	

}
