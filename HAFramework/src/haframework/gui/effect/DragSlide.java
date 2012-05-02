/**
 * 
 */
package haframework.gui.effect;

import haframework.events.TouchEvent;

/**
 * @author	hjb
 * @desc	this class is used to implement the ui effect that drag & slide ui widget in 1d
 */
public class DragSlide
{
	//-------------------------------------------------- static member ------------------------------------------------
	
	final static public int SlideDir_hor = 1;
	final static public int SlideDir_ver = 2;
	
	//-------------------------------------------------- private member -----------------------------------------------
	
	protected float m_minVal = 0;
	protected float m_maxVal = 0;
	protected int m_dir = SlideDir_hor;
	
	//-------------------------------------------------- public function ----------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public DragSlide( float minVal, float maxVal, int dir )
	{
		m_minVal = minVal;
		m_maxVal = maxVal;
		m_dir = dir;
	}
	
	/**
	 * @desc	pull touch event
	 * @param	evt
	 */
	public void PullEvent( TouchEvent evt )
	{
		//TODO
	}
	
	/**
	 * @desc	frame function
	 * @param	elapsed
	 */
	public void Frame( float elapsed )
	{
		//TODO
	}
	
	/**
	 * @desc	return the current position
	 * @return
	 */
	public float GetPosition()
	{
		//TODO
		
		return 0.0f;
	}

}
