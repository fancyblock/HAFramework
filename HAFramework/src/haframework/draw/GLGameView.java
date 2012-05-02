/**
 * 
 */
package haframework.draw;

import haframework.HAApp;
import haframework.events.TouchEvent;
import haframework.gui.UIManager;
import haframework.task.TaskManager;

import java.util.Vector;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * @author hjb
 *
 */
public class GLGameView extends GLSurfaceView
{
	//--------------------------------------------- private member ---------------------------------------------------
	

	//--------------------------------------------- public function --------------------------------------------------
	
	/**
	 * @param context
	 */
	public GLGameView(Context context)
	{
		super(context);
		
		this.getHolder().setType( SurfaceHolder.SURFACE_TYPE_GPU );
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public GLGameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	/**
	 * @desc	callback when surface be destory
	 */
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		try
		{
			RenderList.Destory();
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		int pointerCount = event.getPointerCount();
		
		int action = event.getAction();
		Vector<TouchEvent> events = new Vector<TouchEvent>();
		TouchEvent evt = null;
		
		for( int i = 0; i < pointerCount; i++ )
		{
			evt = new TouchEvent();

			if( action == MotionEvent.ACTION_DOWN )
			{
				evt.Type = TouchEvent.TOUCH;
			}
			else if( action == MotionEvent.ACTION_UP )
			{
				evt.Type = TouchEvent.UNTOUCH;
			}
			else if( action == MotionEvent.ACTION_MOVE )
			{
				evt.Type = TouchEvent.MOVE;
			}

			evt.X = (int)( event.getX(i) / HAApp.SCREEN_RATIO_WID );
			evt.Y = (int)( event.getY(i) / HAApp.SCREEN_RATIO_HEI );
			
			events.add( evt );
		}

		if( UIManager.Singleton().onTouchEvent( events ) == false )
		{
			TaskManager.Singleton().onTouchEvent( events );
		}
		
		return true;
	}
	
	//--------------------------------------------- private function --------------------------------------------------

}
