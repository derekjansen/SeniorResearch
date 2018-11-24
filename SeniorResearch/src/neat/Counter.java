package neat;

import java.io.Serializable;

public class Counter implements Serializable {
	
	private int currentInnovation = 0;
	
	public int getInnovation() {
		return currentInnovation++;
	}
        
        public void setInnocation(int i){
            this.currentInnovation = i;
        }
}
