/**
 * 
 */
package haframework.gui;

import haframework.events.TouchEvent;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author hjb
 *
 */
public class UIWidget
{

	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected UIWidget m_parent = null;
	protected int m_x = 0;
	protected int m_y = 0;
	protected int m_screenX = 0;
	protected int m_screenY = 0;
	protected int m_width;
	protected int m_height;
	protected boolean m_show = true;
	protected boolean m_enable = true;
	
	protected ArrayList<UIWidget> m_children = null;
	protected boolean m_unBlockEvent;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public UIWidget()
	{
		m_children = new ArrayList<UIWidget>();
	}
	
	/**
	 * @desc	main logic
	 * @param	dt
	 */
	public void uiMain( float dt )
	{
		if( m_parent != null )
		{
			m_screenX = m_parent.m_screenX + m_x;
			m_screenY = m_parent.m_screenY + m_y;
		}
		
		vMain( dt );
		
		UIWidget child;
		int len = m_children.size();
		for( int i = 0; i < len; i++ )
		{
			child = m_children.get( i );
			child.uiMain( dt );
		}
	}
	
	/**
	 * @desc	render
	 */
	public void uiDraw()
	{
		if( m_show == false )
		{
			return;
		}
		
		vDraw();
		
		UIWidget child;
		int len = m_children.size();
		for( int i = 0; i < len; i++ )
		{
			child = m_children.get( i );
			child.uiDraw();
		}
		
		vDrawFG();
	}
	
	/**
	 * @desc	process event
	 * @param	events
	 * @return
	 */
	public boolean uiEvent( Vector<TouchEvent> events )
	{
		if( m_enable == false || m_show == false )
		{
			return false;
		}
		
		UIWidget child;
		int len = m_children.size();
		for( int i = 0; i < len; i++ )
		{
			child = m_children.get( i );
			if( child.uiEvent( events ) == true )
			{
				return true;
			}
		}
		
		boolean process = vEvent( events );
		if( m_unBlockEvent == true )
		{
			return false;
		}
		
		return process;
	}
	
	/**
	 * @desc	set the parent of this widget
	 * @param	parent
	 */
	public void SetParent( UIWidget parent )
	{
		if( m_parent == parent )
		{
			return;
		}
		
		if( m_parent != null )
		{
			m_parent.RemoveChild( this );
		}
		
		m_parent = parent;
		
		if( m_parent != null )
		{
			m_parent.m_children.add( this );
		}
	}
	
	/**
	 * @desc	set position of this widget
	 * @param	x
	 * @param	y
	 */
	public void SetPos( int x, int y )
	{
		m_x = x;
		m_y = y;
	}
	
	/**
	 * @desc	return x position
	 * @return
	 */
	public int GetPosX()
	{
		return m_x;
	}
	
	/**
	 * @desc	return y position
	 * @return
	 */
	public int GetPosY()
	{
		return m_y;
	}
	
	/**
	 * @desc	set the screen position of this widget
	 * @param	x
	 * @param	y
	 */
	public void SetScreenPos( int x, int y )
	{
		m_screenX = x;
		m_screenY = y;
	}
	
	/**
	 * @desc	return the x position of this widget
	 * @return
	 */
	public int GetScreenX()
	{
		return m_screenX;
	}
	
	/**
	 * @desc	return the y position of this widget
	 * @return
	 */
	public int GetScreenY()
	{
		return m_screenY;
	}
	
	/**
	 * @desc	set size of this widget
	 * @param	wid
	 * @param	hei
	 */
	public void SetSize( int wid, int hei )
	{
		m_width = wid;
		m_height = hei;
	}
	
	/**
	 * @desc	return the width of this widget
	 * @return
	 */
	public int GetWidth()
	{
		return m_width;
	}
	
	/**
	 * @desc	return the height of this widget
	 * @return
	 */
	public int GetHeight()
	{
		return m_height;
	}
	
	/**
	 * @desc	show or hide
	 * @param	val
	 */
	public void Show( boolean val )
	{
		m_show = val;
	}
	
	/**
	 * @desc	return visible status of this widget
	 * @return
	 */
	public boolean IsShow()
	{
		return m_show;
	}
	
	/**
	 * @desc	enable or disable
	 * @param	val
	 */
	public void Enable( boolean val )
	{
		m_enable = val;
	}
	
	/**
	 * @desc	remove the child
	 * @param	child
	 */
	public void RemoveChild( UIWidget child )
	{
		m_children.remove( child );
		child.m_parent = null;
	}
	
	/**
	 * @desc	remove children
	 */
	public void RemoveAllChild()
	{
		UIWidget child;
		int len = m_children.size();
		for( int i = 0; i < len; i++ )
		{
			child = m_children.get( i );
			child.m_parent = null;
		}
		
		m_children.clear();
	}
	
	/**
	 * @desc	force render
	 */
	public void ForceDraw()
	{
		vDraw();
	}
	
	/**
	 * @desc	return the children
	 * @return
	 */
	public ArrayList<UIWidget> GetChildren()
	{
		return m_children;
	}
	
	/**
	 * @desc	
	 * @param	val
	 */
	public void UnBlockEvent( boolean val )
	{
		m_unBlockEvent = val;
	}
	
	//-------------------------------------------------- private function ----------------------------------------------------------

	protected void vMain( float dt ){}
	protected void vDraw(){}
	protected void vDrawFG(){}
	protected boolean vEvent( Vector<TouchEvent> events ){ return false; }
	
	// test if the point is in this widget or not
	protected boolean isInArea( int x, int y )
	{
		if( x >= m_screenX && x <= ( m_screenX + m_width ) && y >= m_screenY && y <= ( m_screenY + m_height ) )
		{
			return true;
		}
		
		return false;
	}

}

