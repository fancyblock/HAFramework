/**
 * 
 */
package haframework.draw;

/**
 * @author hjb
 *
 */
public class Color
{
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	public int _r;
	public int _g;
	public int _b;
	public int _a;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public Color()
	{
		_r = 0;
		_g = 0;
		_b = 0;
		_a = 1;
	}
	
	/**
	 * @desc	constructor
	 */
	public Color( int r, int g, int b, int a )
	{
		_r = r;
		_g = g;
		_b = b;
		_a = a;
	}
	
	/**
	 * @desc	constructor
	 * @param	r
	 * @param	g
	 * @param	b
	 * @param	a
	 */
	public Color( float r, float g, float b, float a )
	{
		//TODO
	}
	
	/**
	 * @desc	set color
	 * @param	r
	 * @param	g
	 * @param	b
	 * @param	a
	 */
	public void Set( int r, int g, int b, int a )
	{
		_r = r;
		_g = g;
		_b = b;
		_a = a;
	}

}
