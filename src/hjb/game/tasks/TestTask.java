/**
 * 
 */
package hjb.game.tasks;

import haframework.draw.Sprite;
import haframework.draw.SpriteFactory;
import haframework.task.Task;
import hjb.game.R;

/**
 * @author hjb
 *
 */
public class TestTask extends Task
{
	protected Sprite m_gbWar = null;
	protected Sprite m_camark = null;
	protected float m_angle = 0;
	
	/**
	 * @desc	constructor
	 */
	public TestTask()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void vBegin()
	{
		m_gbWar = SpriteFactory.Singleton().CreateSprite( R.drawable.gbw );
		m_gbWar.SetUV( 0.0f, 0.0f, 1.0f, 1.0f );

		m_camark = SpriteFactory.Singleton().CreateSprite( R.drawable.camark );
		m_camark.SetUV( 0.0f, 0.0f, 1.0f, 1.0f );
	}
	
	public void vMain( float elapsed )
	{
		m_angle += 0.01f;
	}
	
	public void vDraw( float elapsed )
	{
		m_gbWar.Draw( 0, 0, 200, 200 );
		
		m_camark.SetAnchor( 100, 100 );
		m_camark.Draw( 360, 160, 200, 200, m_angle );
	}

}
