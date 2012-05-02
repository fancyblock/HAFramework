/**
 * 
 */
package haframework;

import haframework.draw.GLGameView;
import haframework.draw.RenderList;
import haframework.draw.SpriteFactory;
import haframework.file.FileManager;
import haframework.gui.UIManager;
import haframework.sound.SoundManager;
import haframework.task.TaskManager;
import haframework.task.conf.Config;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author hjb
 *
 */
public class HAApp implements IHAApp, Renderer
{
	//-------------------------------------------- static member ---------------------------------------------------
	
	static public float SCREEN_RATIO_WID = 1.0f;
	static public float SCREEN_RATIO_HEI = 1.0f;
	
	static public float SCREEN_LOGIC_WID = 320;	// default width
	static public float SCREEN_LOGIC_HEI = 480;	// default height
	
	static protected Activity m_activity = null;
	
	//-------------------------------------------- private member --------------------------------------------------
	
	protected GLGameView m_gameView = null;
	private Config m_config = null;
	private boolean m_isRunning = false;
	
	// for calculate FPS
	private long m_t1 = 0;
	private long m_t2 = 0;
	
	private long countTime = 0;
	private int frameCount = 0;
	private int circleTime = 5000;		//interval of the print the FPS
	
	//-------------------------------------------- public function -------------------------------------------------
	
	/**
	 * @desc	set the screen ratio
	 */
	static public void SetActivity( Activity activity )
	{
		m_activity = activity;
		
		try
		{
			// set the activity fullscreen
			m_activity.requestWindowFeature( Window.FEATURE_NO_TITLE );
			m_activity.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
			
			// get screen display
	        Display dsp = m_activity.getWindowManager().getDefaultDisplay();
	        int deviceWid = dsp.getWidth();
	        int deviceHei = dsp.getHeight();
	        
	        // set the screen ratio
			SCREEN_RATIO_WID = (float)deviceWid / SCREEN_LOGIC_WID;
			SCREEN_RATIO_HEI = (float)deviceHei / SCREEN_LOGIC_HEI;

		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
        	e.printStackTrace();
		}
	}
	
	/**
	 * @desc	constructor
	 */
	public HAApp()
	{
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void GameLoop( float dt )
	{	
		main( dt );
		draw( dt );
	}
	
	/**
	 * @desc	return the fps
	 * @return
	 */
	public int GetMaxFPS()
	{
		return m_config._maxFPS;
	}

	/* (non-Javadoc)
	 * @see haframework.IHAApp#Create(haframework.task.conf.Config)
	 */
	@Override
	public boolean Create(Config conf)
	{
		m_config = conf;
		
		m_gameView = new GLGameView( m_activity );
		m_gameView.setRenderer( this );
		m_gameView.setRenderMode( GLSurfaceView.RENDERMODE_CONTINUOUSLY );
		m_activity.setContentView( m_gameView );
		
		// initial the managers
		SpriteFactory.Singleton().SetContext( m_activity );
		SoundManager.Singleton().SetContext( m_activity );
		FileManager.Singleton().SetContext( m_activity );
		
		//initial the configuration
		initialConfig();
		
		onCreate();
		
		return true;
	}

	/* (non-Javadoc)
	 * @see haframework.IHAApp#Destory()
	 */
	@Override
	public void Destory()
	{
		onDestory();
		
		TaskManager.Singleton().Quit();
		UIManager.Singleton().CleanWidget();
		
		m_isRunning = false;
	}
	
	/* (non-Javadoc)
	 * @see haframework.IHAApp#Start()
	 */
	@Override
	public void Start()
	{
		m_isRunning = true;

		System.out.println( "[--------------- Game Loop Start ---------------]" );
	}
	
	/* (non-Javadoc)
	 * @see haframework.IHAApp#Stop()
	 */
	@Override
	public void Stop()
	{
		m_isRunning = false;
		
		System.out.println( "[--------------- Game Loop Stop ---------------]" );
	}
	
	/* (non-Javadoc)
	 * @see haframework.IHAApp#Pause()
	 */
	@Override
	public void Pause()
	{
		m_gameView.onPause();
		
		m_isRunning = false;
		
		System.out.println( "[--------------- Game Loop Pause ---------------]" );
	}
	
	/* (non-Javadoc)
	 * @see haframework.IHAApp#Resume()
	 */
	@Override
	public void Resume()
	{
		m_gameView.onResume();
		
		m_isRunning = true;
		
		System.out.println( "[--------------- Game Loop Resume ---------------]" );
	}

	/* (non-Javadoc)
	 * @see haframework.IHAApp#IsRunning()
	 */
	@Override
	public boolean IsRunning()
	{
		return m_isRunning;
	}
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl)
	{
		try
		{
			float dt = (float)( m_t2 - m_t1 );
			m_t1 = m_t2;
			
			if( m_isRunning )
			{
				this.GameLoop( dt );
				RenderList.Render( gl );
			}
			
			m_t2 = System.currentTimeMillis();
			
			// calculate the FPS and print it
			frameCount++;
			if( m_t2 - countTime > circleTime )
			{
				System.out.println( "FPS: " + frameCount * 1000 / ( m_t2 - countTime) );
				frameCount = 0;
				countTime = m_t2;
			}
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		try
		{
			gl.glViewport(0, 0, width, height);
	
	        gl.glMatrixMode(GL10.GL_PROJECTION);
	        gl.glLoadIdentity();
			gl.glOrthof( 0, HAApp.SCREEN_LOGIC_WID - 1, HAApp.SCREEN_LOGIC_HEI - 1, 0, 0, -100.0f );
			
			gl.glMatrixMode( GL10.GL_MODELVIEW );
			gl.glLoadIdentity();
			
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		try
		{
			gl.glDisable( GL10.GL_DITHER );
			gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST );
			
			gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
			gl.glShadeModel( GL10.GL_SMOOTH );
	        gl.glEnable( GL10.GL_DEPTH_TEST );
	        gl.glEnable( GL10.GL_TEXTURE_2D );
	        
	        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
	        gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
	        gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
	
			gl.glActiveTexture( GL10.GL_TEXTURE0 );
			
			gl.glEnable( GL10.GL_BLEND );
			gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
			
			RenderList.Initial( gl );
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc	callback function for the subclass
	 */
	public void onCreate(){}
	public void onDestory(){}

	//-------------------------------------------- private function -------------------------------------------------
	
	// main loop
	protected void main( float ms )
	{
		TaskManager.Singleton().ProcessPending();
		
		UIManager.Singleton().UIMain( ms );
		TaskManager.Singleton().Main( ms );
	}
	
	// rendering
	protected synchronized void draw( float ms )
	{
		RenderList.Clear();
		
		TaskManager.Singleton().Draw( ms );
		UIManager.Singleton().UIDraw();
	}
	
	// initial the congifiguration
	protected void initialConfig()
	{
		if( this.m_config._orientation == Config.ORIENTATION_LANDSCAPE )
		{
			m_activity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
		}
		else if( this.m_config._orientation == Config.ORIENTATION_PORTRAIT )
		{
			m_activity.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		}
		
		HAApp.SCREEN_LOGIC_WID = this.m_config._width;
		HAApp.SCREEN_LOGIC_HEI = this.m_config._height;
		
		// TODO Auto-generated method stub
	}

}

