/**
 * 
 */
package haframework;

import haframework.task.conf.Config;

/**
 * @author hjb
 *
 */
public interface IHAApp
{
	public boolean Create( Config conf );
	
	public void Destory();
	
	public void Start();
	
	public void Stop();
	
	public void Pause();
	
	public void Resume();
	
	public boolean IsRunning();
}
