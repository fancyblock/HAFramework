/**
 * 
 */
package haframework.sound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * @author hjb
 *
 */
public class SoundManager implements OnLoadCompleteListener
{
	//-------------------------------------------------- static member ----------------------------------------------------------

	static private int MAX_SOUND = 3;
	static private SoundManager m_instance = null;
	static private boolean	m_safeFlag = false;
	static private boolean m_soundOn = true;
	static private boolean m_BMGOn = true;
	
	/**
	 * @desc	return the singleton of SoundManager
	 * @return
	 */
	static public SoundManager Singleton()
	{
		if( m_instance == null )
		{
			m_safeFlag = true;
			m_instance = new SoundManager();
			m_safeFlag = false;
		}
		
		return m_instance;
	}
	
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	private Context m_context = null;
	private SoundPool m_soundPool = null;
	private HashMap<String, soundInfo> m_allSounds = null;
	private HashMap <String, MediaPlayer> m_allBGM = null; 
	private MediaPlayer m_curBGM = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public SoundManager()
	{
		if( m_safeFlag == false )
		{
			throw new Error( "[Error]: SoundManager is a singleton , can not be created directly !" );
		}
		
		initial();
	}
	
	/**
	 * @desc	set the context for get resource
	 * @param	context
	 */
	public void SetContext( Context context )
	{
		m_context = context;
	}
	
	/**
	 * @desc	load the sound to the buffer
	 * @param	resId
	 * @param	name
	 * @return
	 */
	public boolean LoadSound( int resId, String name )
	{
		if( m_allSounds.containsKey( name ) )
		{
			return false;
		}
		int soundId = m_soundPool.load( m_context, resId, 1 );
		soundInfo info = new soundInfo();
		info._soundId = soundId;
		info._loaded = false;
		info._playPending = false;
		m_allSounds.put( name, info );
		
		return true;
	}
	
	public static void SetSound(boolean isOn)
	{
		m_soundOn = isOn;
	}
	public static void SetBMG(boolean isOn)
	{
		m_BMGOn = isOn;
	}
	
	/**
	 * @desc	load background music
	 * @param	resId
	 * @param	name
	 * @return
	 */
	public boolean LoadSoundBGM( int resId, String name )
	{
		if( m_allBGM.containsKey( name ) )
		{
			return false;
		}
		
		MediaPlayer mp = MediaPlayer.create( m_context, resId );
		m_allBGM.put( name, mp ); 
		
		return true;
	}
	
	/**
	 * @desc	play sound as bgm
	 * @param	name
	 */
	public void PlayBGM( String name )
	{
		playSound( name, -1 );
	}
	
	/**
	 * @desc	play sound as sound effect
	 * @param	name
	 */
	public void PlaySE( String name )
	{
		playSound( name, 0 );
	}
	
	// callback when sound load complete
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
	{
		try
		{
			Set<String> keySet = m_allSounds.keySet();
			Iterator<String> keys = keySet.iterator();
			
			while( keys.hasNext() == true )
			{
				String key = keys.next();
				soundInfo info = m_allSounds.get( key );
				
				if( info._soundId == sampleId )
				{
					if( info._loaded )
					{
						throw new Error( "[Error]: the soundId already be loaded !" );
					}
					
					info._loaded = true;
					m_soundPool.setLoop( sampleId, info._loop );
					
					if( info._playPending )
					{
						info._playPending = false;
						playSound( key, info._loop );
					}
					
					break;
				}
			}
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------- private function ----------------------------------------------------------
	
	// initial the sound manager
	protected void initial()
	{
		m_soundPool = new SoundPool( MAX_SOUND, AudioManager.STREAM_MUSIC, 0 );
		m_soundPool.setOnLoadCompleteListener( this );
		
		m_allSounds = new HashMap<String, soundInfo>();
		m_allBGM = new HashMap<String, MediaPlayer>();  
	}
	
	// play sound
	protected void playSound( String name, int loop )
	{
		soundInfo info = m_allSounds.get( name );
		
		if( (loop == -1) && m_BMGOn )		// it's BGM
		{
			MediaPlayer mp = m_allBGM.get( name );
			if( m_curBGM == mp )
			{
				return;
			}
			else
			{
				if( m_curBGM != null )
				{
					m_curBGM.pause();
				}
				
				m_curBGM = mp;
				m_curBGM.setLooping( true );
				m_curBGM.seekTo( 0 );
				m_curBGM.start();
			}
		}
		
		if( m_allSounds.containsKey( name) && m_soundOn )
		{
			if( info._loaded == false )
			{
				info._playPending = true;
				info._loop = loop;
				return;
			}
			
			int soundId = info._soundId;
			m_soundPool.play( soundId, 1, 1, 0, 0, 1 );
		}
	}

	/**
	 * @return	if the sound effect is play or not
	 */
	public static boolean IsSFXOn() 
	{
		return m_soundOn;
	}

	/**
	 * @return	if the BGM is play or not
	 */
	public static boolean IsBGMOn() 
	{
		return m_BMGOn;
	}

	/**
	 * @desc	stop all the sound
	 */
	public void StopAll()
	{
		if( m_curBGM != null )
		{
			m_curBGM.pause();
			m_curBGM = null;
		}
	}

}

class soundInfo
{
	int _soundId;
	boolean _loaded;
	boolean _playPending;
	int _loop;
}
