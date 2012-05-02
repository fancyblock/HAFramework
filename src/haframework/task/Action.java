/**
 * 
 */
package haframework.task;

/**
 * @author hejiabin
 *
 */
public class Action 
{
	/**
	 * @desc	constructor
	 */
	public Action() 
	{
	}
	
	public void vEnter(){}
	public void vLeave(){}
	public boolean vUpdate( float elapsed ){ return true; }
	public void vDraw( float elapsed ){}

}
