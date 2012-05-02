/**
 * 
 */
package haframework.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * @author hjb
 *
 */
public class SpriteFactory
{
	//-------------------------------------------------- static member ----------------------------------------------------------
	
	static private SpriteFactory m_instance = null;
	static private boolean	m_safeFlag = false;
	
	/**
	 * @desc	return the singleton of SpriteFactory
	 * @return
	 */
	static public SpriteFactory Singleton()
	{
		if( m_instance == null )
		{
			m_safeFlag = true;
			m_instance = new SpriteFactory();
			m_safeFlag = false;
		}
		
		return m_instance;
	}
	
	//-------------------------------------------------- private member -----------------------------------------------------------
	
	private Context m_context = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public SpriteFactory()
	{
		if( m_safeFlag == false )
		{
			throw new Error( "[Error]: SpriteFactory is a singleton , can not be created directly !" );
		}
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
	 * @desc	create sprite
	 * @param	pic
	 * @return
	 */
	public Sprite CreateSprite( int texRes )
	{
		Sprite spr;
		
		checkTexture( texRes );
		
		spr = new Sprite( texRes );
		
		return spr;
	}
	
	/**
	 * @desc	create a movieclip
	 * @param	texRes
	 * @return
	 */
	public MovieClip CreateMovieClip( int texRes, float interval )
	{
		MovieClip mc;
		
		checkTexture( texRes );
		
		mc = new MovieClip( texRes, interval );
		
		return mc;
	}
	
	/**
	 * @desc	create font
	 * @param	fontFile
	 * @return
	 */
	public Font CreateFont( String fontFile, int texRes )
	{
		Font ft;
		
		ft = new Font( fontFile, texRes );
		
		return ft;
	}
	
	/**
	 * @desc	create the bitmap
	 * @param	resId
	 * @return
	 */
	public Bitmap GetBitmap( int resId )
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = false;
		Bitmap bmp = BitmapFactory.decodeResource( m_context.getResources(), resId, opt );
		
		return bmp;
	}
	
	/**
	 * @desc	create the bitmap
	 * @param	resId
	 * @param	x
	 * @param	y
	 * @param	wid
	 * @param	hei
	 * @return
	 */
	public Bitmap GetBitmap( int resId, int x, int y, int wid, int hei )
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = false;
		Bitmap bmp = BitmapFactory.decodeResource( m_context.getResources(), resId, opt );
		
		Bitmap result = Bitmap.createBitmap( bmp, x, y, wid, hei );
		bmp.recycle();
		
		return result;
	}
	
	/**
	 * @desc	check the size of the bitmap is legal or not ( if the number of width | height is pow of 2 or not )
	 * @param	width	width of the bitmap
	 * @param	height	height of the bitmap
	 * @return	
	 */
	public boolean IsBmpSizeLegal( int width, int height )
	{
		int wid = width;
		int hei = height;
		
		return ( ( wid & ( wid - 1 ) ) == 0 ) && ( ( hei & ( hei - 1 ) ) == 0 );
	}
	
	//-------------------------------------------------- private function ----------------------------------------------------------
	
	protected void checkTexture( int texRes )
	{
		if( RenderList.IsTextureExist( texRes ) == false )
		{
			Bitmap bmp = GetBitmap( texRes );
			
			int wid = bmp.getWidth();
			int hei = bmp.getHeight();
			
			// judge if the bitmap size if fit to OpenGL texture size ( 2^N x 2^N )
			if( IsBmpSizeLegal( bmp.getWidth(), bmp.getHeight() ) == false )
			{
				Bitmap newBmp = reviseBitmap( bmp, true );
				bmp.recycle();
				bmp = newBmp;
			}
			
			RenderList.CreateTexture( texRes, bmp, wid, hei );
		}
	}

	// scale the bitmap make it to the correct size for OpenGL
	private Bitmap reviseBitmap( Bitmap bmp, boolean scaleBigger )
	{
		Bitmap newBmp = null;
		
		int size = bmp.getWidth() > bmp.getHeight() ? bmp.getWidth() : bmp.getHeight();
		
		int sizeList[] = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048 };
		int i;
		int newSize = -1;
		
		if( scaleBigger )
		{
			for( i = 0; i < sizeList.length; i++ )
			{
				if( sizeList[i] >= size )
				{
					newSize = sizeList[i];
					break;
				}
			}
		}
		else
		{
			for( i = 0; i < sizeList.length; i++ )
			{
				if( sizeList[i] > size )
				{
					newSize = sizeList[i-1];
					break;
				}
			}
		}
		
		if( newSize < 0 )
		{
			throw new Error( "[Create Texture Error]: can not create the size of this texture" );
		}
		
		newBmp = Bitmap.createScaledBitmap( bmp, newSize, newSize, true );

		return newBmp;
	}
	
}
