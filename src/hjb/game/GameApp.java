/**
 * 
 */
package hjb.game;

import haframework.HAApp;
import hjb.game.tasks.TestTask;

/**
 * @author hjb
 *
 */
public class GameApp extends HAApp
{

	/**
	 * @desc	constructor
	 */
	public GameApp()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate()
	{
		( new TestTask() ).Start( 0 );
	}
	
	@Override
	public void onDestory()
	{
		//TODO
	}

}
