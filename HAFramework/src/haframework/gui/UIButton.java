/**
 * 
 */
package haframework.gui;

import haframework.draw.Sprite;
import haframework.draw.SpriteFactory;
import haframework.events.TouchEvent;
import haframework.gui.uievent.IButtonCallback;

import java.util.Vector;

/**
 * @author hjb
 *
 */
public class UIButton extends UIWidget
{
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected IButtonCallback m_callback = null;
	protected boolean m_isDown = false;
	protected boolean m_tracking = false;
	
	protected Sprite m_up = null;
	protected Sprite m_down = null;
	protected Sprite m_disable = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public UIButton( int resId, int upU, int upV, int downU, int downV, int wid, int hei )
	{
		m_up = SpriteFactory.Singleton().CreateSprite( resId );
		m_down = SpriteFactory.Singleton().CreateSprite( resId );
		
		m_up.SetUV( upU, upV, wid, hei );
		m_down.SetUV( downU, downV, wid, hei );
		
		this.SetSize( wid, hei );
	}
	
	/**
	 * @desc	constructor 2
	 */
	public UIButton( int resId, int upU, int upV, int downU, int downV, int disableU, int disableV, int wid, int hei )
	{
		m_up = SpriteFactory.Singleton().CreateSprite( resId );
		m_down = SpriteFactory.Singleton().CreateSprite( resId );
		m_disable = SpriteFactory.Singleton().CreateSprite( resId );
		
		m_up.SetUV( upU, upV, wid, hei );
		m_down.SetUV( downU, downV, wid, hei );
		m_disable.SetUV( disableU, disableV, wid, hei );
		
		this.SetSize( wid, hei );
	}
	
	/**
	 * @desc	set button callback
	 * @param 	callback
	 */
	public void SetCallback( IButtonCallback callback )
	{
		m_callback = callback;
	}
	
	/**
	 * @desc	return if button is down or not
	 * @return
	 */
	public boolean IsDown()
	{
		return m_isDown;
	}
	
	/**
	 * @desc	set button status
	 * @param	val
	 */
	public void SetDown( boolean val )
	{
		m_isDown = val;
	}
	
	/**
	 * @desc
	 */
	protected void vMain( float dt )
	{
	}
	
	/**
	 * @desc
	 */
	protected void vDraw()
	{
		if( m_enable == true )
		{
			if( m_isDown == true )
			{
				m_down.Draw( m_screenX, m_screenY );
			}
			else
			{
				m_up.Draw( m_screenX, m_screenY );
			}
		}
		else
		{
			if( m_disable != null )
			{
				m_disable.Draw( m_screenX, m_screenY );
			}
			else
			{
				m_up.Draw( m_screenX, m_screenY );
			}
		}
	}

	/**
	 * @desc
	 */
	protected boolean vEvent( Vector<TouchEvent> events )
	{
		// if the button enable is false, do not process any event
		if( m_enable == false )	return false;
		
		TouchEvent evt = events.get( 0 );
		
		switch( evt.Type )
		{
			case TouchEvent.TOUCH:
				if( this.isInArea( evt.X, evt.Y ) == true )
				{
					m_tracking = true;
					m_isDown = true;
					if( m_callback != null )
					{
						m_callback.onButtonDown( this );
					}
					
					return true;
				}
				break;
			case TouchEvent.UNTOUCH:
				boolean btnDown = m_isDown;
				m_tracking = false;
				m_isDown = false;
				if( btnDown == true && m_callback != null )
				{
					m_callback.onButtonClick( this );
					m_callback.onButtonUp( this );
					
					return true;
				}
				break;
			case TouchEvent.MOVE:
				if( m_tracking == true )
				{
					if( this.isInArea( evt.X, evt.Y ) == true )
					{
						m_isDown = true;
					}
					else
					{
						if( m_isDown == true && m_callback != null )
						{
							m_callback.onButtonUp( this );
						}
						
						m_isDown = false;
					}
					
					return true;
				}
				break;
			default:
				break;
		}
		
		return false; 
	}

}
