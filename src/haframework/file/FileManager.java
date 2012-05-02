/**
 * 
 */
package haframework.file;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;


/**
 * @author hjb
 *
 */
public class FileManager
{
	//-------------------------------------------------- static member ----------------------------------------------------------
	
	static private FileManager m_instance = null;
	static private boolean	m_safeFlag = false;
	
	/**
	 * @desc	return the singleton of FileManager
	 * @return
	 */
	static public FileManager Singleton()
	{
		if( m_instance == null )
		{
			m_safeFlag = true;
			m_instance = new FileManager();
			m_safeFlag = false;
		}
		
		return m_instance;
	}
	
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	protected Context m_context = null;
	protected Resources m_resource = null;
	protected AssetManager m_assetManager = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public FileManager()
	{
		if( m_safeFlag == false )
		{
			throw new Error( "[Error]: FileManager is a singleton , can not be created directly !" );
		}
		
		initial();
	}
	
	/**
	 * @desc	set context
	 * @param	context
	 */
	public void SetContext( Context context )
	{
		m_context = context;
		
		m_resource = context.getResources();
		m_assetManager = context.getAssets();
	}
	
	/**
	 * @desc	regist a file 
	 * @param	name	file name
	 * @param	resId	resource id in Android res catalogue
	 * @return
	 */
	public boolean Regist( String name, int resId )
	{
		//TODO
		
		return true;
	}
	
	/**
	 * @desc	open the file in the asset catalogue
	 * @param	fileName
	 * @return	if file is not exist, return false
	 */
	public InputStream OpenFile( String fileName )
	{
		InputStream stream = null;
		
		try
		{
			stream = m_assetManager.open( fileName, AssetManager.ACCESS_RANDOM );
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
			e.printStackTrace();
		}
		
		return stream;
	}
	
	/**
	 * @desc	open the file in the res catalogue
	 * @param	resId	the id that is in R.java
	 * @return
	 */
	public InputStream OpenFile( int resId )
	{
		InputStream stream = null;
		
		stream = m_resource.openRawResource( resId );
		
		return stream;
	}
	
	//-------------------------------------------------- private function ----------------------------------------------------------
	
	// initial the sound manager
	protected void initial()
	{
		//TODO
	}

}
