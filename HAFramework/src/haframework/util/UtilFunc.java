/**
 * 
 */
package haframework.util;


/**
 * @author hjb
 *
 */
public class UtilFunc
{
	
	/**
	 * @desc	clamp function
	 * @param	value
	 * @param	min
	 * @param	max
	 */
	static public float Clamp( float value, float min, float max )
	{
		if( value > max )
		{
			return max;
		}
		else if( value < min )
		{
			return min;
		}
		
		return value;
	}
	
	/**
	 * @desc	return the max value
	 * @param	val1
	 * @param	val2
	 * @return
	 */
	static public float Max( float val1, float val2 )
	{
		if( val1 > val2 )
		{
			return val1;
		}
		
		return val2;
	}
	
	/**
	 * @desc	return the max value
	 * @param	val1
	 * @param	val2
	 * @return
	 */
	static public int Max( int val1, int val2 )
	{
		if( val1 > val2 )
		{
			return val1;
		}
		
		return val2;
	}
	
	/**
	 * @desc	return the min value
	 * @param	val1
	 * @param	val2
	 * @return
	 */
	static public float Min( float val1, float val2 )
	{
		if( val1 < val2 )
		{
			return val1;
		}
		
		return val2;
	}
	
	static public int Min( int val1, int val2 )
	{
		if( val1 < val2 )
		{
			return val1;
		}
		
		return val2;
	}

	/**
	 * @desc	
	 * @param	val1
	 * @param	val2
	 * @param	scalar
	 * @return
	 */
	public static float Lerp( float val1, float val2, float scalar )
	{
		return ( 1.0f - scalar ) * val1 + scalar * val2;
	}
	
}
