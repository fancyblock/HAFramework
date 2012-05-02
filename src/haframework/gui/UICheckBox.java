/**
 * 
 */
package haframework.gui;

import haframework.draw.Sprite;
import haframework.draw.SpriteFactory;
import haframework.events.TouchEvent;
import haframework.gui.uievent.ICheckBoxCallback;

import java.util.Vector;

/**
 * @author hjb
 *
 */
public class UICheckBox extends UIWidget
{
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected ICheckBoxCallback m_callback = null;
	protected boolean m_isChecked = false;
	
	protected Sprite m_up = null;
	protected Sprite m_down = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public UICheckBox( int resId, int upU, int upV, int downU, int downV, int wid, int hei )
	{
		m_up = SpriteFactory.Singleton().CreateSprite( resId );
		m_down = SpriteFactory.Singleton().CreateSprite( resId );
		
		m_up.SetUV( upU, upV, wid, hei );
		m_down.SetUV( downU, downV, wid, hei );
		
		this.SetSize( wid, hei );
	}
	
	/**
	 * @desc	set button callback
	 * @param 	callback
	 */
	public void SetCallback( ICheckBoxCallback callback )
	{
		m_callback = callback;
	}
	
	/**
	 * @desc	set the checkbox status
	 * @param	val
	 */
	public void Check( boolean val )
	{
		m_isChecked = val;
	}
	
	/**
	 * @desc	return the checkbox status
	 * @return
	 */
	public boolean IsChecked()
	{
		return m_isChecked;
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
			if( m_isChecked == true )
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
			if( m_isChecked == true )
			{
				m_down.Draw( m_screenX, m_screenY );
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
		TouchEvent evt = events.get( 0 );
		
		switch( evt.Type )
		{
			case TouchEvent.TOUCH:
				if( this.isInArea( evt.X, evt.Y ) == true )
				{
					m_isChecked = !m_isChecked;
					if( m_callback != null )
					{
						m_callback.onCheckBoxCheck( this );
					}
					
					return true;
				}
				break;
			case TouchEvent.UNTOUCH:
				break;
			case TouchEvent.MOVE:
				break;
			default:
				break;
		}
		
		return false;
	}

}
