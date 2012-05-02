/**
 * 
 */
package haframework.draw;

import java.util.ArrayList;

/**
 * @author hjb
 *
 */
public class MovieClip
{
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected int m_texRes = 0;
	protected float m_interval = 0;
	protected Sprite m_sprite = null;
	protected ArrayList<frameInfo> m_allFrames = null;
	
	protected float m_time;
	protected float m_x;
	protected float m_y;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public MovieClip()
	{
	}
	
	/**
	 * @desc	constructor
	 * @param	interval
	 */
	public MovieClip( int texRes, float interval )
	{
		m_texRes = texRes;
		m_interval = interval;
		
		m_sprite = SpriteFactory.Singleton().CreateSprite( texRes );
		m_allFrames = new ArrayList<frameInfo>();
		
		m_time = 0.0f;
	}
	
	/**
	 * @desc	add frame
	 * @param	u
	 * @param	v
	 * @param	w
	 * @param	h
	 * @param	ancX
	 * @param	ancY
	 */
	public void AddFrame( int u, int v, int w, int h, int ancX, int ancY )
	{
		frameInfo fi = new frameInfo();
		fi.Set( u, v, w, h, ancX, ancY );
		
		m_allFrames.add( fi );
	}
	
	/**
	 * @desc	update state
	 * @param	dt
	 */
	public void Update( float dt )
	{
		m_time += dt;
	}
	
	/**
	 * @desc	render
	 */
	public void Draw()
	{
		int frameIndex = (int)( m_time / m_interval );
		//frameIndex = UtilFunc.Min( frameIndex, m_allFrames.size() - 1 );
		frameIndex = frameIndex % m_allFrames.size();
		
		frameInfo fi = m_allFrames.get( frameIndex );
		m_sprite.SetUV( fi._u, fi._v, fi._wid, fi._hei );
		m_sprite.SetAnchor( fi._ancX, fi._ancY );
		
		m_sprite.Draw( m_x, m_y );
	}
	
	/**
	 * @desc	reset state
	 */
	public void Reset()
	{
		m_time = 0.0f;
	}
	
	/**
	 * @desc	set position
	 * @param	x
	 * @param	y
	 */
	public void SetPos( float x, float y )
	{
		m_x = x;
		m_y = y;
	}

}

class frameInfo
{
	public int _u;
	public int _v;
	public int _wid;
	public int _hei;
	public int _ancX;
	public int _ancY;
	
	// set the data
	public void Set( int u, int v, int wid, int hei, int ancX, int ancY )
	{
		_u = u;
		_v = v;
		_wid = wid;
		_hei = hei;
		_ancX = ancX;
		_ancY = ancY;
	}
}

