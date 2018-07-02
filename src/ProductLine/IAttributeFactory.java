package ProductLine;

public interface IAttributeFactory {
	public double createCost(int featureID , MODE mode);
	public double createDefect(int featureID , MODE mode);
	public boolean createNotUsedBefore(int featureID, MODE mode);
	
	public double createCost(String name , MODE mode);
	public double createDefect(String name, MODE mode);
	public boolean createNotUsedBefore(String name, MODE mode);
	
	public enum MODE {  
	  RANDOM, ALL_ONE, PREDEFINE_ONE, PREDEFINE_TWO  
	}  
}
