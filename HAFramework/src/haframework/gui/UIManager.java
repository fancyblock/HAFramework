/**
 * 
 */
package haframework.gui;

import haframework.events.TouchEvent;

import java.util.Vector;



/**
 * @author hjb
 *
 */
public class UIManager
{
	//-------------------------------------------------- static member ----------------------------------------------------------
	
	static private UIManager m_instance = null;
	static private boolean	m_safeFlag = false;
	
	/**
	 * @desc	return the singleton of TaskManager
	 * @return
	 */
	static public UIManager Singleton()
	{
		if( m_instance == null )
		{
			m_safeFlag = true;
			m_instance = new UIManager();
			m_safeFlag = false;
		}
		
		return m_instance;
	}

	//-------------------------------------------------- private member -----------------------------------------------------------
	
	private UIWidget m_root = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * 
	 */
	public UIManager()
	{
		if( m_safeFlag == false )
		{
			throw new Error( "[Error]: UIManager is a singleton , can not be created directly !" );
		}
		
		m_root = new UIWidget();

	}
	
	/**
	 * @desc	add a widget to the ui root
	 * @param	widget
	 */
	public void AddToRoot( UIWidget widget )
	{
		widget.SetParent( m_root );
	}
	
	/**
	 * @desc	remove all the ui
	 */
	public void CleanWidget()
	{
		m_root.RemoveAllChild();
	}
	
	/**
	 * @desc	ui main
	 * @param	dt
	 */
	public void UIMain( float dt )
	{
		m_root.uiMain( dt );
	}
	
	/**
	 * @desc	draw ui
	 */
	public void UIDraw()
	{
		m_root.uiDraw();
	}
	
	/**
	 * @desc	pupp events for ui process
	 * @param	events
	 * @return
	 */
	public boolean onTouchEvent( Vector<TouchEvent> events )
	{
		return m_root.uiEvent( events );
	}

}
