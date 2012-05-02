/**
 * 
 */
package haframework.draw;


/**
 * @author hjb
 *
 */
public class Sprite
{
	//--------------------------------------------------- render info -------------------------------------------------------------
	
	public float _u1;
	public float _v1;
	public float _u2;
	public float _v2;

	public float _x1;
	public float _y1;
	public float _x2;
	public float _y2;
	public float _x3;
	public float _y3;
	public float _x4;
	public float _y4;
	
	public float _col1_r;
	public float _col1_g;
	public float _col1_b;
	public float _col1_a;
	public float _col2_r;
	public float _col2_g;
	public float _col2_b;
	public float _col2_a;
	public float _col3_r;
	public float _col3_g;
	public float _col3_b;
	public float _col3_a;
	public float _col4_r;
	public float _col4_g;
	public float _col4_b;
	public float _col4_a;
	
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected int m_texRes = 0;
	
	protected float m_width = 0;
	protected float m_height = 0;
		
	protected float m_anchorX = 0;
	protected float m_anchorY = 0;
	
	protected float m_x = 0;
	protected float m_y = 0;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public Sprite()
	{
	}
	
	/**
	 * @desc	construcor
	 * @param	texRes
	 * @param	tex
	 */
	public Sprite( int texRes )
	{	
		m_texRes = texRes;
		
		_col1_r = _col2_r = _col3_r = _col4_r = 0.2f;
		_col1_g = _col2_g = _col3_g = _col4_g = 1.0f;
		_col1_b = _col2_b = _col3_b = _col4_b = 0.2f;
		_col1_a = _col2_a = _col3_a = _col4_a = 1.0f;
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#Draw()
	 */
	public void Draw()
	{
		_x1 = m_x - m_anchorX;
		_y1 = m_y - m_anchorY;
		
		_x3 = _x1 + m_width;
		_y3 = _y1 + m_height;
		
		_x2 = _x3;
		_y2 = _y1;
		
		_x4 = _x1;
		_y4 = _y3;
		
		RenderList.AddSprite( this );
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#Draw(int, int)
	 */
	public void Draw( float x, float y )
	{
		_x1 = x - m_anchorX;
		_y1 = y - m_anchorY;
		
		_x3 = _x1 + m_width;
		_y3 = _y1 + m_height;
		
		_x2 = _x3;
		_y2 = _y1;
		
		_x4 = _x1;
		_y4 = _y3;
		
		RenderList.AddSprite( this );
	}
	
	/**
	 * @desc	draw sprite
	 * @param	x
	 * @param	y
	 * @param	wid
	 * @param	hei
	 */
	public void Draw( float x, float y, float wid, float hei )
	{
		_x1 = x - m_anchorX;
		_y1 = y - m_anchorY;
		
		_x3 = _x1 + wid;
		_y3 = _y1 + hei;
		
		_x2 = _x3;
		_y2 = _y1;
		
		_x4 = _x1;
		_y4 = _y3;
		
		RenderList.AddSprite( this );
	}
	
	/**
	 * @desc	draw the sprite
	 * @param	x
	 * @param	y
	 * @param	wid
	 * @param	hei
	 * @param	angle
	 */
	public void Draw( float x, float y, float wid, float hei, double angle )
	{
		_x1 = -m_anchorX;
		_y1 = -m_anchorY;
		
		_x3 = wid - m_anchorX;
		_y3 = hei - m_anchorY;
		
		_x2 = _x3;
		_y2 = _y1;
		
		_x4 = _x1;
		_y4 = _y3;
		
		//rotate
		double cosC = Math.cos( angle );
		double sinC = Math.sin( angle );
		float newX;
		float newY;
		newX = (float)( _x1 * cosC - _y1 * sinC );
		newY = (float)( _x1 * sinC + _y1 * cosC );
		_x1 = newX;
		_y1 = newY;
		newX = (float)( _x2 * cosC - _y2 * sinC );
		newY = (float)( _x2 * sinC + _y2 * cosC );
		_x2 = newX;
		_y2 = newY;
		newX = (float)( _x3 * cosC - _y3 * sinC );
		newY = (float)( _x3 * sinC + _y3 * cosC );
		_x3 = newX;
		_y3 = newY;
		newX = (float)( _x4 * cosC - _y4 * sinC );
		newY = (float)( _x4 * sinC + _y4 * cosC );
		_x4 = newX;
		_y4 = newY;
		
		_x1 += x;
		_y1 += y;
		_x2 += x;
		_y2 += y;
		_x3 += x;
		_y3 += y;
		_x4 += x;
		_y4 += y;
		
		RenderList.AddSprite( this );
	}
	
	/**
	 * @desc	
	 * @param	x
	 * @param	y
	 * @param	angle
	 */
	public void Draw( float x, float y, double angle )
	{
		_x1 = -m_anchorX;
		_y1 = -m_anchorY;
		
		_x3 = m_width - m_anchorX;
		_y3 = m_height - m_anchorY;
		
		_x2 = _x3;
		_y2 = _y1;
		
		_x4 = _x1;
		_y4 = _y3;
		
		//rotate
		double cosC = Math.cos( angle );
		double sinC = Math.sin( angle );
		float newX;
		float newY;
		newX = (float)( _x1 * cosC - _y1 * sinC );
		newY = (float)( _x1 * sinC + _y1 * cosC );
		_x1 = newX;
		_y1 = newY;
		newX = (float)( _x2 * cosC - _y2 * sinC );
		newY = (float)( _x2 * sinC + _y2 * cosC );
		_x2 = newX;
		_y2 = newY;
		newX = (float)( _x3 * cosC - _y3 * sinC );
		newY = (float)( _x3 * sinC + _y3 * cosC );
		_x3 = newX;
		_y3 = newY;
		newX = (float)( _x4 * cosC - _y4 * sinC );
		newY = (float)( _x4 * sinC + _y4 * cosC );
		_x4 = newX;
		_y4 = newY;
		
		_x1 += x;
		_y1 += y;
		_x2 += x;
		_y2 += y;
		_x3 += x;
		_y3 += y;
		_x4 += x;
		_y4 += y;
		
		RenderList.AddSprite( this );
	}
	
	/**
	 * @desc	draw sprite
	 * @param	x
	 * @param	y
	 * @param	col
	 */
	public void Draw( float x, float y, Color col )
	{
		Draw( x, y );
		
		//TODO			for temp
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#SetAnchor(float, float)
	 */
	public void SetAnchor(float x, float y)
	{
		m_anchorX = x;
		m_anchorY = y;
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#SetUV(int, int, int, int)
	 */
	public void SetUV(int u, int v, int wid, int hei)
	{
		m_width = wid;
		m_height = hei;
		
		int texWid = RenderList.GetTextureWidth( m_texRes );
		int texHei = RenderList.GetTextureHeight( m_texRes );
		
		_u1 = (float)u / (float)texWid;
		_v1 = (float)v / (float)texHei;
		_u2 = (float)(u + wid) / (float)texWid;
		_v2 = (float)(v + hei) / (float)texHei;
	}
	
	/**
	 * @desc	set uv by float
	 * @param	u1
	 * @param	v1
	 * @param	u2
	 * @param	v2
	 */
	public void SetUV( float u1, float v1, float u2, float v2 )
	{
		_u1 = u1;
		_v1 = v1;
		_u2 = u2;
		_v2 = v2;
	}
	
	/**
	 * @desc	set the sprite size
	 * @param	wid
	 * @param	hei
	 */
	public void SetSize( float wid, float hei )
	{
		m_width = wid;
		m_height = hei;
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#GetWidth()
	 */
	public float GetWidth()
	{
		return m_width;
	}

	/* (non-Javadoc)
	 * @see haframework.draw.ISprite#GetHeight()
	 */
	public float GetHeight()
	{
		return m_height;
	}
	
	public int GetTexRes()
	{
		return m_texRes;
	}
	
	/**
	 * @desc	return the resource id
	 * @return
	 */
	public int GetResId()
	{
		return m_texRes;
	}

}
